package com.mkdp.price

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.LocalDate
import kotlin.test.assertEquals

class NaverPriceClientTest {
    private lateinit var server: MockWebServer
    private lateinit var client: NaverPriceClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        val restClient = RestClient.builder()
            .baseUrl(server.url("/").toString())
            .requestFactory(SimpleClientHttpRequestFactory())
            .build()
        client = NaverPriceClient(restClient)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses the loose JS array response into sorted daily prices`() {
        server.enqueue(
            MockResponse().setBody(
                """
                 [['날짜', '시가', '고가', '저가', '종가', '거래량', '외국인소진율'],

                ["20260106", 135300, 139300, 132700, 138900, 45321341, 52.18],
                ["20260102", 120200, 128500, 120200, 128500, 30463279, 52.37],
                ]
                """.trimIndent(),
            ).addHeader("Content-Type", "text/plain; charset=UTF-8"),
        )

        val result = client.dailyPrices("005930", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31))

        assertEquals(2, result.size)
        assertEquals(LocalDate.of(2026, 1, 2), result[0].date)
        assertEquals(128500, result[0].close)
        assertEquals(LocalDate.of(2026, 1, 6), result[1].date)
        assertEquals(138900, result[1].close)
    }

    @Test
    fun `returns an empty list when only the header row is present`() {
        server.enqueue(
            MockResponse().setBody(
                " [['날짜', '시가', '고가', '저가', '종가', '거래량', '외국인소진율'],\n]",
            ).addHeader("Content-Type", "text/plain; charset=UTF-8"),
        )

        val result = client.dailyPrices("999999", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31))

        assertEquals(0, result.size)
    }
}
