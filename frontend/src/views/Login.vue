<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-header bg-transparent pb-5">
          <div class="text-muted text-center mt-2 mb-3">
            <small>소셜 로그인</small>
          </div>
          <div class="btn-wrapper text-center">
            <a href="#" class="btn btn-neutral btn-icon">
              <span class="btn-inner--icon">
                <img src="img/icons/common/kakao.svg" />
              </span>
              <span class="btn-inner--text">Kakao</span>
            </a>
            <a href="#" class="btn btn-neutral btn-icon">
              <span class="btn-inner--icon">
                <img src="img/icons/common/google.svg" />
              </span>
              <span class="btn-inner--text">Google</span>
            </a>
          </div>
        </div>
        <div class="card-body px-lg-5 py-lg-5">
          <div class="text-center text-muted mb-4">
            <small>또는 사이트 로그인</small>
          </div>
          <form role="form">
            <base-input
              class="input-group-alternative mb-3"
              placeholder="Email"
              addon-left-icon="ni ni-email-83"
              v-model="model.email"
              autofocus
              required
            ></base-input>

            <base-input
              class="input-group-alternative"
              placeholder="Password"
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="model.password"
              required
              @keydown.enter="login()"
            ></base-input>

            <!-- <base-checkbox class="custom-control-alternative">
              <span class="text-muted">이 사이트 기억하기</span>
            </base-checkbox>-->
            <div class="text-center">
              <base-button type="primary" class="my-4" @click="login()"
                >로그인</base-button
              >
            </div>
          </form>
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-6">
          <router-link to="/dashboard" class="text-light">
            <small>메인화면</small>
          </router-link>
        </div>
        <div class="col-6 text-right">
          <router-link :to="{ name: 'register' }" class="text-light">
            <small>회원가입</small>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import store from "@/store/store.js";
import { mapGetters } from "vuex";
import alertify from "alertifyjs";

export default {
  name: "login",
  computed: {
    ...mapGetters(["isLoggedIn"]),
  },
  data() {
    return {
      model: {
        email: "",
        password: "",
      },
    };
  },
  methods: {
    initInputL() {
      this.model.email = "";
      this.model.password = "";
    },
    emailCheck(email) {
      let exptext = /^[\w]+@[\w]+[\w]+/;
      if (exptext.test(email) == false) {
        return false;
      }
      return true;
    },
    pwdCheck(pwd) {
      let pattern1 = /[0-9]/;
      let pattern2 = /[A-Za-z]/;
      if (pattern1.test(pwd) == false) {
        return false;
      }
      if (pattern2.test(pwd) == false) {
        return false;
      }
      if (pwd.length < 8) return false;
      return true;
    },
    login() {
      if (!this.emailCheck(this.model.email)) {
        alertify.error("아이디 또는 비밀번호가 올바르지 않습니다.");
        return;
      }

      store
        .dispatch("auth/login", {
          userEmail: this.model.email,
          pwd: this.model.password,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.$session.start();
            console.log(data.object);
            this.$session.set("uid", data.object.uid);
            this.$session.set("nickname", data.object.nickname);
            this.$session.set("auth", data.object.auth);
            this.initInputL();
            this.$router.push("/");
            return;
          }
        })
        .catch(() => {
          alertify.error("로그인중 서버 오류가 발생하였습니다.", 3);
          return;
        });
    },
  },
  watch: {},
  created() {},
};
</script>
<style></style>
