package com.mkdp.domain

import com.mkdp.db.EtfRepository
import com.mkdp.db.EtfSyncLogRepository
import com.mkdp.price.NaverEtfListClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

/**
 * ETF 유니버스 동기화. DART `corpCode.xml`은 ETF를 포함하지 않으므로(신탁 구조라
 * DART 공시 대상 법인이 아님) 별도로 네이버 금융에서 가져온다.
 */
@Service
class EtfSyncService(
    private val naverEtfListClient: NaverEtfListClient,
    private val etfs: EtfRepository,
    private val syncLogs: EtfSyncLogRepository,
) {
    private val log = LoggerFactory.getLogger(EtfSyncService::class.java)

    @Synchronized
    fun sync(): SyncResult {
        val logId = UUID.randomUUID().toString()
        syncLogs.start(logId, LocalDateTime.now())
        return try {
            val items = naverEtfListClient.list()
            etfs.upsertBatch(items, LocalDateTime.now())
            syncLogs.finish(logId, LocalDateTime.now(), items.size, "SUCCESS")
            log.info("ETF 목록 동기화 완료: {}건", items.size)
            SyncResult(success = true, rowCount = items.size)
        } catch (ex: Exception) {
            log.error("ETF 목록 동기화 실패", ex)
            syncLogs.finish(logId, LocalDateTime.now(), 0, "FAILED")
            SyncResult(success = false, rowCount = 0, error = ex.message)
        }
    }
}
