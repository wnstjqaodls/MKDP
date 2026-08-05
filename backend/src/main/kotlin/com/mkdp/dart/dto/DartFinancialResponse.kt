package com.mkdp.dart.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/** DART 단일회사 주요계정(fnlttSinglAcnt.json) 응답. */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DartFinancialResponse(
    val status: String,
    val message: String,
    val list: List<DartFinancialItem>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DartFinancialItem(
    @JsonProperty("account_nm") val accountName: String = "",
    @JsonProperty("fs_div") val fsDivision: String? = null,
    @JsonProperty("fs_nm") val fsName: String? = null,
    @JsonProperty("sj_div") val sjDivision: String? = null,
    @JsonProperty("sj_nm") val sjName: String? = null,
    @JsonProperty("thstrm_nm") val currentPeriodName: String? = null,
    @JsonProperty("thstrm_amount") val currentAmount: String? = null,
    @JsonProperty("frmtrm_nm") val priorPeriodName: String? = null,
    @JsonProperty("frmtrm_amount") val priorAmount: String? = null,
    @JsonProperty("bfefrmtrm_nm") val twoPriorPeriodName: String? = null,
    @JsonProperty("bfefrmtrm_amount") val twoPriorAmount: String? = null,
    val ord: String? = null,
    val currency: String? = null,
)
