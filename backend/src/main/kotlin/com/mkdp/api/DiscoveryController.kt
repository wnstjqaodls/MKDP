package com.mkdp.api

import com.mkdp.domain.DiscoveryService
import com.mkdp.price.StockMarket
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/** 홈 화면 발견(discovery) 데이터 — 시가총액/거래량 상위 종목, ETF 카테고리별 상위. */
@RestController
@RequestMapping("/api/discovery")
class DiscoveryController(
    private val discoveryService: DiscoveryService,
) {
    @GetMapping("/stocks")
    fun stocks(
        @RequestParam(defaultValue = "KOSPI") market: StockMarket,
        @RequestParam(defaultValue = "marketcap") sort: String,
        @RequestParam(defaultValue = "20") limit: Int,
    ) = discoveryService.topStocks(market, sort, limit.coerceIn(1, 50))

    @GetMapping("/etf-categories")
    fun etfCategories() = discoveryService.etfCategories()

    @GetMapping("/etfs")
    fun etfs(
        @RequestParam tabCode: Int,
        @RequestParam(defaultValue = "marketcap") sort: String,
        @RequestParam(defaultValue = "20") limit: Int,
    ) = discoveryService.topEtfs(tabCode, sort, limit.coerceIn(1, 50))
}
