package com.mkdp.domain

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.pow

data class BacktestHolding(val symbol: String, val weight: Double)

data class BacktestPoint(val date: LocalDate, val value: Long)

data class BacktestResult(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val initialAmount: Long,
    val finalValue: Long,
    val totalReturnPct: Double,
    val cagrPct: Double,
    val mddPct: Double,
    val series: List<BacktestPoint>,
)

/**
 * 매수 후 보유(buy & hold) 포트폴리오 백테스트 — v1이 완성하지 못했던 바로 그 기능.
 *
 * v1은 이 계산에 필요한 일별 주가 데이터 소스가 없어 `runBacktest()`의 실제 구현부가
 * 전부 주석 처리된 채로 남아 있었다(docs/V1-RETROSPECTIVE.md 참고). v3는 [PriceSyncService]가
 * 공급하는 네이버 금융 일별 시세로 이 공백을 메운다.
 *
 * 리밸런싱 없이 시작일에 목표 비중대로 1회 매수한 뒤 그대로 보유한다고 가정한다 —
 * 주기적 리밸런싱은 이후 과제로 남긴다.
 */
@Service
class BacktestService(
    private val priceSyncService: PriceSyncService,
) {
    fun run(holdings: List<BacktestHolding>, startDate: LocalDate, endDate: LocalDate, initialAmount: Long): BacktestResult {
        require(holdings.isNotEmpty()) { "종목을 1개 이상 담아주세요." }
        require(holdings.size <= MAX_HOLDINGS) { "종목은 최대 ${MAX_HOLDINGS}개까지 담을 수 있습니다." }
        require(startDate.isBefore(endDate)) { "시작일은 종료일보다 이전이어야 합니다." }
        require(initialAmount > 0) { "초기 투자금은 0보다 커야 합니다." }
        val weightSum = holdings.sumOf { it.weight }
        require(weightSum > 0) { "비중의 합은 0보다 커야 합니다." }

        val priceSeries = holdings.associate { holding ->
            val prices = priceSyncService.pricesFor(holding.symbol, startDate, endDate)
            if (prices.isEmpty()) {
                throw NoSuchElementException("${holding.symbol} 종목의 해당 기간 시세를 찾을 수 없습니다.")
            }
            holding.symbol to prices.associate { it.date to it.close }
        }

        val commonDates = priceSeries.values
            .map { it.keys }
            .reduce { acc, dates -> acc.intersect(dates) }
            .sorted()
        if (commonDates.isEmpty()) {
            throw NoSuchElementException("담은 종목들의 공통 거래일이 없습니다.")
        }

        val firstDate = commonDates.first()
        val units = holdings.associate { holding ->
            val weight = holding.weight / weightSum
            val startPrice = priceSeries.getValue(holding.symbol).getValue(firstDate)
            holding.symbol to (initialAmount * weight) / startPrice
        }

        val series = commonDates.map { date ->
            val value = holdings.sumOf { holding ->
                units.getValue(holding.symbol) * priceSeries.getValue(holding.symbol).getValue(date)
            }
            BacktestPoint(date = date, value = value.toLong())
        }

        val finalValue = series.last().value
        val totalReturnPct = ((finalValue.toDouble() / initialAmount - 1) * 100).round2()
        val years = ChronoUnit.DAYS.between(firstDate, commonDates.last()) / 365.25
        val cagrPct = if (years > 0) {
            (((finalValue.toDouble() / initialAmount).pow(1 / years) - 1) * 100).round2()
        } else {
            0.0
        }
        val mddPct = maxDrawdownPct(series).round2()

        return BacktestResult(
            startDate = firstDate,
            endDate = commonDates.last(),
            initialAmount = initialAmount,
            finalValue = finalValue,
            totalReturnPct = totalReturnPct,
            cagrPct = cagrPct,
            mddPct = mddPct,
            series = series,
        )
    }

    private fun maxDrawdownPct(series: List<BacktestPoint>): Double {
        var peak = series.first().value.toDouble()
        var worst = 0.0
        for (point in series) {
            val value = point.value.toDouble()
            if (value > peak) peak = value
            val drawdown = (value - peak) / peak * 100
            if (drawdown < worst) worst = drawdown
        }
        return worst
    }

    private fun Double.round2(): Double = Math.round(this * 100) / 100.0

    companion object {
        private const val MAX_HOLDINGS = 10
    }
}
