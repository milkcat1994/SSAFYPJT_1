export const stepper = {
  state: {
    requests: {
      status: false,
      videoType: null,
      videoStyle: null,
      originLength: null,
      finalLength: null,
      videoSkills: null,
      term: null,
    },
  },
  getters: {
    // Stepper 세부사항 불러오기
    getRequests(state) {
      return state.requests;
    },
  },
  mutations: {
    // Stepper status 갱신
    setStatus(state, payload) {
      state.requests.status = payload.value;
    },
    // Stepper 항목별 갱신
    setCategory(state, payload) {
      state.requests.videoType = payload.value;
    },
    setTheme(state, payload) {
      state.requests.videoStyle = payload.value;
    },
    setOriginLength(state, payload) {
      state.requests.originLength = payload.value;
    },
    setFinalLength(state, payload) {
      state.requests.finalLength = payload.value;
    },
    setSkills(state, payload) {
      state.requests.videoSkills = payload.value;
    },
    setTerm(state, payload) {
      state.requests.term = payload.value;
    },
  }
}