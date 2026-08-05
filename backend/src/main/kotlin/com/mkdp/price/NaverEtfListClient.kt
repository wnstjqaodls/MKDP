package com.mkdp.price

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.nio.charset.Charset

data class EtfSummary(
    val symbol: String,
    val name: String,
    val nav: Double?,
)

/**
 * 네이버 금융의 비공식 ETF 목록 엔드포인트(etfItemList.nhn)를 호출한다.
 *
 * DART `corpCode.xml`은 ETF를 포함하지 않는다 — ETF는 신탁 구조라 DART 공시
 * 대상 법인(회사)이 아니기 때문이다. 그래서 기업 마스터와 별도로 ETF 유니버스를
 * 이 엔드포인트에서 동기화한다. 응답 본문은 EUC-KR로 인코딩돼 있어 직접 디코딩한다.
 */
@Component
class NaverEtfListClient(
    @Qualifier("naverEtfListRestClient") private val restClient: RestClient,
) {
    private val objectMapper = ObjectMapper()
    private val eucKr = Charset.forName("EUC-KR")

    fun list(): List<EtfSummary> {
        val bytes = restClient.get()
            .uri("/api/sise/etfItemList.nhn")
            .retrieve()
            .body(ByteArray::class.java) ?: return emptyList()
        val body = String(bytes, eucKr)

        val root = objectMapper.readTree(body)
        val items = root.path("result").path("etfItemList")
        if (!items.isArray) return emptyList()

        return items.mapNotNull { node ->
            val symbol = node.path("itemcode").asText(null) ?: return@mapNotNull null
            val name = node.path("itemname").asText(null) ?: return@mapNotNull null
            val nav = node.path("nav").let { if (it.isMissingNode || it.isNull) null else it.asDouble() }
            EtfSummary(symbol = symbol, name = name, nav = nav)
        }
    }
}
