<template>
  <div id="app"> 
    <div>
      <!-- 컴포넌트가 라우팅 되는 위치  -->
      <router-view></router-view>
      <!-- <portfolio></portfolio> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
   
    //왼쪽에는 아무렇게나 이름을 지어도 된다. 이 화면에서 사용할 이름을 짓는 부분

}

</script>

<style>
#app { 
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}
</style>
