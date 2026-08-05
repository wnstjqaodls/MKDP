package com.mkdp.db

import com.mkdp.price.DailyPrice
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

data class PriceRow(val date: LocalDate, val close: Long)

@Repository
class PriceHistoryRepository(private val jdbc: NamedParameterJdbcTemplate) {
    fun findRange(symbol: String, start: LocalDate, end: LocalDate): List<PriceRow> =
        jdbc.query(
            """
            SELECT date, close FROM price_history
            WHERE symbol = :symbol AND date BETWEEN :start AND :end
            ORDER BY date
            """.trimIndent(),
            mapOf("symbol" to symbol, "start" to start, "end" to end),
        ) { rs, _ -> PriceRow(date = rs.getDate("date").toLocalDate(), close = rs.getLong("close")) }

    /** 대략적인 커버리지 판단 — 요청 구간의 시작/끝 근처에 캐시된 값이 있으면 재조회를 건너뛴다. */
    fun coversRange(symbol: String, start: LocalDate, end: LocalDate): Boolean {
        val bounds = jdbc.query(
            "SELECT MIN(date) AS min_date, MAX(date) AS max_date FROM price_history WHERE symbol = :symbol",
            mapOf("symbol" to symbol),
        ) { rs, _ ->
            val minDate = rs.getDate("min_date")?.toLocalDate()
            val maxDate = rs.getDate("max_date")?.toLocalDate()
            minDate to maxDate
        }.firstOrNull() ?: return false
        val (minDate, maxDate) = bounds
        if (minDate == null || maxDate == null) return false
        return !minDate.isAfter(start.plusDays(4)) && !maxDate.isBefore(end.minusDays(4))
    }

    fun upsertBatch(symbol: String, prices: List<DailyPrice>) {
        if (prices.isEmpty()) return
        val params = prices.map {
            mapOf(
                "symbol" to symbol,
                "date" to it.date,
                "open" to it.open,
                "high" to it.high,
                "low" to it.low,
                "close" to it.close,
                "volume" to it.volume,
            )
        }.toTypedArray()
        jdbc.batchUpdate(
            """
            INSERT INTO price_history (symbol, date, open, high, low, close, volume)
            VALUES (:symbol, :date, :open, :high, :low, :close, :volume)
            ON CONFLICT (symbol, date) DO UPDATE SET
                open = EXCLUDED.open,
                high = EXCLUDED.high,
                low = EXCLUDED.low,
                close = EXCLUDED.close,
                volume = EXCLUDED.volume
            """.trimIndent(),
            params,
        )
    }
}
