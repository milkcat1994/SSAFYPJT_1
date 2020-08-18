<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-body px-lg-5 py-lg-5">
          <div class="text-center mb-4">
            <small>회원가입</small>
          </div>
          <form role="form">
            <div class="d-flex justify-content-center">
              <!-- <base-checkbox class="mb-3 mr-3" v-model="model.accounts.editor"
                >Editor</base-checkbox
              >
              <base-checkbox class="mb-3" v-model="model.accounts.noneditor"
                >Non-Editor</base-checkbox
              >-->
              <div class="custom-control custom-radio custom-control-inline cursor-pointer">
                <input
                  type="radio"
                  id="customRadioInline1"
                  name="customRadioInline1"
                  class="custom-control-input"
                  value="editor"
                  v-model="model.accounts"
                />
                <label class="custom-control-label" for="customRadioInline1">Editor</label>
              </div>
              <div class="custom-control custom-radio custom-control-inline cursor-pointer">
                <input
                  type="radio"
                  id="customRadioInline2"
                  name="customRadioInline1"
                  class="custom-control-input"
                  value="noneditor"
                  v-model="model.accounts"
                />
                <label class="custom-control-label" for="customRadioInline2">Non-Editor</label>
              </div>
            </div>
            <br />
            <div class="d-flex">
              <base-input
                class="input-group-alternative mb-3"
                placeholder="NickName"
                addon-left-icon="ni ni-circle-08"
                v-model="model.name"
                required
              ></base-input>
              <base-button
                type="default"
                class="mb-3"
                style="white-space: nowrap;"
                @click="checkNickname()"
              >중복 체크</base-button>
            </div>

            <div class="d-flex">
              <base-input
                class="input-group-alternative mb-3"
                placeholder="Email"
                addon-left-icon="ni ni-email-83"
                v-model="model.email"
                required
              ></base-input>
              <base-button
                type="default"
                class="mb-3"
                style="white-space: nowrap;"
                @click="modal.show = true"
                :disabled="!emailJValid"
              >이메일인증</base-button>
            </div>

            <modal :show.sync="modal.show">
              <template slot="header">
                <h5 class="modal-title" id="requestForm">이메일 인증</h5>
              </template>
              <div>
                <div class="row">
                  <div class="col-3">
                    <h3>인증 번호</h3>
                  </div>
                  <div class="col">
                    <base-input
                      alternative
                      placeholder="0000"
                      input-classes="text"
                      v-model="model.emailcode"
                    />
                  </div>
                  <div class="col-4">
                    <base-button
                      type="primary"
                      :disabled="isCheck"
                      @click="sendEmail()"
                      style="height:42px"
                    >요청하기</base-button>
                  </div>
                </div>
              </div>
              <template slot="footer">
                <base-button type="primary" :disabled="isCheck" @click="checkEmailCode()">인증하기</base-button>
                <base-button type="secondary" @click="modal.show = false">Close</base-button>
              </template>
            </modal>

            <base-input
              class="input-group-alternative"
              placeholder="Password"
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="model.password"
              required
            ></base-input>

            <base-input
              class="input-group-alternative"
              placeholder="Confirm Password"
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="model.passwordconf"
              required
              @keydown.enter="joinCheck()"
            ></base-input>

            <!-- <div class="text-muted font-italic">
              <small>
                password strength:
                <span class="text-success font-weight-700">strong</span>
              </small>
            </div>-->

            <!-- 약관동의 -->
            <!-- <div class="row my-4">
              <div class="col-12">
                <base-checkbox class="custom-control-alternative">
                  <span class="text-muted">
                    <a href="#!">약관에 동의 하겠습니다.</a>
                  </span>
                </base-checkbox>
              </div>
            </div>-->
            <div class="text-center">
              <base-button type="primary" class="my-4" @click="joinCheck()">회원가입</base-button>
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
          <router-link to="/login" class="text-light">
            <small>로그인</small>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
// import { required, email, sameAs } from "vuelidate/lib/validators";

