package com.mkdp.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:mkdp-asset-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
    ],
)
@AutoConfigureMockMvc
class AssetControllerIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var jdbc: JdbcTemplate

    @BeforeEach
    fun seed() {
        jdbc.update("DELETE FROM company")
        jdbc.update("DELETE FROM etf")
        jdbc.update(
            "INSERT INTO company (corp_code, corp_name, stock_code, modify_date, updated_at) VALUES (?, ?, ?, ?, ?)",
            "00126380",
            "삼성전자",
            "005930",
            "20260101",
            LocalDateTime.now(),
        )
        jdbc.update(
            "INSERT INTO etf (symbol, name, nav, updated_at) VALUES (?, ?, ?, ?)",
            "069500",
            "삼성KODEX200",
            30000.0,
            LocalDateTime.now(),
        )
    }

    @Test
    fun `merges stock and ETF results tagged with their asset type`() {
        mockMvc.get("/api/assets") { param("q", "삼성") }
            .andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(2) }
                jsonPath("$[?(@.symbol == '005930')].type") { value("STOCK") }
                jsonPath("$[?(@.symbol == '069500')].type") { value("ETF") }
            }
    }
}
