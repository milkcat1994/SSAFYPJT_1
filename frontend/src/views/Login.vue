<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-header bg-transparent pb-5">
          <div class="text-muted text-center mt-2 mb-3">
            <small>소셜 로그인</small>
          </div>
          <div class="btn-wrapper text-center">
            <!-- for google login -->
            <a @click="googleLogin" class="btn btn-neutral btn-icon" style="width:350px">
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
            <div
              v-if="!this.$v.model.email.email"
              class="text-left ml-3 mb-3 small"
            >이메일 형식이 올바르지 않습니다.</div>

            <base-input
              class="input-group-alternative"
              placeholder="Password"
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="model.password"
              required
              @keydown.enter="login()"
            ></base-input>
            <div
              v-if="!model.success"
              class="text-left ml-3 mb-3 small"
              style="color:red"
            >아이디 및 비밀번호를 확인해 주세요.</div>
            <!-- <base-checkbox class="custom-control-alternative">
              <span class="text-muted">이 사이트 기억하기</span>
            </base-checkbox>-->
            <div class="text-center">
              <base-button type="primary" class="my-4" @click="login()">로그인</base-button>
            </div>
          </form>
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-6">
          <router-link to="/mainpage" class="text-light">
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

    <!-- for google connect acception -->
    <modal :show.sync="modal.show">
      <template slot="header">
        <h5 class="modal-title" id="requestForm">Google 계정 연동 동의</h5>
      </template>
      <div>구글 계정 연동에 동의하시겠습니까?</div>
      <template slot="footer">
        <base-button type="secondary" @click="modal.show = false">Close</base-button>
        <base-button type="primary" @click="connectGoogle">동의</base-button>
      </template>
    </modal>
  </div>
</template>
<script>
import store from "@/store/store.js";
import { mapGetters } from "vuex";
import alertify from "alertifyjs";
import http from "@/util/http-common";
import { email } from "vuelidate/lib/validators";

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
        success: true,
      },
      modal: {
        show: false,
      },
      googleUid: "",
    };
  },
  methods: {
    initInputL() {
      this.model.email = "";
      this.model.password = "";
      this.model.success = true;
    },
    login() {
      store
        .dispatch("auth/login", {
          userEmail: this.model.email,
          pwd: this.model.password,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.$session.start();
            // console.log(data.object);
            this.$store.commit("auth/mutateIsLogin", true);
            this.$session.set("uid", data.object.uid);
            this.$session.set("nickname", data.object.nickname);
            this.$session.set("auth", data.object.auth);
            this.initInputL();
            this.$router.push("/");
            return;
          } else if (data.data == "fail") {
            // alertify.error("아이디 및 비밀번호를 확인해주세요", 3);
            this.model.success = false;
          }
        })
        .catch(() => {
          // alertify.error("아이디 및 비밀번호를 확인해주세요", 3);
          this.model.success = false;
          return;
        });
    },

    // google Login
    googleLogin() {
      this.$gAuth
        .signIn()
        .then((GoogleUser) => {
          var userEmail = GoogleUser.getBasicProfile().getEmail();
          // 이메일 회원 있는지 확인
          http
            .post("/auth/google/" + userEmail)
            .then(({ data }) => {
              if (data.data == "success") {
                alertify.success("로그인이 되었습니다.");
                this.$session.start();
                this.$store.commit("auth/mutateIsLogin", true);
                this.$session.set("uid", data.object.uid);
                this.$session.set("nickname", data.object.nickname);
                this.$session.set("auth", data.object.auth);
                this.initInputL();
                this.$router.push("/");
              } else if (data.data == "kakao") {
                alertify.warning("카카오와 연동된 계정 입니다.");
              } else if (data.data == "normal") {
                this.googleUid = data.object.uid;
                this.modal.show = true;
              } else {
                alertify.error(
                  "회원 정보가 없습니다. 먼저 회원가입을 진행해 주세요"
                );
                this.$router.push({ name: "register" });
              }
            })
            .catch(() => {
              alert("로그인 시 에러가 발생했습니다.");
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    connectGoogle() {
      http
        .post("/auth/google/connect/" + this.googleUid)
        .then(({ data }) => {
          if (data.data == "success") {
            alertify.success("연동이 완료되었습니다.");
            this.googleLogin();
            this.modal.show = false;
          } else if (data == "fail") {
            alertify.warning("연동시 에러가 발생했습니다.");
            this.modal.show = false;
          }
        })
        .catch(() => {
          this.modal.show = false;
        });

      this.googleUid;
    },
  },
  watch: {
    $route() {
      if (this.$session.exists()) {
        this.isLogin = true;
      } else {
        this.isLogin = false;
      }
    },
  },
  created() {},
  validations: {
    model: {
      email: { email },
    },
  },
};
</script>
<style></style>