import http from "@/util/http-common";
import alertify from "alertifyjs";
export default {
  name: "register",
  data() {
    return {
      model: {
        name: "",
        email: "",
        password: "",
        passwordconf: "",
        accounts: "editor",
      },
      emailcode: "",
      isCheck: false,
      nickCheck: false,

      modal: {
        show: false,
      },
    };
  },
  methods: {
    sendEmail() {
      http
        .post("/email/token", {
          nickname: this.model.name,
          email: this.model.email,
        })
        .then(({ data }) => {
          //메세지 발송 성공시 발송 메세지 출력
          if (data.data == "success") {
            alertify.notify("인증 메일이 발송 되었습니다.", "success", 3);
          }
          //이메일 중복시
          else if (data.data == "duplicate") {
            alertify.error("이미 등록된 이메일입니다.", 3);
          }
          // 회원가입 실패시 오류 메시지 보여주기
          else {
            alertify.error("인증 메일이 발송에 실패하였습니다.", 3);
          }
          return;
        })
        .catch(() => {
          alertify.error("인증 메일이 발송에 실패하였습니다.", 3);
          return;
        });
    },
    //이메일 코드 확인
    checkEmailCode() {
      http
        .post("/email/token/check", {
          email: this.model.email,
          emailKey: this.model.emailcode,
        })
        .then(({ data }) => {
          //메세지 발송 성공시 발송 메세지 출력
          if (data.data == "success") {
            this.isCheck = true;
            this.modal.show = false;
            alertify.notify("이메일 인증에 성공하였습니다.", "success", 3);
          }
          // 회원가입 실패시 오류 메시지 보여주기
          else {
            alertify.error("이메일 인증에 실패하였습니다.", 3);
          }
        })
        .catch((error) => {
          alertify.error(error, 3);
          return;
        });
    },
    checkNickname() {
      http
        .post("/user/nickname/check", {
          nickname: this.model.name,
        })
        .then(({ data }) => {
          if (data.data != "nickname") {
            this.nickCheck = true;
            alertify.notify("닉네임 사용이 가능합니다.", "nickname", 3);
          } else {
            alertify.error("이미 사용 중인 닉네임입니다.", 3);
          }
        })
        .catch((error) => {
          alertify.error(error, 3);
          return;
        });
    },
    initDialog() {
      this.model.name = "";
      this.model.email = "";
      this.model.password = "";
      this.model.passwordconf = "";
      this.isCheck = false;
      this.nickCheck = false;
    },
    emailCheck(email) {
      let exptext = /^[\w]+@[\w]+\.+[\w]+/;
      if (exptext.test(email) == false) {
        return false;
      }
      return true;
    },
    pwdCheck(password) {
      let pattern1 = /[0-9]/;
      let pattern2 = /[A-Za-z]/;
      if (pattern1.test(password) == false) {
        return false;
      }
      if (pattern2.test(password) == false) {
        return false;
      }
      if (password.length < 8) return false;
      return true;
    },
    joinCheck() {
      if (!this.emailCheck(this.model.email)) {
        alertify.error("이메일 양식이 아닙니다.", 3);
        return;
      }

      let valid = true;
      let message = "";
      // 필수 입력이 되었는지 확인
      valid &&
        !this.model.name &&
        ((valid = false), (message = "닉네임을 입력해주세요."));
      valid &&
        this.model.name.length > 15 &&
        ((valid = false), (message = "닉네임은 15자 까지 가능합니다."));
      valid &&
        !this.model.email &&
        ((valid = false), (message = "이메일을 입력해주세요."));
      valid &&
        !this.isCheck &&
        ((valid = false), (message = "이메일 인증을 해주세요."));
      valid &&
        !this.nickCheck &&
        ((valid = false), (message = "닉네임 중복체크를 해주세요."));
      valid &&
        !this.pwdCheck(this.model.password) &&
        ((valid = false),
        (message = "비밀번호는 8자 이상 숫자,문자 1개이상 입니다."));
      valid &&
        (!this.model.password ||
          !this.model.passwordconf ||
          this.model.password !== this.model.passwordconf) &&
        ((valid = false), (message = "비밀번호를 다시 확인해주세요."));
      console.log("valid>>" + message);
      if (valid) this.join();
      else {
        alertify.error(message, 3);
        return;
      }
    },
    join() {
      console.log(this.model);
      http
        .post("/user", {
          auth: this.model.accounts,
          email: this.model.email,
          nickname: this.model.name,
          password: this.model.password,
        })
        .then(({ data }) => {
          console.log(data);
          if (data.data == "success") {
            alertify.notify("회원가입 되었습니다.", "success", 3);
            this.$router.push("/login");
          }
          // 회원가입 실패시 오류 메시지 보여주기
          else if (data.data == "email") {
            alertify.error("이미 존재하는 email입니다.", 3);
          } else if (data.data == "nickname") {
            alertify.error("이미 존재하는 닉네임입니다.", 3);
          }
        })
        .catch((err) => {
          console.log(err);
          alertify.error("서버 오류 발생하였습니다.", 3);
        });
    },
  },
  computed: {
    emailJValidText() {
      let text = "";
      this.emailCheck(this.model.email) || (text = "이메일 형식이 아닙니다.");
      return text;
    },
    nicknameValidText() {
      let text = "";
      this.name.length <= 15 || (text = "15자 까지 가능합니다.");
      return text;
    },
    //비밀번호의 같음을 파악하고 Text를 결정한다.
    pwdConfirmText() {
      let text = "";
      this.password == this.passwordconf ||
        (text = "비밀번호가 일치하지 않습니다.");
      return text;
    },
    emailLValid() {
      if (this.emailCheck(this.model.email)) return true;
      else return false;
    },
    emailJValid() {
      if (this.emailCheck(this.model.email)) return true;
      else {
        if (this.isCheck) return true;
        else return false;
      }
    },
  },
};
</script>
<style>
.cursor-pointer {
  cursor: pointer;
}
</style>
