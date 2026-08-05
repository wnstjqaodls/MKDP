package com.mkdp.price

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

enum class StockMarket { KOSPI, KOSDAQ }

data class StockRanking(
    val symbol: String,
    val name: String,
    val marketValue: Long,
    val tradingVolume: Long,
    val changeRate: Double,
)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class RankingResponse(val stocks: List<RankingStock> = emptyList())

@JsonIgnoreProperties(ignoreUnknown = true)
private data class RankingStock(
    val itemCode: String? = null,
    val stockName: String? = null,
    val stockEndType: String? = null,
    val marketValueRaw: String? = null,
    val accumulatedTradingVolumeRaw: String? = null,
    val fluctuationsRatio: String? = null,
)

/**
 * 네이버 증권 모바일 API의 시가총액 순위 엔드포인트를 호출한다. 홈 화면 "많이 찾는 종목"
 * 발견(discovery) 섹션에 쓴다 — 거래량 상위는 별도 정렬 엔드포인트가 없어, 시가총액 상위
 * 구간(유동성 높은 종목군)을 넉넉히 가져온 뒤 거래량으로 다시 정렬해 근사한다.
 */
@Component
class NaverStockRankingClient(
    @Qualifier("naverRankingRestClient") private val restClient: RestClient,
) {
    fun byMarketValue(market: StockMarket, page: Int, pageSize: Int): List<StockRanking> {
        val response = restClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/stocks/marketValue/{market}")
                    .queryParam("page", page)
                    .queryParam("pageSize", pageSize)
                    .build(market.name)
            }
            .retrieve()
            .body(RankingResponse::class.java) ?: return emptyList()

        return response.stocks.mapNotNull { stock ->
            // 이 엔드포인트는 코스피/코스닥에 상장된 ETF도 함께 내려준다 — ETF는 별도 카테고리
            // 랭킹(DiscoveryService.topEtfs)에서 다루므로 순수 주식만 남긴다.
            if (stock.stockEndType != null && stock.stockEndType != "stock") return@mapNotNull null
            val symbol = stock.itemCode ?: return@mapNotNull null
            val name = stock.stockName ?: return@mapNotNull null
            StockRanking(
                symbol = symbol,
                name = name,
                marketValue = stock.marketValueRaw?.toLongOrNull() ?: 0L,
                tradingVolume = stock.accumulatedTradingVolumeRaw?.toLongOrNull() ?: 0L,
                changeRate = stock.fluctuationsRatio?.toDoubleOrNull() ?: 0.0,
            )
        }
    }
}
