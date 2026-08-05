<script setup lang="ts">
import { onMounted, watch } from 'vue'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { useAssetSearchStore } from '@/stores/assetSearch'
import { usePortfolioStore } from '@/stores/portfolio'
import CompanySearchBar from '@/components/CompanySearchBar.vue'
import type { AssetSummary } from '@/types/portfolio'

const route = useRoute()
const router = useRouter()
const store = useAssetSearchStore()
const portfolio = usePortfolioStore()

function runSearch(query: string) {
  router.replace({ path: '/companies', query: query ? { q: query } : {} })
  store.search(query)
}

function inCart(symbol: string): boolean {
  return portfolio.items.some((item) => item.symbol === symbol)
}

function toggleCart(asset: AssetSummary) {
  if (inCart(asset.symbol)) {
    portfolio.remove(asset.symbol)
  } else {
    portfolio.add(asset)
  }
}

onMounted(() => {
  const q = typeof route.query.q === 'string' ? route.query.q : ''
  if (q) store.search(q)
})

watch(
  () => route.query.q,
  (q) => {
    const value = typeof q === 'string' ? q : ''
    if (value !== store.query) store.search(value)
  },
)
</script>

<template>
  <section class="flex flex-col gap-6">
    <CompanySearchBar :initial-query="store.query" @search="runSearch" />

    <p v-if="store.searching" class="text-sm text-slate-500">검색 중...</p>
    <p v-else-if="store.error" class="text-sm text-red-600">{{ store.error }}</p>
    <p v-else-if="store.query && store.results.length === 0" class="text-sm text-slate-500">
      '{{ store.query }}'에 대한 검색 결과가 없습니다.
    </p>

    <ul v-if="store.results.length" class="divide-y divide-slate-200 rounded-lg border border-slate-200 bg-white">
      <li v-for="asset in store.results" :key="`${asset.type}-${asset.symbol}`">
        <div class="flex items-center justify-between gap-3 px-4 py-3 hover:bg-slate-50">
          <component
            :is="asset.type === 'STOCK' ? RouterLink : 'div'"
            :to="asset.type === 'STOCK' ? `/companies/${asset.symbol}` : undefined"
            class="flex min-w-0 flex-1 items-center gap-2"
          >
            <span
              class="shrink-0 rounded px-1.5 py-0.5 text-xs font-medium"
              :class="asset.type === 'ETF' ? 'bg-orange-50 text-orange-600' : 'bg-blue-50 text-blue-600'"
            >
              {{ asset.type === 'ETF' ? 'ETF' : '주식' }}
            </span>
            <span class="truncate font-medium">{{ asset.name }}</span>
            <span class="shrink-0 text-sm text-slate-400">{{ asset.symbol }}</span>
          </component>
          <button
            type="button"
            class="shrink-0 rounded-md border px-3 py-1.5 text-sm font-medium"
            :class="
              inCart(asset.symbol)
                ? 'border-slate-300 bg-slate-100 text-slate-500'
                : 'border-slate-900 bg-slate-900 text-white hover:bg-slate-700'
            "
            @click="toggleCart(asset)"
          >
            {{ inCart(asset.symbol) ? '담음' : '담기' }}
          </button>
        </div>
      </li>
    </ul>
  </section>
</template>
