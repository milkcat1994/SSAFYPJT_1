/*!

=========================================================
* Vue Argon Dashboard - v1.0.0
=========================================================

* Product Page: https://www.creative-tim.com/product/argon-dashboard
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/argon-dashboard/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "./registerServiceWorker";
import ArgonDashboard from "./plugins/argon-dashboard";
import "vue-lazy-youtube-video/dist/style.css";
import VCalendar from 'v-calendar';
import store from "@/store/store.js";
import VueSession from "vue-session";
import VueCookies from "vue-cookies";

Vue.use(VueCookies);
Vue.use(VueSession);
Vue.use(VCalendar, {
  componentPrefix: 'vc'
});

var sessionOptions = {
  persist: true
}
Vue.use(VueSession, sessionOptions);

Vue.use(VueCookies);
Vue.config.productionTip = false;

Vue.use(ArgonDashboard);
new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount("#app");
