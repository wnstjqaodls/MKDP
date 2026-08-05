import axios, { AxiosError } from 'axios'
import type {
  CompanyOverview,
  CompanySummary,
  DisclosurePage,
  FinancialSummary,
} from '@/types/company'
import type { AssetSummary, BacktestRequest, BacktestResult } from '@/types/portfolio'

const http = axios.create({ baseURL: '/api' })

/** 서버가 내려주는 { error: string } 형태를 사람이 읽을 메시지로 통일한다. */
export function extractErrorMessage(err: unknown): string {
  if (axios.isAxiosError(err)) {
    const data = (err as AxiosError<{ error?: string }>).response?.data
    if (data?.error) return data.error
    if (err.message) return err.message
  }
  return '알 수 없는 오류가 발생했습니다.'
}

export const companyApi = {
  search(q: string, listedOnly = false, page = 0, size = 20) {
    return http
      .get<CompanySummary[]>('/companies', { params: { q, listedOnly, page, size } })
      .then((res) => res.data)
  },

  overview(corpCode: string) {
    return http.get<CompanyOverview>(`/companies/${corpCode}`).then((res) => res.data)
  },

  disclosures(corpCode: string, bgnDe?: string, endDe?: string, page = 0, size = 20) {
    return http
      .get<DisclosurePage>(`/companies/${corpCode}/disclosures`, {
        params: { bgnDe, endDe, page, size },
      })
      .then((res) => res.data)
  },

  financials(corpCode: string, year: number, reprtCode = '11011') {
    return http
      .get<FinancialSummary>(`/companies/${corpCode}/financials`, { params: { year, reprtCode } })
      .then((res) => res.data)
  },
}

export const assetApi = {
  search(q: string, page = 0, size = 20) {
    return http.get<AssetSummary[]>('/assets', { params: { q, page, size } }).then((res) => res.data)
  },
}

export const backtestApi = {
  run(request: BacktestRequest) {
    return http.post<BacktestResult>('/backtest', request).then((res) => res.data)
  },
}
