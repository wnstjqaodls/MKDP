import { defineStore } from 'pinia'
import { backtestApi, extractErrorMessage } from '@/api/client'
import type { AssetSummary, BacktestResult, PortfolioPreset } from '@/types/portfolio'

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

/** 비중 합이 항상 100이 되도록, 바뀐 항목을 뺀 나머지를 기존 비율 그대로 재분배한다. */
function redistribute(items: CartItem[], changedSymbol: string, newWeight: number) {
  const clamped = Math.max(0, Math.min(100, newWeight))
  const others = items.filter((item) => item.symbol !== changedSymbol)
  const remaining = 100 - clamped
  const othersSum = others.reduce((sum, item) => sum + item.weight, 0)

  if (others.length === 0) {
    // 종목이 하나뿐이면 100%로 고정
  } else if (othersSum <= 0) {
    const equalShare = remaining / others.length
    others.forEach((item) => (item.weight = equalShare))
  } else {
    others.forEach((item) => (item.weight = (item.weight / othersSum) * remaining))
  }

  const target = items.find((item) => item.symbol === changedSymbol)
  if (target) target.weight = items.length === 1 ? 100 : clamped
}

/** 새 항목이 균등한 몫을 가져가도록 나머지를 비례 축소한다. */
function redistributeForNewItem(items: CartItem[]) {
  const count = items.length
  if (count === 0) return
  const newShare = 100 / count
  const existing = items.slice(0, count - 1)
  const existingSum = existing.reduce((sum, item) => sum + item.weight, 0)
  if (existingSum > 0) {
    const remaining = 100 - newShare
    existing.forEach((item) => (item.weight = (item.weight / existingSum) * remaining))
  }
  items[count - 1].weight = newShare
}

/** 항목 제거 후 남은 비중의 합이 100이 되도록 비례 확대한다. */
function redistributeAfterRemoval(items: CartItem[]) {
  if (items.length === 0) return
  const sum = items.reduce((s, item) => s + item.weight, 0)
  if (sum <= 0) {
    const equalShare = 100 / items.length
    items.forEach((item) => (item.weight = equalShare))
  } else if (Math.abs(sum - 100) > 0.001) {
    items.forEach((item) => (item.weight = (item.weight / sum) * 100))
  }
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
      this.items.push({ ...asset, weight: 0 })
      redistributeForNewItem(this.items)
    },

    remove(symbol: string) {
      this.items = this.items.filter((item) => item.symbol !== symbol)
      redistributeAfterRemoval(this.items)
    },

    /** 슬라이더로 비중을 바꾼다 — 나머지 종목이 기존 비율대로 자동 조정되어 합계는 항상 100%. */
    adjustWeight(symbol: string, weight: number) {
      redistribute(this.items, symbol, weight)
    },

    applyPreset(preset: PortfolioPreset) {
      this.items = preset.holdings.map((h) => ({ symbol: h.symbol, name: h.name, type: h.type, weight: h.weight }))
      this.result = null
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
