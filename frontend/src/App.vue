<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { usePortfolioStore } from '@/stores/portfolio'

const portfolio = usePortfolioStore()
</script>

<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">
    <header class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-5xl items-center justify-between px-4 py-4">
        <RouterLink to="/" class="text-lg font-semibold tracking-tight"> MKDP </RouterLink>
        <nav class="flex items-center gap-4 text-sm text-slate-600">
          <RouterLink to="/companies" class="hover:text-slate-900">자산 검색</RouterLink>
          <RouterLink to="/portfolio" class="relative flex items-center gap-1.5 hover:text-slate-900">
            포트폴리오
            <span
              v-if="portfolio.items.length"
              class="flex h-5 min-w-5 items-center justify-center rounded-full bg-slate-900 px-1 text-xs font-semibold text-white"
            >
              {{ portfolio.items.length }}
            </span>
          </RouterLink>
        </nav>
      </div>
    </header>
    <main class="mx-auto max-w-5xl px-4 py-8">
      <RouterView />
    </main>
    <footer class="mx-auto max-w-5xl px-4 py-8 text-xs text-slate-400">
      데이터 출처: 금융감독원 전자공시시스템(DART), 네이버 금융
    </footer>
  </div>
</template>
