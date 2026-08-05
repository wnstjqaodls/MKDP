import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import CompanySearchView from '@/views/CompanySearchView.vue'
import CompanyDetailView from '@/views/CompanyDetailView.vue'
import PortfolioView from '@/views/PortfolioView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/companies', name: 'company-search', component: CompanySearchView },
    {
      path: '/companies/:corpCode',
      name: 'company-detail',
      component: CompanyDetailView,
      props: true,
    },
    { path: '/portfolio', name: 'portfolio', component: PortfolioView },
    { path: '/backtest', redirect: '/portfolio' },
  ],
})

export default router
