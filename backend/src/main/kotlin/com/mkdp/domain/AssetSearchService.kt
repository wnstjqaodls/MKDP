package com.mkdp.domain

import com.mkdp.db.CompanyRepository
import com.mkdp.db.EtfRepository
import org.springframework.stereotype.Service

enum class AssetType { STOCK, ETF }

data class AssetSummary(
    val symbol: String,
    val name: String,
    val type: AssetType,
)

/**
 * 종목(개별 주식)과 ETF를 통합 검색한다. 개별 주식은 DART corpCode 동기화 결과인
 * company 테이블에서, ETF는 별도 동기화한 etf 테이블에서 가져와 하나의 결과로 합친다.
 * 백테스트 장바구니에 담을 수 있는 건 상장 주식(stock_code 보유)과 ETF뿐이다.
 */
@Service
class AssetSearchService(
    private val companies: CompanyRepository,
    private val etfs: EtfRepository,
) {
    fun search(query: String, page: Int, size: Int): List<AssetSummary> {
        require(query.isNotBlank()) { "검색어를 입력해주세요." }
        val trimmed = query.trim()
        val stocks = companies.search(trimmed, listedOnly = true, page = page, size = size)
            .mapNotNull { row ->
                val stockCode = row.stockCode?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                AssetSummary(symbol = stockCode, name = row.corpName, type = AssetType.STOCK)
            }
        val etfResults = etfs.search(trimmed, page = page, size = size)
            .map { AssetSummary(symbol = it.symbol, name = it.name, type = AssetType.ETF) }
        return (stocks + etfResults).sortedBy { it.name }.take(size)
    }
}
