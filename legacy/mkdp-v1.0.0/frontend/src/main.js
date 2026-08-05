import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueRouter from 'vue-router'


Vue.use(ElementUI);
Vue.use(BootstrapVue);
// 플러그인 형태의 VueRouter 등록
Vue.use(VueRouter);
Vue.config.productionTip = false

// 라우터 객체 생성
const router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: "/",
      component: () => import("./components/IndexHome.vue"),
      children: [
        {
          path: "contact",
          component: () => import("./components/ContactPage.vue"),
        },
        {
          path: "companyOverview",
          component: () => import("./components/companyOverview.vue"),
        },
        {
          path: "companyCropCode",
          component: () => import("./components/companyCropCode.vue"),
        },
        {
          path: "footer",
          component: () => import("./components/mainFooter.vue"),
        },
      ],
    },
    {
      path: "/ContactPage",
      component: () => import("./components/ContactPage.vue"),
    },
    {
      path: "/portfolio",
      component: () => import("./components/portfolio.vue"),
    },

  ],
});

new Vue({
	router,// 라우터 등록
	render: h => h(App),
}).$mount('#app')