import http from "@/util/http-common";

export const auth = {
  namespaced: true,
  state: {
    isLoggedIn: false,
    userInfo: {},
    userToken: "",
  },
  getters: {
    isLoggedIn(state) {
      return state.isLoggedIn;
    },
    userInfo(state) {
      return state.userInfo;
    },
  },
  mutations: {
    mutateIsLogin(state, isLoggedIn) {
      state.isLoggedIn = isLoggedIn;
    },
    mutateUserInfo(state, userInfo) {
      state.userInfo = {
        userEmail: userInfo.id,
      };
    },
    mutateUserToken(state, userToken) {
      state.userToken = userToken;
    },
    mutateRegister(state) {
      state.isLoggedIn = false;
    },
  },
  actions: {
    login(context, { userEmail, pwd }) {
      return new Promise((resolve, reject) => {
        http
          .post("/user/login", {
            email: userEmail,
            password: pwd,
          })
          .then(({ data }) => {
            context.commit("mutateIsLogin", true);
            let token = Math.random()
              .toString(36)
              .substring(2);
            context.commit("mutateUserToken", token);
            context.commit("mutateUserInfo", data);

            resolve({ data });
          })
          .catch((error) => {
            if (error.response) {
              if (error.response.status == "404") {
                alert("아이디 또는 비밀번호가 올바르지 않습니다.", 3);
              } else {
                alert("로그인 처리시 에러가 발생했습니다.", 3);
              }
            }
            return reject();
          });
      });
    },
    logout(context) {
      return new Promise((resolve, reject) => {
        http
          .get("/user/logout")
          .then(() => {
            context.commit("mutateIsLogin", false);
            context.commit("mutateUserToken", "");
            context.commit("mutateUserInfo", {});
            this.$router.push("/");
            resolve();
          })
          .catch((error) => {
            alert(error);
            return reject();
          });
      });
    },
    setUserInfo(context) {
      context.commit("mutateIsLogin", localStorage.isLoggedIn);
      context.commit("mutateUserToken", localStorage.userToken);
      context.commit("mutateUserInfo", localStorage.userInfo);
    },
    getUserInfo(context, uid) {
      return new Promise((resolve, reject) => {
        http
          .post("/user/" + uid, {
            uid: uid,
          })
          .then(({ data }) => {
            resolve({ data });
          })
          .catch(() => {
            alert("유저정보가 없습니다.", 3);
            return reject();
          });
      });
    },
  },
};
