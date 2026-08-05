<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import CompanyOverviewCard from '@/components/CompanyOverviewCard.vue'
import DisclosureTable from '@/components/DisclosureTable.vue'
import FinancialChart from '@/components/FinancialChart.vue'
import { useCompanyStore } from '@/stores/company'

const props = defineProps<{ corpCode: string }>()
const store = useCompanyStore()

const activeTab = ref<'disclosures' | 'financials'>('disclosures')
const financialYear = ref(new Date().getFullYear() - 1)

function load() {
  store.loadDetail(props.corpCode, financialYear.value)
}

onMounted(load)
watch(() => props.corpCode, load)
watch(financialYear, () => store.loadDetail(props.corpCode, financialYear.value))
</script>

<template>
  <section class="flex flex-col gap-6">
    <p v-if="store.detailLoading" class="text-sm text-slate-500">불러오는 중...</p>
    <p v-else-if="store.detailError" class="text-sm text-red-600">{{ store.detailError }}</p>

    <template v-else-if="store.overview">
      <CompanyOverviewCard :overview="store.overview" />

      <div class="rounded-lg border border-slate-200 bg-white">
        <div class="flex items-center justify-between border-b border-slate-200 px-4">
          <div class="flex gap-4">
            <button
              type="button"
              class="border-b-2 px-1 py-3 text-sm font-medium"
              :class="activeTab === 'disclosures' ? 'border-slate-900 text-slate-900' : 'border-transparent text-slate-400'"
              @click="activeTab = 'disclosures'"
            >
              공시목록
            </button>
            <button
              type="button"
              class="border-b-2 px-1 py-3 text-sm font-medium"
              :class="activeTab === 'financials' ? 'border-slate-900 text-slate-900' : 'border-transparent text-slate-400'"
              @click="activeTab = 'financials'"
            >
              재무 주요계정
            </button>
          </div>
          <select
            v-if="activeTab === 'financials'"
            v-model.number="financialYear"
            class="mr-2 rounded border border-slate-200 px-2 py-1 text-sm"
          >
            <option v-for="y in [financialYear + 1, financialYear, financialYear - 1, financialYear - 2]" :key="y" :value="y">
              {{ y }}년
            </option>
          </select>
        </div>

        <div class="p-4">
          <DisclosureTable v-if="activeTab === 'disclosures' && store.disclosures" :disclosures="store.disclosures" />
          <FinancialChart v-else-if="activeTab === 'financials' && store.financials" :financials="store.financials" />
        </div>
      </div>
    </template>
  </section>
</template>
