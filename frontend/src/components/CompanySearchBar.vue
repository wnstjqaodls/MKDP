<script setup lang="ts">
import { ref } from 'vue'
import { useDebouncedFn } from '@/composables/useDebouncedFn'

const props = withDefaults(defineProps<{ initialQuery?: string; debounceMs?: number }>(), {
  initialQuery: '',
  debounceMs: 300,
})
const emit = defineEmits<{ search: [query: string] }>()

const query = ref(props.initialQuery)
const debouncedEmit = useDebouncedFn((value: string) => emit('search', value), props.debounceMs)

function onInput() {
  debouncedEmit(query.value)
}
</script>

<template>
  <div class="relative">
    <input
      v-model="query"
      type="text"
      placeholder="기업명 또는 종목코드 검색"
      class="w-full rounded-lg border border-slate-300 px-4 py-2.5 text-sm shadow-sm focus:border-slate-400 focus:outline-none"
      @input="onInput"
      @keyup.enter="emit('search', query)"
    />
  </div>
</template>
