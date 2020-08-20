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
    <template>
      <div>
        <i
          v-b-toggle.sidebar-footer
          class="hamburger fas fa-bars mr-2 p-3"
          @mouseup="maskon"
          style="border:none outline:0 width:100px"
        ></i>
        <b-sidebar
          id="sidebar-footer"
          aria-label="Sidebar with custom footer"
          no-header
          shadow
          class="sidebar-1"
          bg-variant="white"
          style="width:100px"
        >
          <template v-slot:footer="{ hide }" style="display:none width:100px">
            <div
              class="d-flex bg-dark text-light align-items-center px-3 py-2"
              style="display:none width:100px"
            >
              <b-button id="closebtn" size="sm" style="display:none;" @click="hide">Close</b-button>
            </div>
          </template>
          <div class="pl-2 py-2 d-flex justify-content-center">
            <router-link class="navbar-brand mb-3" to="/">
              <img
                src="/img/brand/green.png"
                class="navbar-brand-img my-4"
                alt="편집"
                style="width:110px; border:none;"
              />
            </router-link>
          </div>

          <div class="ml-4 mb-3">
            <router-link to="/mainpage">
              <i class="ni ni-tv-2 text-primary mx-2">
                <span class="mx-2">메인페이지</span>
              </i>
            </router-link>
          </div>
          <div class="ml-4 mb-3">
            <router-link to="/editors">
              <i class="ni ni-zoom-split-in text-primary ml-2">
                <span class="mx-2">전체 편집자 보기</span>
              </i>
            </router-link>
          </div>
          <div class="ml-4 mb-3">
            <router-link
              v-if="isEditor && isLogin"
              :to="{
                path: '/portfolio?no=' + this.$session.get('uid'),
                params: { uid: this.$session.get('uid') },
              }"
              :key="$route.fullPath"
            >
              <i class="ni ni-collection text-primary mx-2">
                <span class="mx-2">내 포트폴리오</span>
              </i>
            </router-link>
          </div>
          <div class="ml-4 mb-3">
            <router-link v-if="isLogin" to="/marklist">
              <i class="ni ni-favourite-28 text-red mx-2">
                <span class="mx-2">찜</span>
              </i>
            </router-link>
          </div>
          <div class="ml-4 mb-3">
            <router-link v-if="isLogin" to="/alarm">
              <i class="ni ni-bell-55 text-primar mx-2">
                <span class="mx-2">알람</span>
              </i>
            </router-link>
          </div>
        </b-sidebar>

        <div
          v-if="mask"
          style="position:fixed; top:0px; right:0px; width:100%; height:100vh; opacity:0;"
          @click="maskoff()"
        ></div>
      </div>
    </template>
    <router-link class="navbar-brand" to="/">
      <img
        src="/img/brand/green.png"
        class="navbar-brand-img my-atuo ml-1"
        alt="편집"
        style="width:90px;border:none;"
      />
    </router-link>
    <!-- 최근 메시지 보여주기 -->
    <ul class="navbar-nav align-items-center mr-4 d-none d-md-flex ml-lg-auto">
      <li class="nav-item dropdown" v-if="isLogin">
        <base-dropdown class="nav-link pr-0" position="right">
          <div class="align-items-center text-default" slot="title">
            <i v-if="getNotifyNum() == 0" class="ni ni-bell-55"></i>
            <badge v-if="getNotifyNum() == 0" type="text-light">
              {{
              getNotifyNum()
              }}
            </badge>
            <main rel="main" v-if="getNotifyNum() > 0">
              <div class="notification">
                <svg viewBox="-10 0 35 35">
                  <path
                    class="notification--bell"
                    d="M14 12v1H0v-1l0.73-0.58c0.77-0.77 0.81-3.55 1.19-4.42 0.77-3.77 4.08-5 4.08-5 0-0.55 0.45-1 1-1s1 0.45 1 1c0 0 3.39 1.23 4.16 5 0.38 1.88 0.42 3.66 1.19 4.42l0.66 0.58z"
                  />
                  <path
                    class="notification--bellClapper"
                    d="M7 15.7c1.11 0 2-0.89 2-2H5c0 1.11 0.89 2 2 2z"
                  />
                </svg>
                <span
                  class="notification--num"
                  style="font-style:inherit; font-weight: bold ;"
                >{{ getNotifyNum() }}</span>
              </div>
            </main>
          </div>

          <!-- 레이아웃 width가 md 이하일때는 @/components/Sidebar.vue 수정해야함 -->
          <template>
            <div class="dropdown-header noti-title">
              <h6 class="text-overflow m-0">최근 알림 내역</h6>
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
                <span
                  v-if="notifyitem.notify_type == 'accepted'"
                >{{ notifyitem.request_nickname }}님께서 요청을 수락하셨습니다</span>
                <span
                  v-if="notifyitem.notify_type == 'review'"
                >{{ notifyitem.request_nickname }}님께 후기를 남길 수 있습니다</span>
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
          <i class="fas fa-user-plus mr-1" style="color: #172b4d !important;"></i>
          <span class="nav-link-inner--text" style="color: #172b4d !important;">회원가입</span>
        </router-link>
      </li>
      <li v-if="!isLogin" class="nav-item">
        <router-link :to="{ name: 'login' }" class="nav-link">
          <i class="fas fa-user-check mr-1" style="color: #172b4d !important;"></i>
          <span class="nav-link-inner--text" style="color: #172b4d !important;">로그인</span>
        </router-link>
      </li>
    </ul>

    <!-- 마이페이지 -->
    <ul v-if="isLogin" class="navbar-nav align-items-center ml-2 d-none d-md-flex">
      <li class="nav-item dropdown">
        <base-dropdown class="nav-link pr-0" position="right">
          <div class="media align-items-center" slot="title">
            <i class="fas fa-user" style="color: #172b4d !important;"></i>
            <div class="media-body ml-2 d-none d-lg-block">
              <span class="mb-0 text-sm font-weight-bold text-default">{{ nickname }}</span>
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
            <router-link
              v-if="isEditor"
              :to="{
                path: '/portfolio?no=' + this.$session.get('uid'),
                params: { uid: this.$session.get('uid') },
              }"
              :key="$route.fullPath"
              class="dropdown-item"
            >
              <i class="ni ni-collection"></i>
              <span class>내 포트폴리오</span>
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

      isEditor: "editor",
      isLogin: false,
      // nickname: "",
      mask: false,
    };
  },
  created() {
    //생성 시 로그인 상태 확인
    if (this.$session.exists()) {
      this.isLogin = true;
      this.isEditor = this.$session.get("auth") == "editor";
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
      this.mask = false;
      if (this.$session.exists()) {
        this.isLogin = true;
      } else {
        this.isLogin = false;
      }
    },
  },
  methods: {
    maskon() {
      this.mask = true;
    },
    maskoff() {
      this.mask = false;
      document.getElementById("closebtn").click();
      // console.log("클릭함");
    },
    // toggleSidebar() {
    //   console.log("toggleOn");
    //   this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    //   this.mask = true;
    // },
    // hideSidebar() {
    //   this.$sidebar.displaySidebar(false);
    //   this.mask = false;
    // },
    // toggleMenu() {
    //   this.showMenu = !this.showMenu;
    // },
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
      this.$store.commit("stepper/clearFilterFinderStatus");
      this.$store.commit("auth/mutateIsLogin", false);
      this.isLogin = false;
      this.$router.push("/").catch(() => {});
      // this.$forceUpdate();
    },
  },
};
</script>
<style>
.nav-item:hover {
  cursor: pointer;
}

