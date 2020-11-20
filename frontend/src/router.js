import Vue from "vue";
import Router from "vue-router";
// import DashboardLayout from '@/layout/DashboardLayout'
import MainLayout from "@/layout/MainLayout";
import AuthLayout from "@/layout/AuthLayout";
Vue.use(Router);

//로그인이 되어 있지 않다면 접근 할 수 없다.
const requireLogin = (to, from, next) => {
  if (Vue.prototype.$session.exists()) return next();
  next({
    name: "login",
  });
};

//로그인이 되어 있다면 접근 할 수 없다.
const requireUnLogin = (to, from, next) => {
  if (!Vue.prototype.$session.exists()) return next();
  next({
    name: "mainpage"
  })
};

//editor가 아니라면 접근 할 수 없다.
const requireAuth = (to, from, next) => {
  if (
    Vue.prototype.$session.exists() &&
    Vue.prototype.$session.get("auth") == "editor"
  )
    return next();
  next({
    path: "/",
  });
};

export default new Router({
  linkExactActiveClass: "active",
  routes: [
    {
      path: "/",
      redirect: "mainpage",
      component: MainLayout,
      children: [
        {
          path: "/mainpage",
          name: "mainpage",
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/MainPage.vue"),
        },
        {
          path: "/search",
          name: "search",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/Stepper/Stepper.vue"),
        },
        {
          path: "/editors",
          name: "editors",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/Editors.vue"),
            props: true,
        },
        {
          path: "/marklist",
          name: "marklist",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/MarkList.vue"),
          beforeEnter: requireLogin,
        },
        {
          path: "/alarm",
          name: "alarm",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/Alarm.vue"),
          beforeEnter: requireLogin,
        },

        {
          path: "/about",
          name: "AboutUs",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/AboutUs.vue"),
        },
      ],
    },
    {
      path: "/portfolio",
      redirect: "portfolio",
      component: MainLayout,
      children: [
        {
          path: "",
          name: "portfolio",
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () =>
            import(
              /* webpackChunkName: "demo" */ "./views/Portfolio/Portfolio_Main.vue"
            ),
          // beforeEnter: requireLogin
        },
        {
          path: "/portfolio/edit",
          name: "portfolio_edit",
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () =>
            import(
              /* webpackChunkName: "demo" */ "./views/Portfolio/Portfolio_Edit.vue"
            ),
          beforeEnter: requireAuth,
        },
      ],
    },
    {
      path: "/user",
      redirect: "login",
      component: AuthLayout,
      children: [
        {
          path: "/login",
          name: "login",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/Login.vue"),
            beforeEnter: requireUnLogin,
        },
        {
          path: "/register",
          name: "register",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/Register.vue"),
            beforeEnter: requireUnLogin,
        },
        {
          path: "/profile",
          name: "profile",
          component: () =>
            import(/* webpackChunkName: "demo" */ "./views/UserProfile.vue"),
            beforeEnter: requireLogin,
        },
      ],
    },
    {
      path: "*",
      // component: MainLayout,
      component: () => import("@/views/Error/NotFoundPage"),
    },
  ],
});
