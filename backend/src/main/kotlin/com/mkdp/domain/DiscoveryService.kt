package com.mkdp.domain

import com.mkdp.db.EtfRepository
import com.mkdp.price.NaverStockRankingClient
import com.mkdp.price.StockMarket
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

data class DiscoveryItem(
    val symbol: String,
    val name: String,
    val type: AssetType,
    val marketValue: Long?,
    val tradingVolume: Long?,
    val changeRatePct: Double?,
)

data class EtfCategory(val tabCode: Int, val label: String)

/**
 * 홈 화면 발견(discovery) 데이터 — 시가총액/거래량 상위, ETF 카테고리별 상위.
 * 처음 방문한 사용자는 무엇이 있는지 모르니, 검색창만 던져주지 않고 인기 자산을
 * 먼저 보여준다.
 */
@Service
class DiscoveryService(
    private val stockRankingClient: NaverStockRankingClient,
    private val etfs: EtfRepository,
) {
    @Cacheable(cacheNames = ["discoveryRankings"], key = "{'stocks', #market, #sort}")
    fun topStocks(market: StockMarket, sort: String, limit: Int): List<DiscoveryItem> {
        // 거래량 정렬 API가 없어, 유동성 높은 상위 구간을 넉넉히 가져온 뒤 거래량으로 재정렬해 근사한다.
        val fetchSize = if (sort == "volume") (limit * 8).coerceAtMost(150) else limit
        val ranked = stockRankingClient.byMarketValue(market, page = 1, pageSize = fetchSize)
        val sorted = if (sort == "volume") ranked.sortedByDescending { it.tradingVolume } else ranked
        return sorted.take(limit).map {
            DiscoveryItem(
                symbol = it.symbol,
                name = it.name,
                type = AssetType.STOCK,
                marketValue = it.marketValue,
                tradingVolume = it.tradingVolume,
                changeRatePct = it.changeRate,
            )
        }
    }

    @Cacheable(cacheNames = ["discoveryRankings"], key = "{'etf', #tabCode, #sort}")
    fun topEtfs(tabCode: Int, sort: String, limit: Int): List<DiscoveryItem> =
        etfs.topByCategory(tabCode, sort, limit).map {
            DiscoveryItem(
                symbol = it.symbol,
                name = it.name,
                type = AssetType.ETF,
                marketValue = it.marketSum,
                tradingVolume = it.quant,
                changeRatePct = null,
            )
        }

    fun etfCategories(): List<EtfCategory> = listOf(
        EtfCategory(1, "국내 지수"),
        EtfCategory(2, "국내 업종/테마"),
        EtfCategory(3, "국내 파생(레버리지/인버스)"),
        EtfCategory(4, "해외 주식"),
        EtfCategory(5, "원자재"),
        EtfCategory(6, "채권"),
        EtfCategory(7, "기타"),
    )
}
