package com.mkdp.api

import com.mkdp.domain.PortfolioPresetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** 검색 없이 바로 고를 수 있는 큐레이션 포트폴리오 목록. */
@RestController
@RequestMapping("/api/portfolio-presets")
class PortfolioPresetController(
    private val portfolioPresetService: PortfolioPresetService,
) {
    @GetMapping
    fun list() = portfolioPresetService.list()
}
