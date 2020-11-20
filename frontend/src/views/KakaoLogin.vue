<template>
  <div id="app">
    <KakaoLogin
      api-key="<api-key>"
      image="kakao_login_btn_large"
      :on-success="onSuccess"
      :on-failure="onFailure"
    />
  </div>
</template>

<script>
import KakaoLogin from "vue-kakao-login";

let onSuccess = (data) => {
};
let onFailure = (data) => {
};

export default {
  name: "Kakao",
  components: {
    KakaoLogin,
  },
  methods: {
    onSuccess,
    onFailure,
  },
};
</script>

<style></style>
