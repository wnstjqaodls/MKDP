package com.mkdp.price

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals

class NaverStockRankingClientTest {
    private lateinit var server: MockWebServer
    private lateinit var client: NaverStockRankingClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        val restClient = RestClient.builder()
            .baseUrl(server.url("/").toString())
            .requestFactory(SimpleClientHttpRequestFactory())
            .build()
        client = NaverStockRankingClient(restClient)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses market-cap ranking response ignoring unmodeled fields`() {
        server.enqueue(
            MockResponse().setBody(
                """
                {"stockListSortType":"MARKET_VALUE","stockListCategoryType":"KOSPI","stocks":[
                  {"itemCode":"005930","stockName":"삼성전자","marketValueRaw":"1438184537568000","accumulatedTradingVolumeRaw":"22444447","fluctuationsRatio":"2.50","unused":"ignored"},
                  {"itemCode":"000660","stockName":"SK하이닉스","marketValueRaw":"1218461264820000","accumulatedTradingVolumeRaw":"3740705","fluctuationsRatio":"5.77"}
                ],"totalCount":2475,"page":1,"pageSize":2}
                """.trimIndent(),
            ).addHeader("Content-Type", "application/json"),
        )

        val result = client.byMarketValue(StockMarket.KOSPI, page = 1, pageSize = 2)

        assertEquals(2, result.size)
        assertEquals("005930", result[0].symbol)
        assertEquals("삼성전자", result[0].name)
        assertEquals(1_438_184_537_568_000L, result[0].marketValue)
        assertEquals(22_444_447L, result[0].tradingVolume)
        assertEquals(2.50, result[0].changeRate)
    }
}
