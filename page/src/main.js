import Vue from 'vue'
import App from './App.vue'

import VueRouter from 'vue-router'
import routes from './router/routes'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI);
Vue.use(VueRouter);

//定义路由
const router = new VueRouter({
  mode:'history',
  routes
});
//定义拦截器
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    sessionStorage.removeItem('user');
  }
  let user = JSON.parse(sessionStorage.getItem('user'));
  if (!user && to.path !== '/login') {
    next({path: '/login'})
  } else {
    next()
  }
});

//设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  render: h => h(App),
  template: '<App/>',
  components: {App}
});
