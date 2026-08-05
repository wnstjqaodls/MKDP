 <style>
@import "../assets/css/bootstrap.min.css";
@import "../assets/css/signin.css";

.bd-placeholder-img {
  font-size: 1.125rem;
  text-anchor: middle;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
}

@media (min-width: 768px) {
  .bd-placeholder-img-lg {
    font-size: 3.5rem;
  }
}
</style>

<template>
  <main class="form-signin">
    <form @submit.prevent="sendLoginRequest">
      <!-- submit 이벤트에 sendLoginRequest 메서드 바인딩 -->
      <img
        class="ml-4"
        src="../assets/mkdp-logo-700.jpg"
        alt=""
        width="132"
        height="97"
      />
      <h1 class="h3 mb-3 fw-normal">Login to Market Data Provider</h1>

      <div class="form-floating">
        <input
          type="email"
          class="form-control"
          id="floatingInput"
          placeholder="name@example.com"
          v-model="email"
        />
        <label for="floatingInput">Email address</label>
      </div>
      <div class="form-floating">
        <input
          type="password"
          class="form-control"
          id="floatingPassword"
          placeholder="Password"
          v-model="password"
        />
        <label for="floatingPassword">Password</label>
      </div>

      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" value="remember-me" /> Remember me
        </label>
      </div>
      <button class="w-100 btn btn-lg btn-primary" type="submit">
        Sign in
      </button>
      <p class="mt-5 mb-3 text-muted">&copy; 2017–2021</p>
    </form>
  </main>
</template>

<script>
import { MKDP_HTTP } from "../http/axiosUtil";
import sessionManager from "../store/sessionManager";

export default {
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    sendLoginRequest() {
      const requestData = {
        email: this.email,
        password: this.password,
        //sessId: sessionManager.getSessionData().sessId,
      };

      MKDP_HTTP.MKDP_Request("login", requestData)
        .then((response) => {
          alert("데이터구조" + JSON.stringify(response));
          if (response.loginCheck) {
            // 세션 데이터 저장 등의 로직
            // 로그인 성공 시 세션에 데이터 저장
            sessionManager.saveSessionData(response);
            // 로그인 성공후 메인화면 루트 경로('/')로 라우팅
            // 로그인 테스트 계정정보  id :  slydiard3b@mit.edu       pw : 1
            alert("성공!!!" + sessionManager.getSessionData());

            this.$router.push("/");
          } else {
            // 로그인 실패 처리
            alert("Login failed:", response.message);
            // 필요에 따라 사용자에게 실패 메시지를 표시할 수 있습니다.
          }
        })
        .catch((error) => {
          console.error("Login failed:", error);
        });

      // 폼 필드 초기화
      this.email = "";
      this.password = "";
    },
  },
};
</script>


 