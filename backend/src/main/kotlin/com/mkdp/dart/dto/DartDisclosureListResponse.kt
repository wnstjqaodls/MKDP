package com.mkdp.dart.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/** DART 공시검색(list.json) 응답. */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DartDisclosureListResponse(
    val status: String,
    val message: String,
    @JsonProperty("page_no") val pageNo: Int? = null,
    @JsonProperty("page_count") val pageCount: Int? = null,
    @JsonProperty("total_count") val totalCount: Int? = null,
    @JsonProperty("total_page") val totalPage: Int? = null,
    val list: List<DartDisclosureItem>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DartDisclosureItem(
    @JsonProperty("corp_cls") val corpClass: String? = null,
    @JsonProperty("corp_name") val corpName: String? = null,
    @JsonProperty("corp_code") val corpCode: String? = null,
    @JsonProperty("stock_code") val stockCode: String? = null,
    @JsonProperty("report_nm") val reportName: String = "",
    @JsonProperty("rcept_no") val receiptNo: String = "",
    @JsonProperty("flr_nm") val filerName: String? = null,
    @JsonProperty("rcept_dt") val receiptDate: String = "",
    val rm: String? = null,
)
