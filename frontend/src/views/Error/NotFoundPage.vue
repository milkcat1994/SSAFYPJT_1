<template>
  <div class="main-content bg-default">
    <!-- Navbar -->
    <base-nav
      class="navbar-top navbar-horizontal navbar-dark"
      containerClasses="px-4 container"
      expand
    >
      <router-link slot="brand" class="navbar-brand" to="/">
        <img src="img/brand/white.png" />
      </router-link>

      <template v-slot="{ closeMenu }">
        <!-- Collapse header -->
        <div class="navbar-collapse-header d-md-none">
          <div class="row">
            <div class="col-6 collapse-brand">
              <router-link to="/">
                <img src="img/brand/green.png" />
              </router-link>
            </div>
            <div class="col-6 collapse-close">
              <button
                type="button"
                @click="closeMenu"
                class="navbar-toggler"
                aria-label="Toggle sidenav"
              >
                <span></span>
                <span></span>
              </button>
            </div>
          </div>
        </div>
        <!-- Navbar items -->
        <ul class="navbar-nav ml-auto">
          <li class="nav-item">
            <router-link class="nav-link nav-link-icon" to="/">
              <i class="ni ni-planet"></i>
              <span class="nav-link-inner--text">메인</span>
            </router-link>
          </li>
          <li v-if="!isLogin" class="nav-item">
            <router-link class="nav-link nav-link-icon" to="/user">
              <i class="fas fa-user-plus"></i>
              <span class="nav-link-inner--text">회원가입</span>
            </router-link>
          </li>
          <li v-if="!isLogin" class="nav-item">
            <router-link class="nav-link nav-link-icon" to="/login">
              <i class="fas fa-user-check"></i>
              <span class="nav-link-inner--text">로그인</span>
            </router-link>
          </li>
          <li v-if="isLogin" class="nav-item">
            <router-link class="nav-link nav-link-icon" to="/profile">
              <i class="ni ni-single-02"></i>
              <span class="nav-link-inner--text">Profile</span>
            </router-link>
          </li>
          <li v-if="isLogin" class="nav-item">
            <span class="nav-link nav-link-icon" @click="logout()">
              <i class="ni ni-single-02"></i>
              <span>로그아웃</span>
            </span>
          </li>
        </ul>
      </template>
    </base-nav>
    <!-- Header -->
    <div class="header bg-gradient-success py-7 py-lg-8">
      <div class="container">
        <div class="header-body text-center mb-7">
          <div class="row justify-content-center">
            <div class="col-lg-5 col-md-6">
              <h1 class="text-white" style="font-weight:400; font-size:150px">404</h1>
              <h1 class="text-white">
                페이지를 찾을 수 없습니다.</h1>
                <p class="text-white">페이지가 삭제 되었거나 이름이 바뀌었거나 일시적으로 페이지를 이용할 수 없습니다.</p>
                <base-button @click="moveMain">메인</base-button>
            </div>
          </div>
        </div>
      </div>
      <div class="separator separator-bottom separator-skew zindex-100">
        <svg
          x="0"
          y="0"
          viewBox="0 0 2560 100"
          preserveAspectRatio="none"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
        >
          <polygon class="fill-default" points="2560 0 2560 100 0 100" />
        </svg>
      </div>
    </div>
    <!-- Page content -->
    <div class="container mt--8 pb-5">
      <slide-y-up-transition mode="out-in" origin="center top">
        <router-view></router-view>
      </slide-y-up-transition>
    </div>
    <footer class="py-5">
      <div class="container">
        <div class="row align-items-center justify-content-xl-between">
          <div class="col-xl-6">
            <div class="copyright text-center text-xl-left text-muted">
              &copy; {{ year }}
              <a
                href="https://www.creative-tim.com"
                class="font-weight-bold ml-1"
                target="_blank"
              >Creative Tim</a>
            </div>
          </div>
          <div class="col-xl-6">
            <ul class="nav nav-footer justify-content-center justify-content-xl-end">
              <li class="nav-item">
                <a href="https://www.creative-tim.com" class="nav-link" target="_blank">Creative Tim</a>
              </li>
              <li class="nav-item">
                <a
                  href="#"
                  class="nav-link"
                  target="_blank"
                >About Us</a>
              </li>
              <li class="nav-item">
                <a href="#" class="nav-link" target="_blank">Blog</a>
              </li>
              <li class="nav-item">
                <a
                  href="#"
                  class="nav-link"
                  target="_blank"
                >MIT License</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { SlideYUpTransition } from "vue2-transitions";

export default {
    name: "auth-layout",
    components: {
        SlideYUpTransition,
    },
    watch: {
        // route 경로가 바뀔때마다 로그인 상태인지 확인
        $route() {
        if (this.$session.exists()) {
            this.isLogin = true;
        } else {
            this.isLogin = false;
        }
        },
    },
    data() {
        return {
        year: new Date().getFullYear(),
        showMenu: false,
        isLogin:false,
        };
    },
    methods:{
        moveMain(){
            this.$router.push('/');
        }
    }
};
</script>

<style>
</style>