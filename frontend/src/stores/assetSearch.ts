import { defineStore } from 'pinia'
import { assetApi, extractErrorMessage } from '@/api/client'
import type { AssetSummary } from '@/types/portfolio'

interface AssetSearchState {
  query: string
  results: AssetSummary[]
  searching: boolean
  error: string | null
}

export const useAssetSearchStore = defineStore('assetSearch', {
  state: (): AssetSearchState => ({
    query: '',
    results: [],
    searching: false,
    error: null,
  }),

  actions: {
    async search(query: string) {
      this.query = query
      if (!query.trim()) {
        this.results = []
        this.error = null
        return
      }
      this.searching = true
      this.error = null
      try {
        this.results = await assetApi.search(query)
      } catch (err) {
        this.error = extractErrorMessage(err)
        this.results = []
      } finally {
        this.searching = false
      }
    },
  },
})
