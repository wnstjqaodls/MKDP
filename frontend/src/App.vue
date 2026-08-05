<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">
    <header class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-5xl items-center justify-between px-4 py-4">
        <RouterLink to="/" class="text-lg font-semibold tracking-tight">
          MKDP <span class="text-slate-400 font-normal">· 공시 조회</span>
        </RouterLink>
        <nav class="text-sm text-slate-600">
          <RouterLink to="/companies" class="hover:text-slate-900">기업 검색</RouterLink>
        </nav>
      </div>
    </header>
    <main class="mx-auto max-w-5xl px-4 py-8">
      <RouterView />
    </main>
    <footer class="mx-auto max-w-5xl px-4 py-8 text-xs text-slate-400">
      데이터 출처: 금융감독원 전자공시시스템(DART) 오픈API
    </footer>
  </div>
</template>
