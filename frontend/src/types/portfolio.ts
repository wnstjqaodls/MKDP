export type AssetType = 'STOCK' | 'ETF'

export interface AssetSummary {
  symbol: string
  name: string
  type: AssetType
}

export interface BacktestHoldingRequest {
  symbol: string
  weight: number
}

export interface BacktestRequest {
  holdings: BacktestHoldingRequest[]
  startDate: string
  endDate: string
  initialAmount: number
}

export interface BacktestPoint {
  date: string
  value: number
}

export interface BacktestResult {
  startDate: string
  endDate: string
  initialAmount: number
  finalValue: number
  totalReturnPct: number
  cagrPct: number
  mddPct: number
  series: BacktestPoint[]
}
