<script setup lang="ts">
import { computed, reactive } from 'vue'
import type { FinancialSummary, FinancialTrend } from '@/types/company'

const props = defineProps<{ financials: FinancialSummary }>()

interface MetricPoint {
  period: string
  amountEok: number
  raw: number
}

interface Metric {
  key: string
  label: string
  seriesVar: string
  points: MetricPoint[]
}

const EOK = 100_000_000

function toPoints(trend: FinancialTrend | null): MetricPoint[] {
  if (!trend) return []
  const candidates: Array<[string | null, number | null]> = [
    [trend.twoPriorPeriod, trend.twoPriorAmount],
    [trend.priorPeriod, trend.priorAmount],
    [trend.currentPeriod, trend.currentAmount],
  ]
  return candidates
    .filter((c): c is [string, number] => c[0] != null && c[1] != null)
    .map(([period, raw]) => ({ period, raw, amountEok: raw / EOK }))
}

const metrics = computed<Metric[]>(() => [
  { key: 'revenue', label: '매출액', seriesVar: '--series-1', points: toPoints(props.financials.revenue) },
  {
    key: 'operatingProfit',
    label: '영업이익',
    seriesVar: '--series-2',
    points: toPoints(props.financials.operatingProfit),
  },
  { key: 'netIncome', label: '당기순이익', seriesVar: '--series-3', points: toPoints(props.financials.netIncome) },
])

const hasAnyData = computed(() => metrics.value.some((m) => m.points.length > 0))

/** 회계 관행에 따라 음수는 괄호로 표기한다("(1,234)") — 색상에 의존하지 않는 부호 표시. */
function formatEok(value: number): string {
  const rounded = Math.round(value * 10) / 10
  const abs = Math.abs(rounded).toLocaleString('ko-KR', { maximumFractionDigits: 1 })
  return rounded < 0 ? `(${abs})` : abs
}

const CHART_HEIGHT = 120
const BAR_WIDTH = 24
const GAP = 12

function barGeometry(points: MetricPoint[]) {
  if (points.length === 0) return { bars: [], zeroY: CHART_HEIGHT }
  const values = points.map((p) => p.amountEok)
  const max = Math.max(0, ...values)
  const min = Math.min(0, ...values)
  const range = max - min || 1
  const usableHeight = CHART_HEIGHT - 24 // 상단 라벨 여백
  const zeroY = 12 + (max / range) * usableHeight

  const bars = points.map((point, i) => {
    const y = 12 + ((max - point.amountEok) / range) * usableHeight
    const top = Math.min(y, zeroY)
    const height = Math.max(Math.abs(y - zeroY), 1)
    return {
      x: i * (BAR_WIDTH + GAP),
      y: top,
      height,
      point,
    }
  })
  return { bars, zeroY }
}

const tooltip = reactive<{ visible: boolean; x: number; y: number; period: string; value: string }>({
  visible: false,
  x: 0,
  y: 0,
  period: '',
  value: '',
})

function showTooltip(evt: MouseEvent | FocusEvent, point: MetricPoint) {
  const target = evt.currentTarget as HTMLElement
  const rect = target.closest('.fc-chart')?.getBoundingClientRect()
  if (!rect) return
  const barRect = target.getBoundingClientRect()
  tooltip.visible = true
  tooltip.x = barRect.left - rect.left + barRect.width / 2
  tooltip.y = barRect.top - rect.top
  tooltip.period = point.period
  tooltip.value = `${formatEok(point.amountEok)}억원`
}

function hideTooltip() {
  tooltip.visible = false
}
</script>

