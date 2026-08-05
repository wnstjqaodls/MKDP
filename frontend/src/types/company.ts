export interface CompanySummary {
  corpCode: string
  corpName: string
  stockCode: string | null
}

export interface CompanyOverview {
  corpCode: string
  corpName: string
  corpNameEng: string | null
  stockCode: string | null
  ceoName: string | null
  corpClass: string | null
  address: string | null
  homepageUrl: string | null
  phoneNumber: string | null
  industryCode: string | null
  establishedDate: string | null
  settlementMonth: string | null
}

export interface DisclosureItem {
  receiptNo: string
  reportName: string
  filerName: string | null
  receiptDate: string
  remark: string | null
  originalDocumentUrl: string
}

export interface DisclosurePage {
  items: DisclosureItem[]
  page: number
  size: number
  totalCount: number
  totalPage: number
}

export interface FinancialTrend {
  currentPeriod: string | null
  currentAmount: number | null
  priorPeriod: string | null
  priorAmount: number | null
  twoPriorPeriod: string | null
  twoPriorAmount: number | null
  unit: string
}

export interface FinancialSummary {
  corpCode: string
  year: number
  reprtCode: string
  revenue: FinancialTrend | null
  operatingProfit: FinancialTrend | null
  netIncome: FinancialTrend | null
}

export interface ApiError {
  error: string
}
