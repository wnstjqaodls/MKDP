package com.mkdp.api

import com.mkdp.domain.AssetSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/** 개별 주식과 ETF를 통합 검색한다 — 포트폴리오 백테스트 장바구니에 담을 자산을 찾는 용도. */
@RestController
@RequestMapping("/api/assets")
class AssetController(
    private val assetSearchService: AssetSearchService,
) {
    @GetMapping
    fun search(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ) = assetSearchService.search(q, page.coerceAtLeast(0), size.coerceIn(1, 100))
}
