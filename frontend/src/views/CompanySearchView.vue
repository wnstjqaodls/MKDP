<script setup lang="ts">
import { onMounted, watch } from 'vue'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { useCompanyStore } from '@/stores/company'
import CompanySearchBar from '@/components/CompanySearchBar.vue'

const route = useRoute()
const router = useRouter()
const store = useCompanyStore()

function runSearch(query: string) {
  router.replace({ path: '/companies', query: query ? { q: query } : {} })
  store.search(query)
}

onMounted(() => {
  const q = typeof route.query.q === 'string' ? route.query.q : ''
  if (q) store.search(q)
})

watch(
  () => route.query.q,
  (q) => {
    const value = typeof q === 'string' ? q : ''
    if (value !== store.searchQuery) store.search(value)
  },
)
</script>

<template>
  <section class="flex flex-col gap-6">
    <CompanySearchBar :initial-query="store.searchQuery" @search="runSearch" />

    <p v-if="store.searching" class="text-sm text-slate-500">검색 중...</p>
    <p v-else-if="store.searchError" class="text-sm text-red-600">{{ store.searchError }}</p>
    <p v-else-if="store.searchQuery && store.searchResults.length === 0" class="text-sm text-slate-500">
      '{{ store.searchQuery }}'에 대한 검색 결과가 없습니다.
    </p>

    <ul v-if="store.searchResults.length" class="divide-y divide-slate-200 rounded-lg border border-slate-200 bg-white">
      <li v-for="company in store.searchResults" :key="company.corpCode">
        <RouterLink
          :to="`/companies/${company.corpCode}`"
          class="flex items-center justify-between px-4 py-3 hover:bg-slate-50"
        >
          <span class="font-medium">{{ company.corpName }}</span>
          <span class="text-sm text-slate-400">{{ company.stockCode ?? '비상장' }}</span>
        </RouterLink>
      </li>
    </ul>
  </section>
</template>
