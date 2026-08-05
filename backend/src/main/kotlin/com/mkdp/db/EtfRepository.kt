package com.mkdp.db

import com.mkdp.price.EtfSummary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

data class EtfRow(val symbol: String, val name: String, val nav: Double?)

@Repository
class EtfRepository(private val jdbc: NamedParameterJdbcTemplate) {
    fun search(query: String, page: Int, size: Int): List<EtfRow> =
        jdbc.query(
            """
            SELECT symbol, name, nav FROM etf
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
            "SELECT symbol, name, nav FROM etf WHERE symbol = :symbol",
            mapOf("symbol" to symbol),
            mapper,
        ).firstOrNull()

    fun count(): Long = jdbc.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM etf", Long::class.java) ?: 0L

    fun upsertBatch(items: List<EtfSummary>, syncedAt: LocalDateTime) {
        if (items.isEmpty()) return
        val params = items.map {
            mapOf("symbol" to it.symbol, "name" to it.name, "nav" to it.nav, "updatedAt" to syncedAt)
        }.toTypedArray()
        jdbc.batchUpdate(
            """
            INSERT INTO etf (symbol, name, nav, updated_at)
            VALUES (:symbol, :name, :nav, :updatedAt)
            ON CONFLICT (symbol) DO UPDATE SET
                name = EXCLUDED.name,
                nav = EXCLUDED.nav,
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
        )
    }
}
