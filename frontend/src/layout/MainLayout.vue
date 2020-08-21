<template>
  <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
    <!-- <side-bar
      :background-color="sidebarBackground"
      short-title="편집"
      title="편집"
    >
      <template slot="links">
        <sidebar-item
          :link="{
            name: '메인',
            icon: 'ni ni-tv-2 text-primary',
            path: '/mainpage',
          }"
        />

        <sidebar-item
          :link="{
            name: '편집자 찾기',
            icon: 'ni ni-zoom-split-in text-primary',
            path: '/editors',
          }"
        />
        <sidebar-item
          v-if="isEditor && isLogin"
          :link="{
            name: '포트폴리오',
            icon: 'ni ni-collection text-primary',
            path: '/portfolio?no=' + this.$session.get('uid'),
          }"
        />
        <sidebar-item
          v-if="isLogin"
          :link="{
            name: '찜',
            icon: 'ni ni-favourite-28 text-red',
            path: '/marklist',
          }"
        />
        <sidebar-item
          v-if="isLogin"
          :link="{
            name: '알람',
            icon: 'ni ni-bell-55 text-primary',
            path: '/alarm',
          }"
        />
      </template>
    </side-bar>-->
    <!-- <div class="main-content" :data="sidebarBackground" @mousedown="hide(sidebar)"> -->
    <div class="main-content" :data="sidebarBackground">
      <main-navbar @mousedown.stop></main-navbar>

      <div>
        <fade-transition :duration="200" origin="center top" mode="out-in">
          <!-- your content here -->
          <router-view></router-view>
        </fade-transition>
        <div>
          <!-- <i class="arrow ni ni-bold-up" @click="scrollToTop"></i> -->
          <div class="arrow" @click="scrollToTop"></div>
        </div>
        <content-footer v-if="!$route.meta.hideFooter"></content-footer>
      </div>
    </div>
  </div>
</template>
<script>
import MainNavbar from "./MainNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import { FadeTransition } from "vue2-transitions";

export default {
  components: {
    MainNavbar,
    ContentFooter,
    FadeTransition,
  },
  watch: {
    // session이 바뀔때마다 로그인 상태인지 확인
    $route() {
      if (this.$session.exists()) {
        this.isLogin = true;
        this.isEditor = this.$session.get("auth") == "editor";
        // console.log(this.$session.get("auth"))
        // console.log(this.$session.get("nickname"))
      } else {
        this.isLogin = false;
      }
    },
  },
  data() {
    return {
      sidebarBackground: "vue", //vue|blue|orange|green|red|primary
      isLogin: false,
      isEditor: false,

      collapsed: false,
    };
  },
  created() {
    if (this.$session.exists()) {
      this.isLogin = true;
      this.isEditor = this.$session.get("auth") == "editor";
      // console.log(this.$session.get("auth"))
      // console.log(this.$session.get("nickname"))
    } else {
      this.isLogin = false;
    }
  },
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
    scrollToTop() {
      window.scrollTo(0, 0);
    },
  },
};
</script>
<style lang="scss">
html {
  scroll-behavior: smooth;
}

div.arrow {
  width: 6vmin;
  height: 6vmin;
  position: fixed;
  right: 50px;
  bottom: 80px;
  transition: all 0.5s ease-in-out;
  opacity: 0.6;
  transform: rotate(-45deg);

  &::before {
    content: "";
    width: 100%;
    height: 100%;
    border-width: 0.8vmin 0.8vmin 0 0;
    border-style: solid;
    border-color: #172b4d;
    transition: 0.2s ease;
    display: block;
    transform-origin: 90% 0;
  }

  &:after {
    content: "";
    float: left;
    position: relative;
    top: -100%;
    width: 100%;
    height: 100%;
    border-width: 0 0.8vmin 0 0;
    border-style: solid;
    border-color: #172b4d;
    transform-origin: 80% 0;
    transition: 0.2s ease;
  }

  &:hover::after {
    transform: rotate(45deg);
    border-color: white;
    height: 140%;
    cursor: pointer;
  }
  &:hover::before {
    border-color: white;
    transform: scale(0.8);
    cursor: pointer;
  }
}
body {
  overflow-x: hidden;
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}
</style>