#sidebar-1 {
  width: 100px;
  background-color: #fbf4f9 important;
  z-index: 999;
}

router-link:hover {
  opacity: 1;
}

.hamburger:focus {
  border: none;
  outline: none;
}

.navbar-brand-img:focus {
  border: none;
  outline: none;
}
main {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}
/* .notification {
  position: relative;
  width: 14px;
  height: 16.8px;
} */
.notification {
  display: flex;
  align-items: center;
  margin-top: 35%;
}
svg {
  display: flex;
  align-items: center;
  width: 40px;
}
svg > path {
  fill: #dc143c;
}

.notification--bell {
  animation: bell 2.2s linear infinite;
  transform-origin: 50% 0%;
}

.notification--bellClapper {
  animation: bellClapper 2.2s 0.1s linear infinite;
}

.notification--num {
  position: absolute;
  top: 22%;
  left: 75%;
  font-size: 14px;
  /* border-radius: 90%; */
  /* border: 1px; */
  color: #172b4d;
  /* background-color: #ff4c13; */
  text-align: center;
  animation: notification 2.2s linear;
}

@keyframes bell {
  0%,
  25%,
  75%,
  100% {
    transform: rotate(0deg);
  }
  40% {
    transform: rotate(10deg);
  }
  45% {
    transform: rotate(-10deg);
  }
  55% {
    transform: rotate(8deg);
  }
  60% {
    transform: rotate(-8deg);
  }
}

@keyframes bellClapper {
  0%,
  25%,
  75%,
  100% {
    transform: translateX(0);
  }
  40% {
    transform: translateX(-0.15em);
  }
  45% {
    transform: translateX(0.15em);
  }
  55% {
    transform: translateX(-0.1em);
  }
  60% {
    transform: translateX(0.1em);
  }
}

@keyframes notification {
  0%,
  25%,
  75%,
  100% {
    opacity: 1;
  }
  30%,
  70% {
    opacity: 0;
  }
}
</style>
