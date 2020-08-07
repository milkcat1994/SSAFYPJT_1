<template>
  <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
    <side-bar
      :background-color="sidebarBackground"
      short-title="Argon"
      title="Argon"
    >
      <template slot="links">
        <sidebar-item
          :link="{
            name: '대시보드',
            icon: 'ni ni-tv-2 text-primary',
            path: '/dashboard',
          }"
        />

        <sidebar-item
          :link="{
            name: '아이콘',
            icon: 'ni ni-planet text-blue',
            path: '/icons',
          }"
        />
        <sidebar-item
          :link="{
            name: '포트폴리오',
            icon: 'ni ni-album-2 text-blue',
            path: '/portfolio',
          }"
        />
        <sidebar-item
          :link="{
            name: '내 정보',
            icon: 'ni ni-single-02 text-yellow',
            path: '/profile',
          }"
        />
        <sidebar-item
          :link="{
            name: '테이블',
            icon: 'ni ni-bullet-list-67 text-red',
            path: '/tables',
          }"
        />
        <sidebar-item
          :link="{
            name: '로그인',
            icon: 'ni ni-key-25 text-info',
            path: '/login',
          }"
        />
        <sidebar-item
          :link="{
            name: '회원가입',
            icon: 'ni ni-circle-08 text-pink',
            path: '/register',
          }"
        />
      </template>
    </side-bar>
    <div class="main-content" :data="sidebarBackground">
      <dashboard-navbar></dashboard-navbar>

      <div @click="toggleSidebar">
        <fade-transition :duration="200" origin="center top" mode="out-in">
          <!-- your content here -->
          <router-view></router-view>
        </fade-transition>
        <content-footer v-if="!$route.meta.hideFooter"></content-footer>
      </div>
    </div>
  </div>
</template>
<script>
import DashboardNavbar from "./DashboardNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import { FadeTransition } from "vue2-transitions";

export default {
  components: {
    DashboardNavbar,
    ContentFooter,
    FadeTransition,
  },
  data() {
    return {
      sidebarBackground: "vue", //vue|blue|orange|green|red|primary
    };
  },
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
  },
};
</script>
<style lang="scss"></style>
