package com.mkdp.price

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.nio.charset.Charset
import kotlin.test.assertEquals

class NaverEtfListClientTest {
    private lateinit var server: MockWebServer
    private lateinit var client: NaverEtfListClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        val restClient = RestClient.builder()
            .baseUrl(server.url("/").toString())
            .requestFactory(SimpleClientHttpRequestFactory())
            .build()
        client = NaverEtfListClient(restClient)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `decodes the EUC-KR response into etf summaries`() {
        val json = """
            {"resultCode":"success","result":{"etfItemList":[
                {"itemcode":"069500","itemname":"KODEX 200","nav":104324.0},
                {"itemcode":"360750","itemname":"TIGER 미국S&P500","nav":27320.0}
            ]}}
        """.trimIndent()
        val eucKrBytes = json.toByteArray(Charset.forName("EUC-KR"))
        server.enqueue(
            MockResponse()
                .setBody(Buffer().write(eucKrBytes))
                .addHeader("Content-Type", "text/plain; charset=EUC-KR"),
        )

        val result = client.list()

        assertEquals(2, result.size)
        assertEquals("069500", result[0].symbol)
        assertEquals("KODEX 200", result[0].name)
        assertEquals("360750", result[1].symbol)
        assertEquals("TIGER 미국S&P500", result[1].name)
    }
}
