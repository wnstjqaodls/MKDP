package com.mkdp.db

import com.mkdp.dart.CorpCodeRecord
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

data class CompanyRow(
    val corpCode: String,
    val corpName: String,
    val stockCode: String?,
    val modifyDate: String?,
)

@Repository
class CompanyRepository(private val jdbc: NamedParameterJdbcTemplate) {
    fun search(query: String, listedOnly: Boolean, page: Int, size: Int): List<CompanyRow> {
        val listedClause = if (listedOnly) "AND stock_code IS NOT NULL AND stock_code <> ''" else ""
        return jdbc.query(
            """
            SELECT corp_code, corp_name, stock_code, modify_date
            FROM company
            WHERE (corp_name ILIKE :like OR stock_code = :exact) $listedClause
            ORDER BY (stock_code IS NOT NULL AND stock_code <> '') DESC, corp_name
            LIMIT :size OFFSET :offset
            """.trimIndent(),
            mapOf(
                "like" to "%$query%",
                "exact" to query,
                "size" to size,
                "offset" to page * size,
            ),
            mapper,
        )
    }

    fun findByCorpCode(corpCode: String): CompanyRow? =
        jdbc.query(
            "SELECT corp_code, corp_name, stock_code, modify_date FROM company WHERE corp_code = :corpCode",
            mapOf("corpCode" to corpCode),
            mapper,
        ).firstOrNull()

    fun findByStockCode(stockCode: String): CompanyRow? =
        jdbc.query(
            "SELECT corp_code, corp_name, stock_code, modify_date FROM company WHERE stock_code = :stockCode",
            mapOf("stockCode" to stockCode),
            mapper,
        ).firstOrNull()

    fun count(): Long =
        jdbc.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM company", Long::class.java) ?: 0L

    /**
     * 기업 마스터 배치 upsert.
     *
     * v1(레거시)의 `updateCompanies`는 서비스 클래스를 매퍼 인터페이스로 잘못 넘겨(`getMapper(DisclosureService.class)`)
     * 호출 즉시 예외가 나는 상태였다. 여기서는 검증된 `NamedParameterJdbcTemplate.batchUpdate`로 대체한다.
     */
    fun upsertBatch(records: List<CorpCodeRecord>, syncedAt: LocalDateTime) {
        if (records.isEmpty()) return
        val params = records.map {
            mapOf(
                "corpCode" to it.corpCode,
                "corpName" to it.corpName,
                "stockCode" to it.stockCode,
                "modifyDate" to it.modifyDate,
                "updatedAt" to syncedAt,
            )
        }.toTypedArray()
        jdbc.batchUpdate(
            """
            INSERT INTO company (corp_code, corp_name, stock_code, modify_date, updated_at)
            VALUES (:corpCode, :corpName, :stockCode, :modifyDate, :updatedAt)
            ON CONFLICT (corp_code) DO UPDATE SET
                corp_name = EXCLUDED.corp_name,
                stock_code = EXCLUDED.stock_code,
                modify_date = EXCLUDED.modify_date,
                updated_at = EXCLUDED.updated_at
            """.trimIndent(),
            params,
        )
    }

    private val mapper = { rs: ResultSet, _: Int ->
        CompanyRow(
            corpCode = rs.getString("corp_code"),
            corpName = rs.getString("corp_name"),
            stockCode = rs.getString("stock_code"),
            modifyDate = rs.getString("modify_date"),
        )
    }
}
