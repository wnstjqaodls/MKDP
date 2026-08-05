package com.mkdp.domain

import com.mkdp.dart.DartClient
import com.mkdp.db.CompanyRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

data class CompanySummary(val corpCode: String, val corpName: String, val stockCode: String?)

data class CompanyOverview(
    val corpCode: String,
    val corpName: String,
    val corpNameEng: String?,
    val stockCode: String?,
    val ceoName: String?,
    val corpClass: String?,
    val address: String?,
    val homepageUrl: String?,
    val phoneNumber: String?,
    val industryCode: String?,
    val establishedDate: String?,
    val settlementMonth: String?,
)

@Service
class CompanyService(
    private val companies: CompanyRepository,
    private val dartClient: DartClient,
) {
    fun search(query: String, listedOnly: Boolean, page: Int, size: Int): List<CompanySummary> {
        require(query.isNotBlank()) { "검색어를 입력해주세요." }
        return companies.search(query.trim(), listedOnly, page, size)
            .map { CompanySummary(it.corpCode, it.corpName, it.stockCode) }
    }

    @Cacheable(cacheNames = ["companyOverview"], key = "#corpCode")
    fun overview(corpCode: String): CompanyOverview {
        val local = companies.findByCorpCode(corpCode)
            ?: throw NoSuchElementException("등록되지 않은 기업 고유번호입니다: $corpCode")
        val dart = dartClient.companyOverview(corpCode)
        return CompanyOverview(
            corpCode = local.corpCode,
            corpName = dart.corpName ?: local.corpName,
            corpNameEng = dart.corpNameEng,
            stockCode = local.stockCode,
            ceoName = dart.ceoName,
            corpClass = dart.corpClass,
            address = dart.address,
            homepageUrl = dart.homepageUrl,
            phoneNumber = dart.phoneNumber,
            industryCode = dart.industryCode,
            establishedDate = dart.establishedDate,
            settlementMonth = dart.settlementMonth,
        )
    }
}
