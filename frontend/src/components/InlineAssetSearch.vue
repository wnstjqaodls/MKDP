<script setup lang="ts">
import { ref } from 'vue'
import { assetApi } from '@/api/client'
import { useDebouncedFn } from '@/composables/useDebouncedFn'
import { usePortfolioStore } from '@/stores/portfolio'
import type { AssetSummary } from '@/types/portfolio'

const portfolio = usePortfolioStore()
const query = ref('')
const results = ref<AssetSummary[]>([])
const searching = ref(false)
const showResults = ref(false)

const debouncedSearch = useDebouncedFn(async (q: string) => {
  if (!q.trim()) {
    results.value = []
    return
  }
  searching.value = true
  try {
    results.value = await assetApi.search(q)
  } finally {
    searching.value = false
  }
}, 250)

function onInput() {
  showResults.value = true
  debouncedSearch(query.value)
}

function inCart(symbol: string): boolean {
  return portfolio.items.some((i) => i.symbol === symbol)
}

function add(asset: AssetSummary) {
  portfolio.add(asset)
}
</script>

<template>
  <div class="relative">
    <input
      v-model="query"
      type="text"
      placeholder="종목명 또는 코드로 검색해서 바로 담기 (예: 삼성전자, KODEX)"
      class="w-full rounded-lg border border-slate-300 px-4 py-2.5 text-sm shadow-sm focus:border-slate-400 focus:outline-none"
      @input="onInput"
      @focus="showResults = true"
    />

    <div
      v-if="showResults && query.trim()"
      class="mt-2 max-h-72 overflow-y-auto rounded-lg border border-slate-200 bg-white shadow-sm"
    >
      <p v-if="searching" class="px-4 py-3 text-sm text-slate-400">검색 중...</p>
      <p v-else-if="results.length === 0" class="px-4 py-3 text-sm text-slate-400">검색 결과가 없습니다.</p>
      <ul v-else class="divide-y divide-slate-100">
        <li v-for="asset in results" :key="`${asset.type}-${asset.symbol}`">
          <button
            type="button"
            class="flex w-full items-center gap-2 px-4 py-2.5 text-left hover:bg-slate-50 disabled:cursor-default disabled:opacity-50"
            :disabled="inCart(asset.symbol)"
            @click="add(asset)"
          >
            <span
              class="shrink-0 rounded px-1.5 py-0.5 text-xs font-medium"
              :class="asset.type === 'ETF' ? 'bg-orange-50 text-orange-600' : 'bg-blue-50 text-blue-600'"
            >
              {{ asset.type === 'ETF' ? 'ETF' : '주식' }}
            </span>
            <span class="min-w-0 flex-1 truncate text-sm font-medium">{{ asset.name }}</span>
            <span class="shrink-0 text-xs text-slate-400">{{ asset.symbol }}</span>
            <span class="shrink-0 text-xs font-medium" :class="inCart(asset.symbol) ? 'text-slate-400' : 'text-blue-600'">
              {{ inCart(asset.symbol) ? '담음' : '+ 담기' }}
            </span>
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>
