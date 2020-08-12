<template>
  <base-nav class="navbar-top navbar-dark" id="navbar-main" :show-toggle-button="false" expand>
    <!-- <form class="navbar-search navbar-search-dark form-inline mr-3 d-none d-md-flex ml-lg-auto">
      <div class="form-group mb-0">
        <base-input
          placeholder="Search"
          class="input-group-alternative"
          alternative
          addon-right-icon="fas fa-search"
        ></base-input>
      </div>
    </form>-->

    <!-- 최근 메시지 보여주기 -->
    <ul class="navbar-nav align-items-center mr-4 d-none d-md-flex ml-lg-auto">
      <li class="nav-item dropdown" v-if="isLogin">
        <base-dropdown class="nav-link pr-0" position="right">
          <div class="align-items-center text-default" slot="title">
            <i class="ni ni-bell-55"></i>
            <badge type="text-light">{{ getNotifyNum() }}</badge>
          </div>

          <!-- 레이아웃 width가 md 이하일때는 @/components/Sidebar.vue 수정해야함 -->
          <template>
            <div class="dropdown-header noti-title">
              <h6 class="text-overflow m-0">최근 매칭 내역</h6>
            </div>
            <div
              v-for="(notifyitem, index) in notifyitems.object"
              :key="index + '_notifyitems'"
              @click="readNotify()"
            >
              <router-link to="/alarm" class="dropdown-item">
                <i class="ni ni-bulb-61 text-yellow"></i>
                <span v-if="notifyitem.notify_type == 'request'">
                  {{ notifyitem.request_nickname }}님께서 영상편집을
                  요청하셨습니다
                </span>
                <span v-if="notifyitem.notify_type == 'accepted'">
                  {{ notifyitem.request_nickname }}님께서 요청을
                  수락하셨습니다
                </span>
              </router-link>
            </div>
            <div class="dropdown-divider"></div>

            <!-- 더보기 -->
            <div @click="readNotify()">
              <router-link to="/alarm" class="dropdown-item">
                <i class="ni ni-fat-add"></i>
                <span>더보기</span>
              </router-link>
            </div>
          </template>
        </base-dropdown>
      </li>
    </ul>

    <ul class="navbar-nav">
      <li v-if="!isLogin" class="nav-item mr-3">
        <router-link :to="{ name: 'register' }" class="nav-link">
          <i class="fas fa-user-plus"></i>
          <span class="nav-link-inner--text">회원가입</span>
        </router-link>
      </li>
      <li v-if="!isLogin" class="nav-item">
        <router-link :to="{ name: 'login' }" class="nav-link">
          <i class="fas fa-user-check"></i>
          <span class="nav-link-inner--text">로그인</span>
        </router-link>
      </li>
    </ul>

    <!-- 마이페이지 -->
    <ul v-if="isLogin" class="navbar-nav align-items-center ml-2 d-none d-md-flex">
      <li class="nav-item dropdown">
        <base-dropdown class="nav-link pr-0" position="right">
          <div class="media align-items-center" slot="title">
            <span class="avatar avatar-sm rounded-circle">
              <img alt="Image placeholder" src="img/theme/avatar.svg" />
            </span>
            <div class="media-body ml-2 d-none d-lg-block">
              <span class="mb-0 text-sm font-weight-bold text-default">{{nickname}}</span>
            </div>
          </div>

          <template>
            <div class="dropdown-header noti-title">
              <h6 class="text-overflow m-0">환영합니다!</h6>
            </div>
            <router-link :to="'/profile'" class="dropdown-item">
              <i class="ni ni-single-02"></i>
              <span>내 정보</span>
            </router-link>
            <!-- <router-link to="/404" class="dropdown-item">
              <i class="ni ni-settings-gear-65"></i>
              <span>계정 설정</span>
            </router-link>
            <router-link to="/404" class="dropdown-item">
              <i class="ni ni-calendar-grid-58"></i>
              <span>작업 일정</span>
            </router-link>-->
            <router-link to="/404" class="dropdown-item">
              <i class="ni ni-support-16"></i>
              <span>도움말</span>
            </router-link>
            <div class="dropdown-divider"></div>
            <span style="cursor:pointer;" class="dropdown-item" @click="logout()">
              <i class="ni ni-lock-circle-open"></i>
              <span>로그아웃</span>
            </span>
          </template>
        </base-dropdown>
      </li>
    </ul>
  </base-nav>
</template>

<script>
import http from "@/util/http-common";
import store from "@/store/store.js";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      activeNotifications: false,
      showMenu: false,
      searchQuery: "",
      notifyNum: "",

      isLogin: false,
      // nickname: "",
    };
  },
  created() {
    //생성 시 로그인 상태 확인
    if (this.$session.exists()) {
      this.isLogin = true;
      // this.nickname = this.$session.get("nickname");
      // 로그인이 되어있으면 알림 가져옴
      store.dispatch(
        "getNotifyitems",
        "/request/notify/" + this.$session.get("nickname")
      );
    } else {
      this.isLogin = false;
    }
    // 알림 받아옴
  },
  computed: {
    ...mapGetters(["notifyitems"]),
    ...mapGetters(["notifyitem"]),
    nickname() {
      return this.$session.get("nickname");
    },
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
  methods: {
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    },
    toggleMenu() {
      this.showMenu = !this.showMenu;
    },
    getNotifyNum() {
      this.notifyNum = this.notifyitems.data;
      return this.notifyNum;
    },
    // 요청 읽음
    readNotify() {
      http
        .put("/request/notify/read/" + this.$session.get("nickname"))
        .then(({ data }) => {
          if (data == "success") {
            // alert('알람 읽음 완료');
          }
        })
        .catch(() => {
          // alert('요청 거절중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          store.dispatch(
            "getNotifyitems",
            "/request/notify/" + this.$session.get("nickname")
          );
        });
    },

    logout() {
      this.$session.destroy();
      store.dispatch("initDataOnAlarm");
      this.isLogin = false;
      this.$router.push("/").catch(() => {});
    },
  },
};
</script>
<style>
.nav-item:hover {
  cursor: pointer;
}
</style>
