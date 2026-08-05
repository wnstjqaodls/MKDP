package com.mkdp.domain

import com.mkdp.dart.DartClient
import com.mkdp.db.CompanyRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

data class DisclosureItem(
    val receiptNo: String,
    val reportName: String,
    val filerName: String?,
    val receiptDate: String,
    val remark: String?,
    val originalDocumentUrl: String,
)

data class DisclosurePage(
    val items: List<DisclosureItem>,
    val page: Int,
    val size: Int,
    val totalCount: Int,
    val totalPage: Int,
)

@Service
class DisclosureService(
    private val companies: CompanyRepository,
    private val dartClient: DartClient,
) {
    @Cacheable(cacheNames = ["disclosures"], key = "{#corpCode, #bgnDe, #endDe, #page, #size}")
    fun list(corpCode: String, bgnDe: String?, endDe: String?, page: Int, size: Int): DisclosurePage {
        companies.findByCorpCode(corpCode)
            ?: throw NoSuchElementException("등록되지 않은 기업 고유번호입니다: $corpCode")
        // DART 페이지 번호는 1부터 시작하지만 API는 0-base로 노출한다.
        val response = dartClient.disclosureList(corpCode, bgnDe, endDe, page + 1, size)
        val items = (response.list ?: emptyList()).map {
            DisclosureItem(
                receiptNo = it.receiptNo,
                reportName = it.reportName,
                filerName = it.filerName,
                receiptDate = it.receiptDate,
                remark = it.rm,
                originalDocumentUrl = "https://dart.fss.or.kr/dsaf001/main.do?rcpNo=${it.receiptNo}",
            )
        }
        return DisclosurePage(
            items = items,
            page = page,
            size = size,
            totalCount = response.totalCount ?: items.size,
            totalPage = response.totalPage ?: if (items.isEmpty()) 0 else 1,
        )
    }
}
