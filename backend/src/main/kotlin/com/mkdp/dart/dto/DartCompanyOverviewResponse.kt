package com.mkdp.dart.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/** DART 기업개황(company.json) 응답. */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DartCompanyOverviewResponse(
    val status: String,
    val message: String,
    @JsonProperty("corp_code") val corpCode: String? = null,
    @JsonProperty("corp_name") val corpName: String? = null,
    @JsonProperty("corp_name_eng") val corpNameEng: String? = null,
    @JsonProperty("stock_name") val stockName: String? = null,
    @JsonProperty("stock_code") val stockCode: String? = null,
    @JsonProperty("ceo_nm") val ceoName: String? = null,
    @JsonProperty("corp_cls") val corpClass: String? = null,
    @JsonProperty("jurir_no") val corporateRegistrationNumber: String? = null,
    @JsonProperty("bizr_no") val businessRegistrationNumber: String? = null,
    @JsonProperty("adres") val address: String? = null,
    @JsonProperty("hm_url") val homepageUrl: String? = null,
    @JsonProperty("ir_url") val irUrl: String? = null,
    @JsonProperty("phn_no") val phoneNumber: String? = null,
    @JsonProperty("fax_no") val faxNumber: String? = null,
    @JsonProperty("induty_code") val industryCode: String? = null,
    @JsonProperty("est_dt") val establishedDate: String? = null,
    @JsonProperty("acc_mt") val settlementMonth: String? = null,
)
