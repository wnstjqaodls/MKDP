package com.mkdp.db

import com.mkdp.price.EtfSummary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

data class EtfRow(
    val symbol: String,
    val name: String,
    val nav: Double?,
    val tabCode: Int?,
    val marketSum: Long?,
    val quant: Long?,
)

@Repository
class EtfRepository(private val jdbc: NamedParameterJdbcTemplate) {
    fun search(query: String, page: Int, size: Int): List<EtfRow> =
        jdbc.query(
            """
            SELECT symbol, name, nav, tab_code, market_sum, quant FROM etf
            WHERE name ILIKE :like OR symbol = :exact
            ORDER BY name
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

    fun findBySymbol(symbol: String): EtfRow? =
        jdbc.query(
            "SELECT symbol, name, nav, tab_code, market_sum, quant FROM etf WHERE symbol = :symbol",
            mapOf("symbol" to symbol),
            mapper,
        ).firstOrNull()

    fun count(): Long = jdbc.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM etf", Long::class.java) ?: 0L

    /** ETF 카테고리(tabCode)별 시가총액 또는 거래량 상위 목록. */
    fun topByCategory(tabCode: Int, sort: String, limit: Int): List<EtfRow> {
        val orderColumn = if (sort == "volume") "quant" else "market_sum"
        return jdbc.query(
            """
            SELECT symbol, name, nav, tab_code, market_sum, quant FROM etf
            WHERE tab_code = :tabCode
            ORDER BY $orderColumn DESC NULLS LAST
            LIMIT :limit
            """.trimIndent(),
            mapOf("tabCode" to tabCode, "limit" to limit),
            mapper,
        )
    }

    fun upsertBatch(items: List<EtfSummary>, syncedAt: LocalDateTime) {
        if (items.isEmpty()) return
        val params = items.map {
            mapOf(
                "symbol" to it.symbol,
                "name" to it.name,
                "nav" to it.nav,
                "tabCode" to it.tabCode,
                "marketSum" to it.marketSum,
                "quant" to it.quant,
                "updatedAt" to syncedAt,
            )
        }.toTypedArray()
        jdbc.batchUpdate(
            """
            INSERT INTO etf (symbol, name, nav, tab_code, market_sum, quant, updated_at)
            VALUES (:symbol, :name, :nav, :tabCode, :marketSum, :quant, :updatedAt)
            ON CONFLICT (symbol) DO UPDATE SET
                name = EXCLUDED.name,
                nav = EXCLUDED.nav,
                tab_code = EXCLUDED.tab_code,
                market_sum = EXCLUDED.market_sum,
                quant = EXCLUDED.quant,
                updated_at = EXCLUDED.updated_at
            """.trimIndent(),
            params,
        )
    }

    private val mapper = { rs: ResultSet, _: Int ->
        EtfRow(
            symbol = rs.getString("symbol"),
            name = rs.getString("name"),
            nav = rs.getObject("nav")?.let { rs.getDouble("nav") },
            tabCode = rs.getObject("tab_code")?.let { rs.getInt("tab_code") },
            marketSum = rs.getObject("market_sum")?.let { rs.getLong("market_sum") },
            quant = rs.getObject("quant")?.let { rs.getLong("quant") },
        )
    }
}
