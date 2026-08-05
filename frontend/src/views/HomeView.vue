<script setup lang="ts">
import { useRouter } from 'vue-router'
import CompanySearchBar from '@/components/CompanySearchBar.vue'

const router = useRouter()

function onSearch(query: string) {
  router.push({ path: '/companies', query: { q: query } })
}
</script>

<template>
  <section class="flex flex-col items-center gap-6 py-12 text-center">
    <h1 class="text-3xl font-bold tracking-tight">기업 공시, 한 번에 찾아보기</h1>
    <p class="max-w-xl text-slate-600">
      DART 오픈API로 상장·비상장 기업의 공시목록과 재무 주요계정을 조회합니다.
    </p>
    <div class="w-full max-w-md">
      <CompanySearchBar @search="onSearch" />
    </div>
  </section>
</template>
