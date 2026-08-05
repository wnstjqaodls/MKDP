<template>
    <div class="portfolio-backtest">
      <h2>포트폴리오 백테스트</h2>
      
      <!-- 기간 설정 -->
      <div class="date-range">
        <date-picker v-model="startDate" placeholder="시작일" />
        <date-picker v-model="endDate" placeholder="종료일" />
      </div>
      
      <!-- 초기 투자금액 -->
      <div class="initial-amount">
        <input type="number" v-model="initialAmount" placeholder="초기 투자금액" />
      </div>
      
      <!-- 자산 배분 -->
      <div class="asset-allocation" v-for="(asset, index) in assets" :key="index">
        <company-search v-model="asset.companyCode" />
        <input type="number" v-model="asset.ratio" placeholder="배분 비율 (%)" />
      </div>
      
      <!-- 백테스트 실행 버튼 -->
      <button @click="runBacktest">백테스트 실행</button>
      
      <!-- 결과 표시 -->
      <div class="results" v-if="backtestResult">
        <div>최종 금액: {{ backtestResult.finalAmount }}</div>
        <div>총 수익률: {{ backtestResult.totalReturn }}%</div>
        <div>연간 수익률: {{ backtestResult.annualizedReturn }}%</div>
        <div>최대 낙폭: {{ backtestResult.maxDrawdown }}%</div>
        
        <!-- 차트 표시 -->
        <line-chart :data="backtestResult.dailyValues" />
      </div>
    </div>
  </template>