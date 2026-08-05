package com.mkdp.domain

import com.mkdp.dart.DartClient
import com.mkdp.dart.dto.DartFinancialItem
import com.mkdp.db.CompanyRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

data class FinancialTrend(
    val currentPeriod: String?,
    val currentAmount: Long?,
    val priorPeriod: String?,
    val priorAmount: Long?,
    val twoPriorPeriod: String?,
    val twoPriorAmount: Long?,
    val unit: String,
)

data class FinancialSummary(
    val corpCode: String,
    val year: Int,
    val reprtCode: String,
    val revenue: FinancialTrend?,
    val operatingProfit: FinancialTrend?,
    val netIncome: FinancialTrend?,
)

@Service
class FinancialService(
    private val companies: CompanyRepository,
    private val dartClient: DartClient,
) {
    @Cacheable(cacheNames = ["financials"], key = "{#corpCode, #year, #reprtCode}")
    fun keyAccounts(corpCode: String, year: Int, reprtCode: String): FinancialSummary {
        companies.findByCorpCode(corpCode)
            ?: throw NoSuchElementException("등록되지 않은 기업 고유번호입니다: $corpCode")
        val items = dartClient.financials(corpCode, year, reprtCode).list.orEmpty()

        fun pick(accountName: String): DartFinancialItem? =
            items.firstOrNull { it.accountName == accountName && it.fsDivision == "CFS" }
                ?: items.firstOrNull { it.accountName == accountName }

        return FinancialSummary(
            corpCode = corpCode,
            year = year,
            reprtCode = reprtCode,
            revenue = pick("매출액")?.toTrend(),
            operatingProfit = pick("영업이익")?.toTrend(),
            netIncome = pick("당기순이익")?.toTrend(),
        )
    }

    private fun DartFinancialItem.toTrend() = FinancialTrend(
        currentPeriod = currentPeriodName,
        currentAmount = currentAmount?.toAmountOrNull(),
        priorPeriod = priorPeriodName,
        priorAmount = priorAmount?.toAmountOrNull(),
        twoPriorPeriod = twoPriorPeriodName,
        twoPriorAmount = twoPriorAmount?.toAmountOrNull(),
        unit = currency ?: "KRW",
    )

    /** DART 금액 필드는 "1,234,567" 형태의 문자열이다. */
    private fun String.toAmountOrNull(): Long? = replace(",", "").toLongOrNull()
}
