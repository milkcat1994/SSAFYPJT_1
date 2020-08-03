// import Vue from "vue";
// import Vuex from "vuex";

// import axios from "axios";
// import cookies from "vue-cookies";

// import router from "@/router";

// Vue.use(Vuex);
// SERVER_URL = "http://localhost:8080";
// export default new Vuex.Store({
//   state: {
//     authToken: cookies.get("auth-token"),
//   },
//   getters: {
//     isLoggedIn: (state) => !!state.authToken,
//     currentUsername: (state) => state.username,
//     config: (state) => ({
//       headers: { Authorization: `Token ${state.authToken}` },
//     }),
//   },
//   mutations: {
//     SET_TOKEN(state, token) {
//       state.authToken = token;
//       cookies.set("auth-token", token);
//     },
//     SET_USERNAME(state, username) {
//       state.currentUsername = username;
//       cookies.set("username", username);
//     },
//   },
//   actions: {
//     auth({ commit }, info) {
//       axios
//         .post(SERVER.URL + info.location, info.data)
//         .then((res) => {
//           commit("SET_TOKEN", res.data.key);
//           commit("SET_USERNAME", info.data.username);
//           router.push({ name: "About" });
//         })
//         .catch((err) => {
//           console.error(err.response.data);
//           alert("다시 확인해주세요");
//         });
//     },

//     signup({ dispatch }, signupData) {
//       const info = {
//         data: signupData,
//       };
//       dispatch("auth", info);
//     },
//     login({ dispatch }, loginData) {
//       const info = {
//         data: loginData,
//       };
//       dispatch("auth", info);
//     },
//     logout({ commit, getters }) {
//       axios
//         .post(SERVER_URL + "/logout", null, getters.config)
//         .then(() => {
//           commit("SET_TOKEN", null);
//           commit("SET_USERNAME", null);
//           cookies.remove("auth-token");
//           cookies.remove("username");
//           router.push({ name: "dashboard" });
//         })
//         .catch((err) => console.error(err));
//     },
//     deleteAccount({ getters }, uid) {
//       console.log(SERVER_URL + uid + "/delete" + "/");
//       axios
//         .post(
//           SERVER.URL + SERVER.ROUTES.userdelete + uid + "/",
//           null,
//           getters.config
//         )
//         .then(() => router.push({ name: "Config" }))
//         .catch((err) => {
//           console.error(err);
//         });
//     },
//   },
//   modules: {},
// });
