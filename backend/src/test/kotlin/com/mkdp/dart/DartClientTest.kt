package com.mkdp.dart

import com.mkdp.config.DartProperties
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals

class DartClientTest {
    private lateinit var server: MockWebServer
    private lateinit var client: DartClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        val properties = DartProperties().apply {
            apiKey = "test-key"
            baseUrl = server.url("/api").toString()
        }
        val restClient = RestClient.builder()
            .baseUrl(properties.baseUrl)
            .requestFactory(SimpleClientHttpRequestFactory())
            .build()
        client = DartClient(restClient, properties)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses a normal company overview response`() {
        server.enqueue(
            MockResponse().setBody(
                """{"status":"000","message":"정상","corp_code":"00126380","corp_name":"삼성전자"}""",
            ).addHeader("Content-Type", "application/json"),
        )

        val result = client.companyOverview("00126380")

        assertEquals("삼성전자", result.corpName)
    }

    @Test
    fun `throws InvalidKey when DART reports an authentication error`() {
        server.enqueue(
            MockResponse().setBody(
                """{"status":"011","message":"등록되지 않은 키입니다."}""",
            ).addHeader("Content-Type", "application/json"),
        )

        assertThrows<DartApiException.InvalidKey> { client.companyOverview("00126380") }
    }

    @Test
    fun `throws RateLimited when DART reports quota exceeded`() {
        server.enqueue(
            MockResponse().setBody(
                """{"status":"020","message":"요청 제한을 초과하였습니다."}""",
            ).addHeader("Content-Type", "application/json"),
        )

        assertThrows<DartApiException.RateLimited> { client.companyOverview("00126380") }
    }

    @Test
    fun `disclosure list treats no-data status as an empty result rather than an error`() {
        server.enqueue(
            MockResponse().setBody(
                """{"status":"013","message":"조회된 데이터가 없습니다."}""",
            ).addHeader("Content-Type", "application/json"),
        )

        val result = client.disclosureList("00126380", null, null, 1, 20)

        assertEquals("013", result.status)
        assertEquals(null, result.list)
    }
}
