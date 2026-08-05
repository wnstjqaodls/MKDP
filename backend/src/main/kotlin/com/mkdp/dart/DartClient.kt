package com.mkdp.dart

import com.mkdp.config.DartProperties
import com.mkdp.dart.dto.DartCompanyOverviewResponse
import com.mkdp.dart.dto.DartDisclosureListResponse
import com.mkdp.dart.dto.DartFinancialResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

/**
 * DART Open API를 호출하는 유일한 지점.
 *
 * v1(레거시)은 이 호출을 4개 컨트롤러 메서드에 각각 흩어 놓고, 응답 status 코드를 확인하지 않은 채
 * raw 바디를 그대로 반환했다. v2는 호출을 한 곳으로 모으고 status를 반드시 검사한다.
 */
@Component
class DartClient(
    @Qualifier("dartRestClient") private val restClient: RestClient,
    private val properties: DartProperties,
) {
    fun companyOverview(corpCode: String): DartCompanyOverviewResponse {
        val response = restClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/company.json")
                    .queryParam("crtfc_key", properties.apiKey)
                    .queryParam("corp_code", corpCode)
                    .build()
            }
            .retrieve()
            .body(DartCompanyOverviewResponse::class.java)
            ?: throw DartApiException.Unknown("", "DART 응답이 비어있습니다.")
        checkStatus(response.status, response.message)
        return response
    }

    fun disclosureList(
        corpCode: String,
        bgnDe: String?,
        endDe: String?,
        pageNo: Int,
        pageCount: Int,
    ): DartDisclosureListResponse {
        val response = restClient.get()
            .uri { uriBuilder ->
                var builder = uriBuilder.path("/list.json")
                    .queryParam("crtfc_key", properties.apiKey)
                    .queryParam("corp_code", corpCode)
                    .queryParam("page_no", pageNo)
                    .queryParam("page_count", pageCount)
                    .queryParam("sort", "date")
                    .queryParam("sort_mth", "desc")
                if (!bgnDe.isNullOrBlank()) builder = builder.queryParam("bgn_de", bgnDe)
                if (!endDe.isNullOrBlank()) builder = builder.queryParam("end_de", endDe)
                builder.build()
            }
            .retrieve()
            .body(DartDisclosureListResponse::class.java)
            ?: throw DartApiException.Unknown("", "DART 응답이 비어있습니다.")
        // 013(해당 데이터 없음)은 "공시가 없는 기업"이라는 정상 상태이므로 예외로 취급하지 않는다.
        checkStatus(response.status, response.message, allowNoData = true)
        return response
    }

    fun financials(corpCode: String, businessYear: Int, reprtCode: String): DartFinancialResponse {
        val response = restClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/fnlttSinglAcnt.json")
                    .queryParam("crtfc_key", properties.apiKey)
                    .queryParam("corp_code", corpCode)
                    .queryParam("bsns_year", businessYear)
                    .queryParam("reprt_code", reprtCode)
                    .build()
            }
            .retrieve()
            .body(DartFinancialResponse::class.java)
            ?: throw DartApiException.Unknown("", "DART 응답이 비어있습니다.")
        checkStatus(response.status, response.message, allowNoData = true)
        return response
    }

    /** corpCode.xml — 전체 상장/비상장 기업 고유번호를 담은 ZIP(XML) 원문. */
    fun downloadCorpCodeZip(): ByteArray =
        restClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/corpCode.xml")
                    .queryParam("crtfc_key", properties.apiKey)
                    .build()
            }
            .retrieve()
            .body(ByteArray::class.java)
            ?: throw DartApiException.Unknown("", "DART corpCode.xml 응답이 비어있습니다.")

    private fun checkStatus(status: String, message: String, allowNoData: Boolean = false) {
        when (status) {
            "000" -> return
            "013" -> if (!allowNoData) throw DartApiException.NoData(message)
            "020" -> throw DartApiException.RateLimited(message)
            "010", "011", "012" -> throw DartApiException.InvalidKey(message)
            else -> throw DartApiException.Unknown(status, message)
        }
    }
}
