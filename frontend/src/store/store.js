import Vue from "vue";
import Vuex from "vuex";

import http from "@/util/http-common";

import { auth } from "./auth.module";

import createPersistedState from "vuex-persistedstate";
import alertify from "alertifyjs";

import Vuelidate from "vuelidate";

Vue.use(Vuelidate);
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
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

    requests: {
      category: null,
      theme: null,
      originLength: null,
      finalLength: null,
      skills: null,
      otherSkill: null,
      term: null,
    },
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

    // 작업 요청서 세부사항 불러오기
    getRequests(state) {
      return state.requests;
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
      let obj = [];
      for (let index = 0; index < requestitems0.length; index++) {
        let element = requestitems0[index];
        element.uid = index + "request";

        obj.push(element);
      }
      // state.requestitems = requestitems;
      state.requestitems0 = obj;
    },
    mutateSetRequestitems1(state, requestitems1) {
      let obj = [];
      for (let index = 0; index < requestitems1.length; index++) {
        let element = requestitems1[index];
        element.uid = index + "pogreess";

        obj.push(element);
      }
      // state.requestitems = requestitems;
      state.requestitems1 = obj;
    },
    mutateSetRequestitems2(state, requestitems2) {
      let obj = [];
      for (let index = 0; index < requestitems2.length; index++) {
        let element = requestitems2[index];
        element.uid = index + "done";

        obj.push(element);
      }
      // state.requestitems = requestitems;
      state.requestitems2 = obj;
    },
    mutateSetRequestitems3(state, requestitems3) {
      let obj = [];
      for (let index = 0; index < requestitems3.length; index++) {
        let element = requestitems3[index];
        element.uid = index + "review";

        obj.push(element);
      }
      // state.requestitems = requestitems;
      state.requestitems3 = obj;
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
    // 작업 요청서 항목별 갱신
    setCategory(state, payload) {
      state.requests.category = payload.value;
    },
    setTheme(state, payload) {
      state.requests.theme = payload.value;
    },
    setOriginLength(state, payload) {
      state.requests.originLength = payload.value;
    },
    setFinalLength(state, payload) {
      state.requests.finalLength = payload.value;
    },
    setSkills(state, payload) {
      state.requests.skills = payload.value;
    },
    setOtherSkill(state, payload) {
      state.requests.otherSkill = payload.value;
    },
    setTerm(state, payload) {
      state.requests.term = payload.value;
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
      context.commit('mutateSetProgressdate', []);
      context.commit('mutateSetHolidaydate', []);
      context.commit('mutateSetRequestitems0123', []);
    },

    getTagitem(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("mutateSetTagitem", data);
      });
    },
  },
  plugins: [createPersistedState()],
});
