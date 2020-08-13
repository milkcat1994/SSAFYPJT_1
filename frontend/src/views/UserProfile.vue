<template>
  <div>
    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-12 order-xl-1">
          <card shadow type="secondary">
            <div slot="header" class="bg-white border-0">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">회원정보</h3>
                </div>
                <div class="col-4 text-right">
                  <base-button
                    class="btn btn-sm btn-primary"
                    :disabled="!ischeck"
                    @click="updateProfile()"
                  >회원 정보 수정</base-button>
                </div>
              </div>
            </div>
            <template>
              <form @submit.prevent>
                <h6 class="heading-small text-muted mb-4">User information</h6>
                <div class="pl-lg-4">
                  <div class="row justify-content-md-center">
                    <div class="col-lg-12">
                      <base-input
                        alternative
                        label="Nickname"
                        input-classes="form-control-alternative"
                        v-model="nickname"
                      />
                    </div>
                    <div class="d-flex justify-content-center">
                      <base-button
                        type="default"
                        class="btn-sm"
                        style="white-space: nowrap;"
                        @click="checkNickname()"
                      >중복확인</base-button>
                    </div>

                    <div class="col-lg-12">
                      <base-input
                        alternative
                        disabled
                        label="Email address"
                        ref="email"
                        v-model="email"
                      />
                    </div>
                  </div>
                  <div class="d-flex justify-content-center">
                    <v-btn class="btn btn-sm btn-primary" @click="userDelete()">탈퇴하기</v-btn>
                  </div>
                </div>
              </form>
            </template>
          </card>
        </div>
      </div>
    </div>
    <br />
  </div>
</template>
<script>
import http from "@/util/http-common";
import alertify from "alertifyjs";
import store from "@/store/store.js";

export default {
  name: "profile",
  data() {
    return {
      uid: "",
      nickname: "",
      email: "",
      isLogin: false,
      ischeck: false,
    };
  },
  created() {
    if (this.$session.exists()) {
      this.isLogin = true;
      store
        .dispatch("auth/getUserInfo", this.$session.get("uid"))
        .then((response) => {
          this.uid = this.$session.get("uid");
          this.setUserInfo(response.data.object);
        })
        .catch(() => {
          this.$router.push("/");
        });
    } else {
      this.isLogin = false;
    }
  },
  methods: {
    setUserInfo(data) {
      this.nickname = data.nickname;
      this.email = data.email;
    },
    checkNickname() {
      http
        .post("/user/nickname/check", {
          nickname: this.nickname,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            this.ischeck = true;
            alertify.notify("닉네임 사용이 가능합니다", "nickname", 3);
          } else {
            alertify.error("이미 사용 중인 닉네임입니다.", 3);
          }
        })
        .catch((error) => {
          alertify.error(error, 3);
          return;
        });
    },
    updateProfile() {
      let msg = "회원 정보 수정에 실패하였습니다.";
      if (this.ischeck == false) {
        alertify.error("닉네임 중복체크를 해주세요", 3);
        return;
      }
      http
        .put("/user/user/" + this.uid, {
          nickname: this.nickname,
        })
        .then(({ data }) => {
          console.log(data);
          if (data.data == "success") {
            this.$session.set("nickname", this.nickname);
            msg = "회원 정보가 수정되었습니다";
            this.ischeck = false;
            alertify.notify(msg, "success", 3);
            return;
          } else {
            alertify.error(msg, 3);
            this.ischeck = false;
            return;
          }
        })
        .catch(() => {
          msg = "회원정보 수정 서버 통신 실패";
          alertify.error(msg, 3);
          return;
        });
    },
    userDelete() {
      let uid = this.$session.get("uid");
      let msg = "회원탈퇴 실패";
      let now = this;
      alertify.confirm(
        "회원 탈퇴",
        "탈퇴 하시겠습니까?",
        function () {
          http
            .delete("user/user/" + uid)
            .then(({ data }) => {
              if (data.data == "success") {
                msg = "탈퇴가 완료되었습니다.";
                alertify.notify(msg, "success", 3);
                now.logout();
                return;
              } else {
                msg = "탈퇴에 실패하였습니다.";
                alertify.error(msg, 3);
                return;
              }
            })
            .catch(() => {
              msg = "회원탈퇴 서버 통신 실패";
              alertify.error(msg, 3);
              return;
            });
        },
        function () {
          alertify.error("취소되었습니다.");
        }
      );
    },
    logout() {
      this.$session.destroy();
      this.$router.push("/");
    },
  },
};
</script>
<style></style>
