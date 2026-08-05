<script setup lang="ts">
import type { DisclosurePage } from '@/types/company'

defineProps<{ disclosures: DisclosurePage }>()

function formatDate(raw: string): string {
  if (raw.length !== 8) return raw
  return `${raw.slice(0, 4)}.${raw.slice(4, 6)}.${raw.slice(6, 8)}`
}
</script>

<template>
  <div class="rounded-lg border border-slate-200 bg-white">
    <table class="w-full text-sm">
      <thead class="border-b border-slate-200 text-left text-slate-500">
        <tr>
          <th class="px-4 py-2 font-medium">보고서명</th>
          <th class="px-4 py-2 font-medium">제출인</th>
          <th class="px-4 py-2 font-medium">접수일</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-slate-100">
        <tr v-for="item in disclosures.items" :key="item.receiptNo" class="hover:bg-slate-50">
          <td class="px-4 py-2.5">
            <a
              :href="item.originalDocumentUrl"
              target="_blank"
              rel="noopener noreferrer"
              class="text-blue-600 hover:underline"
            >
              {{ item.reportName }}
            </a>
            <span v-if="item.remark" class="ml-1 text-xs text-slate-400">{{ item.remark }}</span>
          </td>
          <td class="px-4 py-2.5 text-slate-600">{{ item.filerName ?? '-' }}</td>
          <td class="px-4 py-2.5 text-slate-600">{{ formatDate(item.receiptDate) }}</td>
        </tr>
        <tr v-if="disclosures.items.length === 0">
          <td colspan="3" class="px-4 py-6 text-center text-slate-400">조회된 공시가 없습니다.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
