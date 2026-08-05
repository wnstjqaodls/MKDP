<script setup lang="ts">
import { computed, reactive } from 'vue'
import type { BacktestPoint } from '@/types/portfolio'

const props = defineProps<{ series: BacktestPoint[] }>()

const WIDTH = 640
const HEIGHT = 220
const PAD_LEFT = 56
const PAD_RIGHT = 16
const PAD_TOP = 16
const PAD_BOTTOM = 28

function formatWon(value: number): string {
  return `${Math.round(value).toLocaleString('ko-KR')}원`
}

function formatDate(iso: string): string {
  const [y, m, d] = iso.split('-')
  return `${y}.${m}.${d}`
}

const geometry = computed(() => {
  const points = props.series
  if (points.length === 0) return { path: '', dots: [], yTicks: [], xTicks: [] }

  const values = points.map((p) => p.value)
  const minValue = Math.min(...values)
  const maxValue = Math.max(...values)
  const range = maxValue - minValue || 1
  const plotWidth = WIDTH - PAD_LEFT - PAD_RIGHT
  const plotHeight = HEIGHT - PAD_TOP - PAD_BOTTOM

  const x = (i: number) => PAD_LEFT + (i / Math.max(points.length - 1, 1)) * plotWidth
  const y = (v: number) => PAD_TOP + (1 - (v - minValue) / range) * plotHeight

  const path = points.map((p, i) => `${i === 0 ? 'M' : 'L'} ${x(i).toFixed(1)} ${y(p.value).toFixed(1)}`).join(' ')

  const yTickCount = 4
  const yTicks = Array.from({ length: yTickCount + 1 }, (_, i) => {
    const value = minValue + (range * i) / yTickCount
    return { value, y: y(value) }
  })

  const xTickIdx = [0, Math.floor((points.length - 1) / 2), points.length - 1]
  const xTicks = [...new Set(xTickIdx)].map((i) => ({ label: formatDate(points[i].date), x: x(i) }))

  return {
    path,
    dots: points.map((p, i) => ({ x: x(i), y: y(p.value), point: p })),
    yTicks,
    xTicks,
  }
})

const tooltip = reactive<{ visible: boolean; x: number; y: number; date: string; value: string }>({
  visible: false,
  x: 0,
  y: 0,
  date: '',
  value: '',
})
const crosshairX = reactive<{ value: number }>({ value: 0 })

function showTooltip(evt: PointerEvent) {
  const svg = (evt.currentTarget as SVGSVGElement).getBoundingClientRect()
  const relativeX = ((evt.clientX - svg.left) / svg.width) * WIDTH
  const dots = geometry.value.dots
  if (dots.length === 0) return
  let nearest = dots[0]
  let nearestDist = Math.abs(dots[0].x - relativeX)
  for (const dot of dots) {
    const dist = Math.abs(dot.x - relativeX)
    if (dist < nearestDist) {
      nearest = dot
      nearestDist = dist
    }
  }
  crosshairX.value = nearest.x
  tooltip.visible = true
  tooltip.x = (nearest.x / WIDTH) * svg.width
  tooltip.y = (nearest.y / HEIGHT) * svg.height
  tooltip.date = formatDate(nearest.point.date)
  tooltip.value = formatWon(nearest.point.value)
}

function hideTooltip() {
  tooltip.visible = false
}
</script>

<template>
  <div class="pvc-root relative">
    <svg
      :viewBox="`0 0 ${WIDTH} ${HEIGHT}`"
      class="w-full"
      :style="{ height: `${HEIGHT}px` }"
      @pointermove="showTooltip"
      @pointerleave="hideTooltip"
    >
      <line
        v-for="tick in geometry.yTicks"
        :key="tick.value"
        :x1="PAD_LEFT"
        :x2="WIDTH - PAD_RIGHT"
        :y1="tick.y"
        :y2="tick.y"
        class="pvc-gridline"
      />
      <text v-for="tick in geometry.yTicks" :key="`label-${tick.value}`" :x="PAD_LEFT - 8" :y="tick.y + 3" text-anchor="end" class="pvc-axis-label">
        {{ Math.round(tick.value / 10000).toLocaleString('ko-KR') }}만
      </text>
      <text v-for="tick in geometry.xTicks" :key="tick.label" :x="tick.x" :y="HEIGHT - 6" text-anchor="middle" class="pvc-axis-label">
        {{ tick.label }}
      </text>

      <path :d="geometry.path" fill="none" class="pvc-line" />

      <circle
        v-if="geometry.dots.length"
        :cx="geometry.dots[geometry.dots.length - 1].x"
        :cy="geometry.dots[geometry.dots.length - 1].y"
        r="4"
        class="pvc-end-dot"
      />
      <line
        v-if="tooltip.visible"
        :x1="crosshairX.value"
        :x2="crosshairX.value"
        :y1="PAD_TOP"
        :y2="HEIGHT - PAD_BOTTOM"
        class="pvc-crosshair"
      />
    </svg>

    <div v-if="tooltip.visible" class="pvc-tooltip" :style="{ left: `${tooltip.x}px`, top: `${tooltip.y}px` }">
      <strong>{{ tooltip.value }}</strong>
      <span>{{ tooltip.date }}</span>
    </div>
  </div>
</template>

<style scoped>
.pvc-root {
  --text-secondary: #52514e;
  --text-muted: #898781;
  --gridline: #e1e0d9;
  --series-1: #2a78d6;
  --surface-1: #fcfcfb;
}
@media (prefers-color-scheme: dark) {
  :root:where(:not([data-theme='light'])) .pvc-root {
    --text-secondary: #c3c2b7;
    --text-muted: #898781;
    --gridline: #2c2c2a;
    --series-1: #3987e5;
    --surface-1: #1a1a19;
  }
}
:root[data-theme='dark'] .pvc-root {
  --text-secondary: #c3c2b7;
  --text-muted: #898781;
  --gridline: #2c2c2a;
  --series-1: #3987e5;
  --surface-1: #1a1a19;
}

.pvc-gridline {
  stroke: var(--gridline);
  stroke-width: 1;
}
.pvc-axis-label {
  font-size: 10px;
  fill: var(--text-muted);
}
.pvc-line {
  stroke: var(--series-1);
  stroke-width: 2;
  stroke-linejoin: round;
  stroke-linecap: round;
}
.pvc-end-dot {
  fill: var(--series-1);
  stroke: var(--surface-1);
  stroke-width: 2;
}
.pvc-crosshair {
  stroke: var(--text-muted);
  stroke-width: 1;
  stroke-dasharray: 2 2;
}
.pvc-tooltip {
  position: absolute;
  transform: translate(-50%, -100%) translateY(-10px);
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
.pvc-tooltip strong {
  color: var(--text-secondary);
}
.pvc-tooltip span {
  color: var(--text-muted);
}
</style>
