<script setup>
import { ref } from 'vue';
import { MKDP_HTTP } from '../http/axiosUtil'; // Axios 모듈의 경로를 확인해주세요

const companyOverviewData = ref(null);

function getCorpCode() {
  MKDP_HTTP.MKDP_Request('/api/updateCorpCodes', {
    method : 'POST'
  }) // 두 번째 인자로 필요한 파라미터를 전달
      .then(data => {
        companyOverviewData.value = data;
      })
      .catch(error => {
        console.error('Failed to fetch data:', error);
      });
}
</script>

<template>
  <div class="row g-3">
    <div class="col-lg-12">
      <button @click="getCorpCode" class="w-100 btn btn-primary btn-lg">Get Corp Code</button>&nbsp;
    </div>
    <div v-if="companyOverviewData">
      {{ companyOverviewData }}
    </div>
  </div>
</template>
