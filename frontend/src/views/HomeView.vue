<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import CompanySearchBar from '@/components/CompanySearchBar.vue'
import DiscoveryTable from '@/components/DiscoveryTable.vue'
import { discoveryApi } from '@/api/client'
import type { DiscoveryItem, EtfCategory } from '@/types/portfolio'

const router = useRouter()

function onSearch(query: string) {
  router.push({ path: '/companies', query: { q: query } })
}

type MainTab = 'stock-marketcap' | 'stock-volume' | 'etf'
const activeTab = ref<MainTab>('stock-marketcap')

const stockItems = ref<DiscoveryItem[]>([])
const stockLoading = ref(false)

const etfCategories = ref<EtfCategory[]>([])
const activeEtfCategory = ref<number>(1)
const etfItems = ref<DiscoveryItem[]>([])
const etfLoading = ref(false)

async function loadStocks(sort: 'marketcap' | 'volume') {
  stockLoading.value = true
  try {
    stockItems.value = await discoveryApi.stocks('KOSPI', sort, 20)
  } finally {
    stockLoading.value = false
  }
}

async function loadEtfs(tabCode: number) {
  etfLoading.value = true
  try {
    etfItems.value = await discoveryApi.etfs(tabCode, 'marketcap', 20)
  } finally {
    etfLoading.value = false
  }
}

watch(activeEtfCategory, (tabCode) => {
  if (activeTab.value === 'etf') loadEtfs(tabCode)
})

onMounted(async () => {
  loadStocks('marketcap')
  etfCategories.value = await discoveryApi.etfCategories()
})

function selectStockTab(sort: 'marketcap' | 'volume') {
  activeTab.value = sort === 'marketcap' ? 'stock-marketcap' : 'stock-volume'
  loadStocks(sort)
}

function selectEtfTab() {
  activeTab.value = 'etf'
  loadEtfs(activeEtfCategory.value)
}
</script>

<template>
  <section class="flex flex-col gap-6">
    <div class="max-w-md">
      <CompanySearchBar @search="onSearch" />
    </div>

    <div>
      <div class="mb-3 flex gap-1 border-b border-slate-200 text-sm">
        <button
          type="button"
          class="border-b-2 px-3 py-2 font-medium"
          :class="activeTab === 'stock-marketcap' ? 'border-slate-900 text-slate-900' : 'border-transparent text-slate-400'"
          @click="selectStockTab('marketcap')"
        >
          시가총액 Top 20
        </button>
        <button
          type="button"
          class="border-b-2 px-3 py-2 font-medium"
          :class="activeTab === 'stock-volume' ? 'border-slate-900 text-slate-900' : 'border-transparent text-slate-400'"
          @click="selectStockTab('volume')"
        >
          거래량 Top 20
        </button>
        <button
          type="button"
          class="border-b-2 px-3 py-2 font-medium"
          :class="activeTab === 'etf' ? 'border-slate-900 text-slate-900' : 'border-transparent text-slate-400'"
          @click="selectEtfTab"
        >
          ETF
        </button>
      </div>

      <template v-if="activeTab === 'etf'">
        <div class="mb-3 flex flex-wrap gap-2">
          <button
            v-for="cat in etfCategories"
            :key="cat.tabCode"
            type="button"
            class="rounded border px-3 py-1 text-xs font-medium"
            :class="
              activeEtfCategory === cat.tabCode
                ? 'border-slate-900 bg-slate-900 text-white'
                : 'border-slate-300 text-slate-600 hover:border-slate-400'
            "
            @click="activeEtfCategory = cat.tabCode"
          >
            {{ cat.label }}
          </button>
        </div>
        <DiscoveryTable :items="etfItems" metric-label="시가총액" :loading="etfLoading" />
      </template>
      <DiscoveryTable
        v-else
        :items="stockItems"
        :metric-label="activeTab === 'stock-volume' ? '거래량' : '시가총액'"
        :loading="stockLoading"
      />
    </div>
  </section>
</template>
