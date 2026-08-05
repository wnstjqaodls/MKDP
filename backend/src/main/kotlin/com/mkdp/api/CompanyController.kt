package com.mkdp.api

import com.mkdp.domain.CompanyService
import com.mkdp.domain.DisclosureService
import com.mkdp.domain.FinancialService
import jakarta.validation.constraints.Pattern
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/companies")
class CompanyController(
    private val companyService: CompanyService,
    private val disclosureService: DisclosureService,
    private val financialService: FinancialService,
) {
    @GetMapping
    fun search(
        @RequestParam q: String,
        @RequestParam(defaultValue = "false") listedOnly: Boolean,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ) = companyService.search(q, listedOnly, page.coerceAtLeast(0), size.coerceIn(1, 100))

    @GetMapping("/{corpCode}")
    fun overview(@PathVariable @Pattern(regexp = "\\d{8}") corpCode: String) =
        companyService.overview(corpCode)

    @GetMapping("/{corpCode}/disclosures")
    fun disclosures(
        @PathVariable @Pattern(regexp = "\\d{8}") corpCode: String,
        @RequestParam(required = false) bgnDe: String?,
        @RequestParam(required = false) endDe: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ) = disclosureService.list(corpCode, bgnDe, endDe, page.coerceAtLeast(0), size.coerceIn(1, 100))

    @GetMapping("/{corpCode}/financials")
    fun financials(
        @PathVariable @Pattern(regexp = "\\d{8}") corpCode: String,
        @RequestParam year: Int,
        @RequestParam(defaultValue = "11011") reprtCode: String,
    ) = financialService.keyAccounts(corpCode, year, reprtCode)
}
