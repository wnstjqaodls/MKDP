package com.mkdp.domain

import com.mkdp.db.PriceRow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BacktestServiceTest {
    private val priceSyncService: PriceSyncService = Mockito.mock(PriceSyncService::class.java)
    private val service = BacktestService(priceSyncService)

    @Test
    fun `computes total return, CAGR, and max drawdown for a single-asset buy-and-hold`() {
        val start = LocalDate.of(2025, 1, 1)
        val end = LocalDate.of(2026, 1, 1)
        val prices = listOf(
            PriceRow(LocalDate.of(2025, 1, 2), 10_000),
            PriceRow(LocalDate.of(2025, 6, 1), 8_000),
            PriceRow(LocalDate.of(2026, 1, 1), 12_000),
        )
        `when`(priceSyncService.pricesFor("005930", start, end)).thenReturn(prices)

        val result = service.run(listOf(BacktestHolding("005930", 100.0)), start, end, 1_000_000)

        assertEquals(1_200_000, result.finalValue)
        assertEquals(20.0, result.totalReturnPct, 0.01)
        assertTrue(result.cagrPct > 0)
        // 10,000 -> 8,000 구간에서 -20% 낙폭
        assertEquals(-20.0, result.mddPct, 0.01)
        assertEquals(3, result.series.size)
    }

    @Test
    fun `weights are normalized so they need not sum to exactly 100`() {
        val start = LocalDate.of(2025, 1, 1)
        val end = LocalDate.of(2025, 2, 1)
        val dates = listOf(LocalDate.of(2025, 1, 2), LocalDate.of(2025, 2, 1))
        `when`(priceSyncService.pricesFor("AAA", start, end)).thenReturn(
            listOf(PriceRow(dates[0], 100), PriceRow(dates[1], 110)),
        )
        `when`(priceSyncService.pricesFor("BBB", start, end)).thenReturn(
            listOf(PriceRow(dates[0], 200), PriceRow(dates[1], 220)),
        )

        val result = service.run(
            listOf(BacktestHolding("AAA", 1.0), BacktestHolding("BBB", 1.0)),
            start,
            end,
            1_000_000,
        )

        // 두 종목 모두 10% 상승했으니 비중 가중치와 무관하게 총수익률도 10%여야 한다.
        assertEquals(10.0, result.totalReturnPct, 0.1)
    }

    @Test
    fun `throws when a holding has no price data in range`() {
        val start = LocalDate.of(2025, 1, 1)
        val end = LocalDate.of(2025, 2, 1)
        `when`(priceSyncService.pricesFor("999999", start, end)).thenReturn(emptyList())

        assertThrows<NoSuchElementException> {
            service.run(listOf(BacktestHolding("999999", 100.0)), start, end, 1_000_000)
        }
    }

    @Test
    fun `rejects more than the maximum number of holdings`() {
        val holdings = (1..11).map { BacktestHolding("SYM$it", 10.0) }
        assertThrows<IllegalArgumentException> {
            service.run(holdings, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1), 1_000_000)
        }
    }
}
