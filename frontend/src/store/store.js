import Vue from "vue";
import Vuex from "vuex";

import http from "@/util/http-common";

import { auth } from "./auth.module";
import { stepper } from "./stepper";

import createPersistedState from "vuex-persistedstate";
import alertify from "alertifyjs";

import Vuelidate from "vuelidate";
// import { platform } from "chart.js";

Vue.use(Vuelidate);
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
    stepper,
  },

  state: {
    notifyitems: [],
    notifyitem: {},

    tagitem: "",

    progressdate: [],
    holidaydate: [],
    scheduledate: [],
  },
  getters: {
    // notify
    notifyitems(state) {
      return state.notifyitems;
    },
    notifyitem(state) {
      return state.notifyitem;
    },
    progressdate(state) {
      return state.progressdate;
    },
    holidaydate(state) {
      return state.holidaydate;
    },
    scheduledate(state) {
      if (state.holidaydate.length > 0 && state.progressdate.length > 0)
        return state.holidaydate
          .concat(state.progressdate)
          .concat([
            {
              start: "",
              end: "",
              categoryId: 2,
              repeat: "montly",
              title: "selected",
            },
          ]);
      if (state.holidaydate.length > 0 && state.progressdate.length <= 0)
        return state.holidaydate.concat([
          {
            start: "",
            end: "",
            categoryId: 2,
            repeat: "montly",
            title: "selected",
          },
        ]);
      if (state.progressdate.length > 0 && state.holidaydate.length <= 0)
        return state.progressdate.concat([
          {
            start: "",
            end: "",
            categoryId: 2,
            repeat: "montly",
            title: "selected",
          },
        ]);
      return state.scheduledate.concat([
        {
          start: "",
          end: "",
          categoryId: 2,
          repeat: "montly",
          title: "selected",
        },
      ]);
    },

    tagitem(state) {
      return state.tagitem;
    },
  },
  mutations: {
    // notify
    mutateSetNotifyitems(state, notifyitems) {
      state.notifyitems = notifyitems;
    },
    mutateSetNotifyitem(state, notifyitem) {
      state.notifyitem = notifyitem;
    },

    mutateSetProgressdate(state, progressdate) {
      state.progressdate = progressdate;
    },
    mutateSetHolidaydate(state, holidaydate) {
      state.holidaydate = holidaydate;
    },

    mutateSetScheduledate(state, data) {
      state.holidaydate = data;
    },

    mutateSetTagitem(state, tagitem) {
      state.tagitem = tagitem;
    },
  },
  actions: {
    // notify
    getNotifyitems(context, payload) {
      http
        .get(payload)
        .then(({ data }) => {
          context.commit("mutateSetNotifyitems", data);
        })
        .catch(() => {
          alertify.error("에러가 발생했습니다.", 3);
        });
    },
    getNotifyitem(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetNotifyitem", data);
      });
    },

    getProgressdate(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetProgressdate", data);
      });
    },
    getHolidaydate(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetHolidaydate", data);
      });
    },
    getTagitem(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetTagitem", data);
      });
    },
  },
  plugins: [
    createPersistedState({
      paths: ["auth"],
    }),
  ],
});
