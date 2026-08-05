import { defineStore } from 'pinia'
import { companyApi, extractErrorMessage } from '@/api/client'
import type {
  CompanyOverview,
  CompanySummary,
  DisclosurePage,
  FinancialSummary,
} from '@/types/company'

interface CompanyState {
  searchQuery: string
  searchResults: CompanySummary[]
  searching: boolean
  searchError: string | null

  overview: CompanyOverview | null
  disclosures: DisclosurePage | null
  financials: FinancialSummary | null
  detailLoading: boolean
  detailError: string | null
}

export const useCompanyStore = defineStore('company', {
  state: (): CompanyState => ({
    searchQuery: '',
    searchResults: [],
    searching: false,
    searchError: null,

    overview: null,
    disclosures: null,
    financials: null,
    detailLoading: false,
    detailError: null,
  }),

  actions: {
    async search(query: string) {
      this.searchQuery = query
      if (!query.trim()) {
        this.searchResults = []
        this.searchError = null
        return
      }
      this.searching = true
      this.searchError = null
      try {
        this.searchResults = await companyApi.search(query)
      } catch (err) {
        this.searchError = extractErrorMessage(err)
        this.searchResults = []
      } finally {
        this.searching = false
      }
    },

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
