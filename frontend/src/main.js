import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "./registerServiceWorker";
import EditHouse from "./plugins/editHouse";
import "vue-lazy-youtube-video/dist/style.css";
import VCalendar from "v-calendar";
import store from "@/store/store.js";
import VueSession from "vue-session";
import VueCookies from "vue-cookies";

// for google Login
import GAuth from "vue-google-oauth2";
Vue.use(GAuth, {
  clientId:
    "869231689869-54urhupkc5n8lpbrv5pruplbj0tdgtlu.apps.googleusercontent.com",
  scope: "profile email https://www.googleapis.com/auth/plus.login",
});

import StarRating from "vue-star-rating";
window.Kakao.init("435fabf554fd7f76ef38eeb97c6c7e50");

Vue.use(VueCookies);
Vue.use(VueSession);
Vue.use(VCalendar, {
  componentPrefix: "vc",
});

var sessionOptions = {
  persist: true,
};
Vue.use(VueSession, sessionOptions);

Vue.use(VueCookies);
Vue.config.productionTip = false;

Vue.component("star-rating", StarRating);

Vue.use(EditHouse);
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
