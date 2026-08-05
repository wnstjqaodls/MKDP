import { defineStore } from 'pinia'
import { backtestApi, extractErrorMessage } from '@/api/client'
import type { AssetSummary, BacktestResult } from '@/types/portfolio'

export interface CartItem extends AssetSummary {
  weight: number
}

interface PortfolioState {
  items: CartItem[]
  startDate: string
  endDate: string
  initialAmount: number
  running: boolean
  error: string | null
  result: BacktestResult | null
}

function defaultStartDate(): string {
  const d = new Date()
  d.setFullYear(d.getFullYear() - 3)
  return d.toISOString().slice(0, 10)
}

function defaultEndDate(): string {
  return new Date().toISOString().slice(0, 10)
}

export const usePortfolioStore = defineStore('portfolio', {
  state: (): PortfolioState => ({
    items: [],
    startDate: defaultStartDate(),
    endDate: defaultEndDate(),
    initialAmount: 10_000_000,
    running: false,
    error: null,
    result: null,
  }),

  getters: {
    totalWeight: (state) => state.items.reduce((sum, item) => sum + item.weight, 0),
  },

  actions: {
    add(asset: AssetSummary) {
      if (this.items.some((item) => item.symbol === asset.symbol)) return
      this.items.push({ ...asset, weight: 10 })
    },

    remove(symbol: string) {
      this.items = this.items.filter((item) => item.symbol !== symbol)
    },

    setWeight(symbol: string, weight: number) {
      const item = this.items.find((i) => i.symbol === symbol)
      if (item) item.weight = Math.max(0, weight)
    },

    clear() {
      this.items = []
      this.result = null
    },

    async runBacktest() {
      if (this.items.length === 0) {
        this.error = '종목을 1개 이상 담아주세요.'
        return
      }
      this.running = true
      this.error = null
      this.result = null
      try {
        this.result = await backtestApi.run({
          holdings: this.items.map((item) => ({ symbol: item.symbol, weight: item.weight })),
          startDate: this.startDate,
          endDate: this.endDate,
          initialAmount: this.initialAmount,
        })
      } catch (err) {
        this.error = extractErrorMessage(err)
      } finally {
        this.running = false
      }
    },
  },
})
