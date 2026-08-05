<template>
  <div>
    {{ memberList }}
    <!-- <el-table border :data="memberList">
      <el-table-column prop="kiwoom_id" label="키움아이디"></el-table-column>
      <el-table-column prop="kiwoom_account" label="키움계좌번호"></el-table-column>
      <el-table-column prop="mem_name" label="이름"></el-table-column>
    </el-table>       -->
  </div>
</template>
 
<script>
import axios from 'axios';
 
export default {
  name: 'MemberInfo',
  data() {
    return {
      memberList: [],
    };
  },
  props: {

  },
  created() {
    this.getMemberInfo();
  },
  methods: {
    getMemberInfo() {
      axios
        .get('http://localhost:8050/mkdpApp/')
        .then((response) => {
          if (response.data.success) {
            console.log(response.data.result);
            this.memberList = response.data.result;
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