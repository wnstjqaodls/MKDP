<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { usePortfolioStore } from '@/stores/portfolio'
import type { DiscoveryItem } from '@/types/portfolio'

const props = defineProps<{ items: DiscoveryItem[]; metricLabel: string; loading?: boolean }>()
const portfolio = usePortfolioStore()

function formatMetric(item: DiscoveryItem): string {
  const value = props.metricLabel === '거래량' ? item.tradingVolume : item.marketValue
  if (value == null) return '-'
  const eok = value / 100_000_000
  if (eok >= 10_000) return `${(eok / 10_000).toFixed(1)}조`
  return `${Math.round(eok).toLocaleString('ko-KR')}억`
}

function inCart(symbol: string): boolean {
  return portfolio.items.some((i) => i.symbol === symbol)
}

function toggleCart(item: DiscoveryItem) {
  if (inCart(item.symbol)) {
    portfolio.remove(item.symbol)
  } else {
    portfolio.add({ symbol: item.symbol, name: item.name, type: item.type })
  }
}
</script>

<template>
  <div class="rounded-lg border border-slate-200 bg-white">
    <p v-if="loading" class="px-4 py-6 text-center text-sm text-slate-400">불러오는 중...</p>
    <p v-else-if="items.length === 0" class="px-4 py-6 text-center text-sm text-slate-400">데이터가 없습니다.</p>
    <ul v-else class="divide-y divide-slate-100">
      <li v-for="(item, i) in items" :key="item.symbol" class="flex items-center gap-3 px-4 py-2.5">
        <span class="w-5 shrink-0 text-right text-xs text-slate-400">{{ i + 1 }}</span>
        <component
          :is="item.type === 'STOCK' ? RouterLink : 'div'"
          :to="item.type === 'STOCK' ? `/companies/${item.symbol}` : undefined"
          class="min-w-0 flex-1 truncate text-sm font-medium hover:text-blue-600"
        >
          {{ item.name }}
        </component>
        <span
          v-if="item.changeRatePct != null"
          class="w-16 shrink-0 text-right text-xs font-medium"
          :class="item.changeRatePct > 0 ? 'text-red-600' : item.changeRatePct < 0 ? 'text-blue-600' : 'text-slate-400'"
        >
          {{ item.changeRatePct > 0 ? '+' : '' }}{{ item.changeRatePct.toFixed(2) }}%
        </span>
        <span class="w-20 shrink-0 text-right text-xs text-slate-500">{{ formatMetric(item) }}</span>
        <button
          type="button"
          class="shrink-0 rounded-md border px-2.5 py-1 text-xs font-medium"
          :class="
            inCart(item.symbol)
              ? 'border-slate-300 bg-slate-100 text-slate-500'
              : 'border-slate-900 bg-slate-900 text-white hover:bg-slate-700'
          "
          @click="toggleCart(item)"
        >
          {{ inCart(item.symbol) ? '담음' : '담기' }}
        </button>
      </li>
    </ul>
  </div>
</template>
