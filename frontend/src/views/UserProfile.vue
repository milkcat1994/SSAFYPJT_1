<template>
  <div>
    <base-header
      class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 300px; background-image: url(img/theme/profile-cover.jpg); background-size: cover; background-position: center top;"
    >
      <!-- Mask -->
      <span class="mask bg-gradient-success opacity-8"></span>
    </base-header>

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
                  <a href="#" class="btn btn-sm btn-primary">회원정보변경</a>
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
                        placeholder="Nickname"
                        input-classes="form-control-alternative"
                        v-model="model.username"
                      />
                    </div>
                    <div class="col-2">
                      <base-button type="default" class style="white-space: nowrap;">중복확인</base-button>
                    </div>

                    <div class="col-lg-12">
                      <base-input
                        alternative
                        label="Email address"
                        placeholder="email@example.com"
                        input-classes="form-control-alternative"
                        v-model="model.email"
                      />
                    </div>
                  </div>
                  <v-btn class="ma-2" @click="userDelete()" color="error">탈퇴하기</v-btn>
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

export default {
  name: "user-profile",
  data() {
    return {
      model: {
        username: "",
        email: "",
      },
    };
  },
  methods: {
    userDelete() {
      let uid = this.$session.get("uid");
      let msg = "회원탈퇴 실패";
      let vue = this;
      alertify.confirm(
        "회원탈퇴",
        "탈퇴 하시겠습니까?",
        function () {
          http
            .delete("user/" + uid)
            .then(({ data }) => {
              if (data.data == "success") {
                msg = "탈퇴가 완료되었습니다.";
                alertify.notify(msg, "success", 3);
                vue.logout();
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
  },
};
</script>
<style></style>
