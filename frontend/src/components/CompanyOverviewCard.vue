<script setup lang="ts">
import type { CompanyOverview } from '@/types/company'

defineProps<{ overview: CompanyOverview }>()

function formatDate(raw: string | null): string {
  if (!raw || raw.length !== 8) return '-'
  return `${raw.slice(0, 4)}.${raw.slice(4, 6)}.${raw.slice(6, 8)}`
}
</script>

<template>
  <div class="rounded-lg border border-slate-200 bg-white p-5">
    <div class="flex items-baseline justify-between gap-4">
      <h2 class="text-xl font-semibold">{{ overview.corpName }}</h2>
      <span v-if="overview.stockCode" class="text-sm text-slate-400">{{ overview.stockCode }}</span>
      <span v-else class="text-sm text-slate-400">비상장</span>
    </div>
    <p v-if="overview.corpNameEng" class="mt-1 text-sm text-slate-500">{{ overview.corpNameEng }}</p>

    <dl class="mt-4 grid grid-cols-1 gap-x-6 gap-y-2 text-sm sm:grid-cols-2">
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">대표자</dt>
        <dd>{{ overview.ceoName ?? '-' }}</dd>
      </div>
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">법인구분</dt>
        <dd>{{ overview.corpClass ?? '-' }}</dd>
      </div>
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">설립일</dt>
        <dd>{{ formatDate(overview.establishedDate) }}</dd>
      </div>
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">결산월</dt>
        <dd>{{ overview.settlementMonth ? `${overview.settlementMonth}월` : '-' }}</dd>
      </div>
      <div class="flex justify-between gap-2 sm:col-span-2">
        <dt class="text-slate-500">주소</dt>
        <dd class="text-right">{{ overview.address ?? '-' }}</dd>
      </div>
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">전화</dt>
        <dd>{{ overview.phoneNumber ?? '-' }}</dd>
      </div>
      <div class="flex justify-between gap-2">
        <dt class="text-slate-500">홈페이지</dt>
        <dd>
          <a
            v-if="overview.homepageUrl"
            :href="overview.homepageUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="text-blue-600 hover:underline"
          >
            {{ overview.homepageUrl }}
          </a>
          <span v-else>-</span>
        </dd>
      </div>
    </dl>
  </div>
</template>
