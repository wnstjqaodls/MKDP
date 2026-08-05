import { defineStore } from 'pinia'
import { companyApi, extractErrorMessage } from '@/api/client'
import type { CompanyOverview, DisclosurePage, FinancialSummary } from '@/types/company'

interface CompanyState {
  overview: CompanyOverview | null
  disclosures: DisclosurePage | null
  financials: FinancialSummary | null
  detailLoading: boolean
  detailError: string | null
}

export const useCompanyStore = defineStore('company', {
  state: (): CompanyState => ({
    overview: null,
    disclosures: null,
    financials: null,
    detailLoading: false,
    detailError: null,
  }),

  actions: {
    async loadDetail(corpCode: string, year: number) {
      this.detailLoading = true
      this.detailError = null
      this.overview = null
      this.disclosures = null
      this.financials = null
      try {
        const [overview, disclosures, financials] = await Promise.all([
          companyApi.overview(corpCode),
          companyApi.disclosures(corpCode),
          companyApi.financials(corpCode, year).catch(() => null),
        ])
        this.overview = overview
        this.disclosures = disclosures
        this.financials = financials
      } catch (err) {
        this.detailError = extractErrorMessage(err)
      } finally {
        this.detailLoading = false
      }
    },
  },
})
