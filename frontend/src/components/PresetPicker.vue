<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { presetApi } from '@/api/client'
import { usePortfolioStore } from '@/stores/portfolio'
import type { PortfolioPreset } from '@/types/portfolio'

const portfolio = usePortfolioStore()
const presets = ref<PortfolioPreset[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    presets.value = await presetApi.list()
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <h2 class="mb-3 text-sm font-medium text-slate-700">추천 포트폴리오</h2>
    <p v-if="loading" class="text-sm text-slate-400">불러오는 중...</p>
    <div v-else class="grid grid-cols-1 gap-3 sm:grid-cols-3">
      <button
        v-for="preset in presets"
        :key="preset.key"
        type="button"
        class="flex flex-col items-start gap-2 rounded-lg border border-slate-200 bg-white p-4 text-left hover:border-slate-400"
        @click="portfolio.applyPreset(preset)"
      >
        <span class="font-semibold">{{ preset.label }}</span>
        <span class="text-xs text-slate-500">{{ preset.description }}</span>
        <div class="flex flex-wrap gap-1 pt-1">
          <span
            v-for="h in preset.holdings"
            :key="h.symbol"
            class="rounded bg-slate-100 px-2 py-0.5 text-[11px] text-slate-600"
          >
            {{ h.name }} {{ Math.round(h.weight) }}%
          </span>
        </div>
      </button>
    </div>
  </div>
</template>
