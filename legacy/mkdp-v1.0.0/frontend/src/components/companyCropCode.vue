<template>
  <div>
    기업 코드 정보 데이터
    {{ someData }}
  </div>
</template>
 
<script>
import axios from 'axios';
 
export default {
  name: 'companyCropCode',
  data() {
    return {
      someData: 1
    };
  },
   props: {

  },
  created() {
    this.getcompanyCropCode();
  },
  methods: {
    getcompanyCropCode() {
      axios
        .get('http://localhost:8050/mkdpApp/companyCropCode')
        .then((response) => {
          if (response.data.success) {
            console.log(response.data.result.result);
            this.companyCropCodeData = response.data.result.result;
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },
  },
};
</script>
 
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>