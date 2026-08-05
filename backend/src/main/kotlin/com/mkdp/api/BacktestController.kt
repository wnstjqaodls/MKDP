package com.mkdp.api

import com.mkdp.domain.BacktestHolding
import com.mkdp.domain.BacktestService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

data class BacktestHoldingRequest(
    @field:NotBlank
    val symbol: String,
    @field:Positive
    val weight: Double,
)

data class BacktestRequest(
    @field:NotEmpty
    @field:Valid
    val holdings: List<BacktestHoldingRequest>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    @field:Positive
    val initialAmount: Long,
)

/** 포트폴리오 백테스트 실행 — v1이 완성하지 못했던 기능을 v3에서 되살린다. */
@RestController
@RequestMapping("/api/backtest")
class BacktestController(
    private val backtestService: BacktestService,
) {
    @PostMapping
    fun run(@Valid @RequestBody request: BacktestRequest) = backtestService.run(
        holdings = request.holdings.map { BacktestHolding(symbol = it.symbol, weight = it.weight) },
        startDate = request.startDate,
        endDate = request.endDate,
        initialAmount = request.initialAmount,
    )
}
