<template>
  <div>
    <base-header
      class="header pb-4 pt-2 pt-lg-5 d-flex align-items-center"
      style="
        min-height: 100px;
        background-size: cover;
        background-position: center top;
      "
    >
      <span class="mask bg-gradient-success opacity-8"></span>
    </base-header>
    <br />
    <h1 class="text-center">About Us</h1>
    <hr />

    <div class="container">
      <div class="row">
        <h1 class="col-12 text-center my-3">WELCOME TO 편집</h1>
        <br />
        <div class="col d-flex justify-content-center">
          <img class="my-3 logoimg" src="img/theme/logo.png" alt="편집" />
        </div>
        <!-- <h2 class="col-12">편집</h2>
        <h4 class="col-12">편하게 집에서 의뢰하세요</h4>-->
        <span class="col-12 mt-5 mb-3">
          편집은 "편하게 집에서 의뢰하세요"라는 의미를 가진 사이트로,
          사용자가 본인의 영상을 원하는 스타일대로 편집할 수 있게끔 편집자와 매칭해주는 사이트입니다.
        </span>
        <span
          class="col-12 mb-3"
        >편집자들은 자신이 편집한 영상을 포트폴리오로 올릴 수 있으며, 본인을 어필할 수 있는 해쉬태그를 통해 자신의 편집 스타일을 사용자에게 보여줄 수 있습니다.</span>
        <span
          class="col-12 mb-3"
        >편집이 끝난 후에는, 사용자들이 남긴 후기로 편집자들의 평점이 매겨지며, 이를 통해 슈퍼 편집자 추천 페이지에 등재될 수 있습니다.</span>
        <span
          class="col-12 mt-3 mb-6"
        >사용자들은 마음에 드는 편집자를 북마크 함으로써, 편집에서 사용하는 추천 알고리즘을 통해 그와 비슷한 편집자들을 추천 받을 수 있습니다.</span>

        <h1 class="col-12 text-center mt-5">Who We Are</h1>
        <h2 class="col-12 text-center mb-5">1로와</h2>

        <div class="col-6 mb-3 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/전수현.jpg" />
          </div>
          <p class="d-flex align-items-center">팀장 전수현</p>
        </div>

        <div class="col-6 mb-3 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/양지용.jpg" />
          </div>
          <p class="d-flex align-items-center">프로 양지용</p>
        </div>

        <div class="col-6 mb-3 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/홍주표.jpg" />
          </div>
          <p class="d-flex align-items-center">깃홍 홍주표</p>
        </div>

        <div class="col-6 mb-3 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/정세린.jpg" />
          </div>
          <p class="d-flex align-items-center">비타 정세린</p>
        </div>

        <div class="col-6 mb-8 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/임진희.jpg" />
          </div>
          <p class="d-flex align-items-center">나는 임진희</p>
        </div>

        <div class="col-6 mb-8 d-flex justify-content-center">
          <div class="box mr-3" style="background: #BDBDBD;">
            <img class="profile" src="img/theme/조다빈.jpg" />
          </div>
          <p class="d-flex align-items-center">커밋 조다빈</p>
        </div>

        <!-- <b-card-group deck class="col-12">
          <b-card img-src="https://placekitten.com/1000/300" img-alt="1로와" img-top>
            <b-card-title>1로와</b-card-title>
            <b-card-text>편하게 집에서 의뢰하세요</b-card-text>
          </b-card>
        </b-card-group>-->
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import store from "@/store/store.js";
// Install BootstrapVue
Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);

export default {
  name: "AboutUs",
  components: {},
  data() {
    return {
      isLogin: false,
    };
  },
  created() {
    if (this.$session.exists()) {
      this.isLogin = true;
      store
        .dispatch("auth/getUserInfo", this.$session.get("uid"))
        .then((response) => {
          this.setUserInfo(response.data.object);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      this.isLogin = false;
    }
  },
  computed: {},
  methods: {},
  mounted() {
    window.scrollTo(0, 0);
  },
};
</script>

<style scoped>
.box {
  width: 150px;
  height: 150px;
  border-radius: 70%;
  overflow: hidden;
}
.profile {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>