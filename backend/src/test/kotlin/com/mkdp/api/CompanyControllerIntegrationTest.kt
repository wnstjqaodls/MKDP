package com.mkdp.api

import com.mkdp.dart.DartClient
import com.mkdp.dart.dto.DartCompanyOverviewResponse
import com.mkdp.dart.dto.DartDisclosureItem
import com.mkdp.dart.dto.DartDisclosureListResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:mkdp-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "mkdp.sync-token=test-token",
    ],
)
@AutoConfigureMockMvc
class CompanyControllerIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var jdbc: JdbcTemplate

    @MockBean
    lateinit var dartClient: DartClient

    @BeforeEach
    fun seedCompany() {
        jdbc.update("DELETE FROM company")
        jdbc.update(
            "INSERT INTO company (corp_code, corp_name, stock_code, modify_date, updated_at) VALUES (?, ?, ?, ?, ?)",
            "00126380",
            "삼성전자",
            "005930",
            "20260101",
            LocalDateTime.now(),
        )
    }

    @Test
    fun `search finds a seeded company by name`() {
        mockMvc.get("/api/companies") { param("q", "삼성") }
            .andExpect {
                status { isOk() }
                jsonPath("$[0].corpCode") { value("00126380") }
            }
    }

    @Test
    fun `overview rejects a malformed corp code before calling DART`() {
        mockMvc.get("/api/companies/not-a-code")
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun `overview returns 404 for a corp code that was never synced`() {
        mockMvc.get("/api/companies/99999999")
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun `overview merges DART data with the locally synced company row`() {
        `when`(dartClient.companyOverview("00126380")).thenReturn(
            DartCompanyOverviewResponse(
                status = "000",
                message = "정상",
                corpCode = "00126380",
                corpName = "삼성전자",
                ceoName = "한종희",
            ),
        )

        mockMvc.get("/api/companies/00126380")
            .andExpect {
                status { isOk() }
                jsonPath("$.ceoName") { value("한종희") }
                jsonPath("$.stockCode") { value("005930") }
            }
    }

    @Test
    fun `disclosures maps DART receipt numbers to the public document url`() {
        `when`(dartClient.disclosureList("00126380", null, null, 1, 20)).thenReturn(
            DartDisclosureListResponse(
                status = "000",
                message = "정상",
                totalCount = 1,
                totalPage = 1,
                list = listOf(
                    DartDisclosureItem(
                        corpCode = "00126380",
                        reportName = "분기보고서",
                        receiptNo = "20260101000123",
                        filerName = "삼성전자",
                        receiptDate = "20260101",
                    ),
                ),
            ),
        )

        mockMvc.get("/api/companies/00126380/disclosures")
            .andExpect {
                status { isOk() }
                jsonPath("$.items[0].originalDocumentUrl") {
                    value("https://dart.fss.or.kr/dsaf001/main.do?rcpNo=20260101000123")
                }
            }
    }

    @Test
    fun `admin sync endpoint rejects a missing or wrong token`() {
        mockMvc.post("/api/admin/corp-codes/sync")
            .andExpect { status { isUnauthorized() } }
    }
}
