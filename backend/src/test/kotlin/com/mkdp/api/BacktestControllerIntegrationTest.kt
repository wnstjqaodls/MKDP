package com.mkdp.api

import com.mkdp.price.NaverPriceClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:mkdp-backtest-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
    ],
)
@AutoConfigureMockMvc
class BacktestControllerIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var jdbc: JdbcTemplate

    @MockBean
    lateinit var naverPriceClient: NaverPriceClient

    @BeforeEach
    fun seedPriceHistory() {
        jdbc.update("DELETE FROM price_history")
        val rows = listOf(
            Triple("005930", "2025-01-02", 10000L),
            Triple("005930", "2025-06-01", 8000L),
            Triple("005930", "2026-01-01", 12000L),
        )
        rows.forEach { (symbol, date, close) ->
            jdbc.update(
                "INSERT INTO price_history (symbol, date, open, high, low, close, volume) VALUES (?, ?::date, ?, ?, ?, ?, ?)",
                symbol,
                date,
                close,
                close,
                close,
                close,
                1000L,
            )
        }
    }

    @Test
    fun `runs a buy-and-hold backtest for a single seeded holding`() {
        mockMvc.post("/api/backtest") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "holdings": [{"symbol": "005930", "weight": 100}],
                  "startDate": "2025-01-01",
                  "endDate": "2026-01-01",
                  "initialAmount": 1000000
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.finalValue") { value(1200000) }
            jsonPath("$.totalReturnPct") { value(20.0) }
            jsonPath("$.mddPct") { value(-20.0) }
            jsonPath("$.series.length()") { value(3) }
        }
    }

    @Test
    fun `rejects an empty holdings list`() {
        mockMvc.post("/api/backtest") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"holdings": [], "startDate": "2025-01-01", "endDate": "2026-01-01", "initialAmount": 1000000}"""
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `returns 404 when a holding has no cached price data`() {
        mockMvc.post("/api/backtest") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"holdings": [{"symbol": "999999", "weight": 100}], "startDate": "2025-01-01", "endDate": "2026-01-01", "initialAmount": 1000000}"""
        }.andExpect { status { isNotFound() } }
    }
}
