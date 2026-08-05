package com.mkdp.price

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DailyPrice(
    val date: LocalDate,
    val open: Long,
    val high: Long,
    val low: Long,
    val close: Long,
    val volume: Long,
)

/**
 * 네이버 금융 비공식 일별 시세 엔드포인트(siseJson.naver)를 호출한다.
 *
 * DART 오픈API는 일별 주가를 제공하지 않는다 — v1이 백테스트를 완성하지 못한
 * 근본 원인이었다(docs/V1-RETROSPECTIVE.md 참고). 이 클라이언트가 그 공백을 메운다.
 * 응답은 엄밀한 JSON이 아니라 작은따옴표 헤더 행 + 큰따옴표 데이터 행이 섞인
 * JS 배열 리터럴이라 정규식으로 데이터 행만 뽑아낸다.
 */
@Component
class NaverPriceClient(
    @Qualifier("naverPriceRestClient") private val restClient: RestClient,
) {
    private val rowPattern = Regex(
        """\["(\d{8})",\s*([\d.]+),\s*([\d.]+),\s*([\d.]+),\s*([\d.]+),\s*(\d+)""",
    )
    private val dateFormatter = DateTimeFormatter.BASIC_ISO_DATE

    fun dailyPrices(symbol: String, startDate: LocalDate, endDate: LocalDate): List<DailyPrice> {
        val body = restClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/siseJson.naver")
                    .queryParam("symbol", symbol)
                    .queryParam("requestType", 1)
                    .queryParam("startTime", startDate.format(dateFormatter))
                    .queryParam("endTime", endDate.format(dateFormatter))
                    .queryParam("timeframe", "day")
                    .build()
            }
            .retrieve()
            .body(String::class.java) ?: return emptyList()

        return rowPattern.findAll(body).map { match ->
            val (date, open, high, low, close, volume) = match.destructured
            DailyPrice(
                date = LocalDate.parse(date, dateFormatter),
                open = open.toDouble().toLong(),
                high = high.toDouble().toLong(),
                low = low.toDouble().toLong(),
                close = close.toDouble().toLong(),
                volume = volume.toLong(),
            )
        }.sortedBy { it.date }.toList()
    }
}
