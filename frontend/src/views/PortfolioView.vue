<script setup lang="ts">
import { computed } from 'vue'
import { usePortfolioStore } from '@/stores/portfolio'
import PresetPicker from '@/components/PresetPicker.vue'
import InlineAssetSearch from '@/components/InlineAssetSearch.vue'
import PortfolioValueChart from '@/components/PortfolioValueChart.vue'

const portfolio = usePortfolioStore()

// palette.md 카테고리 순서(blue, orange, aqua, yellow, magenta, green, violet, red) — 항목별 식별용 색상.
const PALETTE = ['#2a78d6', '#eb6834', '#1baf7a', '#eda100', '#e87ba4', '#008300', '#4a3aa7', '#e34948']
function colorFor(index: number): string {
  return PALETTE[index % PALETTE.length]
}

function formatWon(value: number): string {
  return `${Math.round(value).toLocaleString('ko-KR')}원`
}

function formatPct(value: number): string {
  const rounded = Math.round(value * 10) / 10
  return `${rounded > 0 ? '+' : ''}${rounded.toLocaleString('ko-KR')}%`
}

const canRunBacktest = computed(() => portfolio.items.length > 0 && !portfolio.running)
</script>

<template>
  <section class="flex flex-col gap-8">
    <h1 class="text-xl font-semibold">포트폴리오 만들기</h1>

    <PresetPicker />

    <div>
      <h2 class="mb-3 text-sm font-medium text-slate-700">직접 검색해서 담기</h2>
      <InlineAssetSearch />
    </div>

    <div v-if="portfolio.items.length === 0" class="rounded-lg border border-dashed border-slate-300 p-8 text-center text-sm text-slate-500">
      위에서 추천 포트폴리오를 고르거나, 검색해서 종목을 담아주세요.
    </div>

    <template v-else>
      <div class="rounded-lg border border-slate-200 bg-white p-4">
        <div class="mb-4 flex items-center justify-between">
          <h2 class="text-sm font-medium text-slate-700">담은 종목 ({{ portfolio.items.length }})</h2>
          <button type="button" class="text-xs text-slate-400 hover:text-red-600" @click="portfolio.clear">
            전체 비우기
          </button>
        </div>

        <!-- 비중 한눈에 보기: 누적 막대 -->
        <div class="mb-4 flex h-3 w-full overflow-hidden rounded-full bg-slate-100">
          <div
            v-for="(item, i) in portfolio.items"
            :key="item.symbol"
            :style="{ width: `${item.weight}%`, backgroundColor: colorFor(i) }"
            :title="`${item.name} ${item.weight.toFixed(1)}%`"
          />
        </div>

        <ul class="divide-y divide-slate-100">
          <li v-for="(item, i) in portfolio.items" :key="item.symbol" class="flex items-center gap-3 py-3">
            <span class="h-2.5 w-2.5 shrink-0 rounded-full" :style="{ backgroundColor: colorFor(i) }" />
            <span
              class="shrink-0 rounded px-1.5 py-0.5 text-xs font-medium"
              :class="item.type === 'ETF' ? 'bg-orange-50 text-orange-600' : 'bg-blue-50 text-blue-600'"
            >
              {{ item.type === 'ETF' ? 'ETF' : '주식' }}
            </span>
            <span class="w-32 shrink-0 truncate text-sm font-medium sm:w-48">{{ item.name }}</span>
            <input
              type="range"
              min="0"
              max="100"
              step="1"
              :value="item.weight"
              class="min-w-0 flex-1 accent-slate-900"
              @input="portfolio.adjustWeight(item.symbol, Number(($event.target as HTMLInputElement).value))"
            />
            <span class="w-14 shrink-0 text-right text-sm tabular-nums text-slate-600">{{ item.weight.toFixed(1) }}%</span>
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
        <p class="mt-2 text-xs text-slate-400">
          슬라이더를 움직이면 나머지 종목의 비중이 비율대로 자동 조정돼 합계는 항상 100%로 유지됩니다.
        </p>
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
          :disabled="!canRunBacktest"
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
