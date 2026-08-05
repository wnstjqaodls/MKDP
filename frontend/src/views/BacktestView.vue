<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { usePortfolioStore } from '@/stores/portfolio'
import PortfolioValueChart from '@/components/PortfolioValueChart.vue'

const portfolio = usePortfolioStore()

const weightShares = computed(() => {
  const total = portfolio.totalWeight || 1
  return Object.fromEntries(portfolio.items.map((item) => [item.symbol, (item.weight / total) * 100]))
})

function formatWon(value: number): string {
  return `${Math.round(value).toLocaleString('ko-KR')}원`
}

function formatPct(value: number): string {
  const rounded = Math.round(value * 10) / 10
  return `${rounded > 0 ? '+' : ''}${rounded.toLocaleString('ko-KR')}%`
}
</script>

<template>
  <section class="flex flex-col gap-6">
    <h1 class="text-xl font-semibold">포트폴리오 백테스트</h1>

    <div v-if="portfolio.items.length === 0" class="rounded-lg border border-dashed border-slate-300 p-8 text-center text-sm text-slate-500">
      담은 종목이 없습니다.
      <RouterLink to="/companies" class="text-blue-600 hover:underline">자산 검색</RouterLink>에서
      주식이나 ETF를 담아주세요.
    </div>

    <template v-else>
      <div class="rounded-lg border border-slate-200 bg-white p-4">
        <h2 class="mb-3 text-sm font-medium text-slate-700">담은 종목 ({{ portfolio.items.length }})</h2>
        <ul class="divide-y divide-slate-100">
          <li v-for="item in portfolio.items" :key="item.symbol" class="flex items-center gap-3 py-2.5">
            <span
              class="shrink-0 rounded px-1.5 py-0.5 text-xs font-medium"
              :class="item.type === 'ETF' ? 'bg-orange-50 text-orange-600' : 'bg-blue-50 text-blue-600'"
            >
              {{ item.type === 'ETF' ? 'ETF' : '주식' }}
            </span>
            <span class="min-w-0 flex-1 truncate text-sm font-medium">{{ item.name }}</span>
            <input
              type="number"
              min="0"
              step="1"
              :value="item.weight"
              class="w-20 rounded border border-slate-200 px-2 py-1 text-right text-sm"
              @input="portfolio.setWeight(item.symbol, Number(($event.target as HTMLInputElement).value))"
            />
            <span class="w-14 shrink-0 text-right text-xs text-slate-400">
              {{ weightShares[item.symbol]?.toFixed(1) ?? '0.0' }}%
            </span>
            <button
              type="button"
              class="shrink-0 text-slate-400 hover:text-red-600"
              aria-label="제거"
              @click="portfolio.remove(item.symbol)"
            >
              ✕
            </button>
          </li>
        </ul>
      </div>

      <div class="rounded-lg border border-slate-200 bg-white p-4">
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-3">
          <label class="flex flex-col gap-1 text-sm text-slate-600">
            시작일
            <input v-model="portfolio.startDate" type="date" class="rounded border border-slate-200 px-2 py-1.5" />
          </label>
          <label class="flex flex-col gap-1 text-sm text-slate-600">
            종료일
            <input v-model="portfolio.endDate" type="date" class="rounded border border-slate-200 px-2 py-1.5" />
          </label>
          <label class="flex flex-col gap-1 text-sm text-slate-600">
            초기 투자금
            <input
              v-model.number="portfolio.initialAmount"
              type="number"
              min="1"
              step="10000"
              class="rounded border border-slate-200 px-2 py-1.5"
            />
          </label>
        </div>
        <button
          type="button"
          class="mt-4 w-full rounded-md bg-slate-900 py-2.5 text-sm font-semibold text-white hover:bg-slate-700 disabled:opacity-50"
          :disabled="portfolio.running"
          @click="portfolio.runBacktest"
        >
          {{ portfolio.running ? '백테스트 실행 중...' : '백테스트 실행' }}
        </button>
        <p v-if="portfolio.error" class="mt-2 text-sm text-red-600">{{ portfolio.error }}</p>
      </div>

      <div v-if="portfolio.result" class="rounded-lg border border-slate-200 bg-white p-4">
        <div class="grid grid-cols-2 gap-4 sm:grid-cols-4">
          <div>
            <p class="text-xs text-slate-500">최종 자산가치</p>
            <p class="mt-1 text-lg font-semibold">{{ formatWon(portfolio.result.finalValue) }}</p>
          </div>
          <div>
            <p class="text-xs text-slate-500">총수익률</p>
            <p class="mt-1 text-lg font-semibold" :class="portfolio.result.totalReturnPct >= 0 ? 'text-red-600' : 'text-blue-600'">
              {{ formatPct(portfolio.result.totalReturnPct) }}
            </p>
          </div>
          <div>
            <p class="text-xs text-slate-500">CAGR</p>
            <p class="mt-1 text-lg font-semibold" :class="portfolio.result.cagrPct >= 0 ? 'text-red-600' : 'text-blue-600'">
              {{ formatPct(portfolio.result.cagrPct) }}
            </p>
          </div>
          <div>
            <p class="text-xs text-slate-500">MDD (최대 낙폭)</p>
            <p class="mt-1 text-lg font-semibold text-blue-600">{{ formatPct(portfolio.result.mddPct) }}</p>
          </div>
        </div>
        <div class="mt-6">
          <PortfolioValueChart :series="portfolio.result.series" />
        </div>
      </div>
    </template>
  </section>
</template>
