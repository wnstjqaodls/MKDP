package com.mkdp.domain

import com.mkdp.dart.CorpCodeRecord
import com.mkdp.dart.CorpCodeZipParser
import com.mkdp.dart.DartClient
import com.mkdp.db.CompanyRepository
import com.mkdp.db.SyncLogRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

data class SyncResult(val success: Boolean, val rowCount: Int, val error: String? = null)

/**
 * 기업 고유번호(corpCode.xml) 전체 동기화.
 *
 * v1(레거시) `BusinessController.updateCorpCodes()` / `DisclosureService.updateCompanies()`는
 * 아래 세 가지가 모두 깨져 있어 한 번도 정상 동작한 적이 없다:
 *  1. 응답 스트림을 `String.valueOf(InputStream)`로 문자열화(객체 해시값만 저장됨)
 *  2. `new DisclosureService()`로 직접 생성해 `@Autowired SqlSessionFactory`가 항상 null
 *  3. `ssf.openSession().getMapper(DisclosureService.class)` — 매퍼가 아닌 서비스 클래스를 전달
 * v2는 스프링이 주입한 리포지토리만 사용하고, 배치 크기를 나눠 100,000건 규모에서도
 * 메모리를 안정적으로 유지한다(2GB LXC 기준).
 */
@Service
class CorpCodeSyncService(
    private val dartClient: DartClient,
    private val parser: CorpCodeZipParser,
    private val companies: CompanyRepository,
    private val syncLogs: SyncLogRepository,
) {
    private val log = LoggerFactory.getLogger(CorpCodeSyncService::class.java)

    @Synchronized
    fun sync(): SyncResult {
        val logId = UUID.randomUUID().toString()
        val startedAt = LocalDateTime.now()
        syncLogs.start(logId, startedAt)
        return try {
            val zip = dartClient.downloadCorpCodeZip()
            var total = 0
            val batch = mutableListOf<CorpCodeRecord>()
            val syncedAt = LocalDateTime.now()
            parser.parse(zip) { record ->
                batch.add(record)
                if (batch.size >= BATCH_SIZE) {
                    companies.upsertBatch(batch.toList(), syncedAt)
                    total += batch.size
                    batch.clear()
                }
            }
            if (batch.isNotEmpty()) {
                companies.upsertBatch(batch, syncedAt)
                total += batch.size
            }
            syncLogs.finish(logId, LocalDateTime.now(), total, "SUCCESS")
            log.info("기업 고유번호 동기화 완료: {}건", total)
            SyncResult(success = true, rowCount = total)
        } catch (ex: Exception) {
            log.error("기업 고유번호 동기화 실패", ex)
            syncLogs.finish(logId, LocalDateTime.now(), 0, "FAILED")
            SyncResult(success = false, rowCount = 0, error = ex.message)
        }
    }

    companion object {
        private const val BATCH_SIZE = 500
    }
}
