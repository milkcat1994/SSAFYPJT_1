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
    stepper
  },

  state: {
    notifyitems: [],
    notifyitem: {},

    requestitems0: [],
    requestitems1: [],
    requestitems2: [],
    requestitems3: [],
    requestitem: {},

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

    // request
    requestitems0(state) {
      return state.requestitems0;
    },
    requestitems1(state) {
      return state.requestitems1;
    },
    requestitems2(state) {
      return state.requestitems2;
    },
    requestitems3(state) {
      return state.requestitems3;
    },
    requestitem(state) {
      return state.requestitem;
    },
    progressdate(state) {
        return state.progressdate;
    },
    holidaydate(state) {
        return state.holidaydate;
    },
    scheduledate(state) {
      if (state.holidaydate.length > 0 && state.progressdate.length > 0) 
        return state.holidaydate.concat(state.progressdate).concat([{start:"", end:"", categoryId: 2, repeat: "montly", title:"selected"}]);
      if (state.holidaydate.length > 0 && state.progressdate.length <= 0) 
        return state.holidaydate.concat([{start:"", end:"", categoryId: 2, repeat: "montly", title:"selected"}]);
      if (state.progressdate.length > 0 && state.holidaydate.length <= 0) 
        return state.progressdate.concat([{start:"", end:"", categoryId: 2, repeat: "montly", title:"selected"}]);
      return state.scheduledate.concat([{start:"", end:"", categoryId: 2, repeat: "montly", title:"selected"}]);
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

    // request
    mutateSetRequestitems0(state, requestitems0) {
      state.requestitems0 = requestitems0;
    },
    mutateSetRequestitems1(state, requestitems1) {
      state.requestitems1 = requestitems1;
    },
    mutateSetRequestitems2(state, requestitems2) {
      state.requestitems2 = requestitems2;
    },
    mutateSetRequestitems3(state, requestitems3) {
      state.requestitems3 = requestitems3;
    },
    mutateSetRequestitems0123(state, requestitems) {
      state.requestitems0 = requestitems
      state.requestitems1 = requestitems
      state.requestitems2 = requestitems
      state.requestitems3 = requestitems
    },
    mutateSetRequestitem(state, requestitem) {
      state.requestitem = requestitem;
    },
    mutateSetProgressdate(state, progressdate) {
        state.progressdate = progressdate
    },
    mutateSetHolidaydate(state, holidaydate) {
        state.holidaydate = holidaydate
    },

    mutateSetScheduledate(state, data) {
        state.holidaydate = data
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

    // request
    getRequestitems0(context, payload) {
      http
        .get(payload)
        .then(({ data }) => {
          context.commit("mutateSetRequestitems0", data);
        })
        .catch(() => {
          alertify.error("에러가 발생했습니다.", 3);
        });
    },
    getRequestitems1(context, payload) {
      http
        .get(payload)
        .then(({ data }) => {
          context.commit("mutateSetRequestitems1", data);
        })
        .catch(() => {
          alertify.error("에러가 발생했습니다.", 3);
        });
    },
    getRequestitems2(context, payload) {
      http
        .get(payload)
        .then(({ data }) => {
          context.commit("mutateSetRequestitems2", data);
        })
        .catch(() => {
          alertify.error("에러가 발생했습니다.", 3);
        });
    },
    getRequestitems3(context, payload) {
      http
        .get(payload)
        .then(({ data }) => {
          context.commit("mutateSetRequestitems3", data);
        })
        .catch(() => {
          alertify.error("에러가 발생했습니다.", 3);
        });
    },
    getRequestitem(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetRequestitem", data);
      });
    },
    getProgressdate(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit('mutateSetProgressdate', data);
      });
    },
    getHolidaydate(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit('mutateSetHolidaydate', data);
      });
    },
    initDataOnAlarm(context) {
      context.commit('mutateSetHolidaydate', []);
      context.commit('mutateSetProgressdate', []);
      context.commit('mutateSetRequestitems0123', []);
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
    })
  ],
});