<template>
  <div class="fc-root">
    <p v-if="!hasAnyData" class="text-sm text-slate-400">재무 데이터가 없습니다.</p>

    <div v-else class="grid grid-cols-1 gap-6 sm:grid-cols-3">
      <div v-for="metric in metrics" :key="metric.key" class="relative">
        <div class="mb-2 flex items-center gap-1.5 text-sm font-medium text-slate-700">
          <span
            class="inline-block h-2.5 w-2.5 rounded-full"
            :style="{ backgroundColor: `var(${metric.seriesVar})` }"
          />
          {{ metric.label }}
        </div>

        <div v-if="metric.points.length === 0" class="text-xs text-slate-400">데이터 없음</div>

        <div v-else class="fc-chart relative">
          <svg
            :viewBox="`0 0 ${metric.points.length * (BAR_WIDTH + GAP) - GAP} ${CHART_HEIGHT}`"
            class="w-full"
            :style="{ height: `${CHART_HEIGHT}px` }"
          >
            <line
              :x1="0"
              :x2="metric.points.length * (BAR_WIDTH + GAP) - GAP"
              :y1="barGeometry(metric.points).zeroY"
              :y2="barGeometry(metric.points).zeroY"
              class="fc-baseline"
            />
            <g v-for="bar in barGeometry(metric.points).bars" :key="bar.point.period">
              <rect
                :x="bar.x"
                :y="bar.y"
                :width="BAR_WIDTH"
                :height="bar.height"
                rx="4"
                :style="{ fill: `var(${metric.seriesVar})` }"
                tabindex="0"
                role="img"
                :aria-label="`${bar.point.period} ${formatEok(bar.point.amountEok)}억원`"
                @pointermove="showTooltip($event, bar.point)"
                @pointerleave="hideTooltip"
                @focus="showTooltip($event, bar.point)"
                @blur="hideTooltip"
              />
              <text
                :x="bar.x + BAR_WIDTH / 2"
                :y="bar.point.amountEok >= 0 ? bar.y - 4 : bar.y + bar.height + 12"
                text-anchor="middle"
                class="fc-value-label"
              >
                {{ formatEok(bar.point.amountEok) }}
              </text>
              <text
                :x="bar.x + BAR_WIDTH / 2"
                :y="CHART_HEIGHT - 2"
                text-anchor="middle"
                class="fc-axis-label"
              >
                {{ bar.point.period }}
              </text>
            </g>
          </svg>

          <div
            v-if="tooltip.visible"
            class="fc-tooltip"
            :style="{ left: `${tooltip.x}px`, top: `${tooltip.y}px` }"
          >
            <strong>{{ tooltip.value }}</strong>
            <span>{{ tooltip.period }}</span>
          </div>
        </div>
      </div>
    </div>

    <details v-if="hasAnyData" class="mt-4 text-sm">
      <summary class="cursor-pointer text-slate-500">표로 보기</summary>
      <table class="mt-2 w-full text-sm">
        <thead class="text-left text-slate-500">
          <tr>
            <th class="py-1 pr-4 font-medium">계정</th>
            <th v-for="p in metrics[0].points" :key="p.period" class="py-1 pr-4 font-medium">
              {{ p.period }}
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="metric in metrics" :key="metric.key">
            <td class="py-1.5 pr-4 text-slate-600">{{ metric.label }}</td>
            <td v-for="p in metric.points" :key="p.period" class="py-1.5 pr-4 tabular-nums">
              {{ formatEok(p.amountEok) }}억원
            </td>
          </tr>
        </tbody>
      </table>
    </details>
  </div>
</template>

<style scoped>
.fc-root {
  --surface-1: #fcfcfb;
  --text-secondary: #52514e;
  --text-muted: #898781;
  --gridline: #e1e0d9;
  --series-1: #2a78d6;
  --series-2: #eb6834;
  --series-3: #1baf7a;
}
@media (prefers-color-scheme: dark) {
  :root:where(:not([data-theme='light'])) .fc-root {
    --surface-1: #1a1a19;
    --text-secondary: #c3c2b7;
    --text-muted: #898781;
    --gridline: #2c2c2a;
    --series-1: #3987e5;
    --series-2: #d95926;
    --series-3: #199e70;
  }
}
:root[data-theme='dark'] .fc-root {
  --surface-1: #1a1a19;
  --text-secondary: #c3c2b7;
  --text-muted: #898781;
  --gridline: #2c2c2a;
  --series-1: #3987e5;
  --series-2: #d95926;
  --series-3: #199e70;
}

.fc-baseline {
  stroke: var(--gridline);
  stroke-width: 1;
}
.fc-value-label {
  font-size: 10px;
  fill: var(--text-secondary);
}
.fc-axis-label {
  font-size: 10px;
  fill: var(--text-muted);
}
.fc-tooltip {
  position: absolute;
  transform: translate(-50%, -100%) translateY(-6px);
  background: var(--surface-1);
  border: 1px solid var(--gridline);
  border-radius: 6px;
  padding: 4px 8px;
  font-size: 12px;
  display: flex;
  flex-direction: column;
  gap: 2px;
  pointer-events: none;
  white-space: nowrap;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}
.fc-tooltip strong {
  color: var(--text-secondary);
}
.fc-tooltip span {
  color: var(--text-muted);
}
rect:focus-visible {
  outline: 2px solid var(--series-1);
  outline-offset: 2px;
}
</style>
