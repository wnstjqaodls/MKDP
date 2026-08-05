package com.mkdp.domain

import com.mkdp.db.PriceHistoryRepository
import com.mkdp.db.PriceRow
import com.mkdp.price.NaverPriceClient
import org.springframework.stereotype.Service
import java.time.LocalDate

/** 심볼별 일별 시세를 온디맨드로 캐시한다. 백테스트 요청이 들어올 때마다 매번 네이버를 때리지 않기 위함이다. */
@Service
class PriceSyncService(
    private val naverPriceClient: NaverPriceClient,
    private val priceHistory: PriceHistoryRepository,
) {
    fun pricesFor(symbol: String, start: LocalDate, end: LocalDate): List<PriceRow> {
        if (!priceHistory.coversRange(symbol, start, end)) {
            val fetched = naverPriceClient.dailyPrices(symbol, start, end)
            priceHistory.upsertBatch(symbol, fetched)
        }
        return priceHistory.findRange(symbol, start, end)
    }
}
