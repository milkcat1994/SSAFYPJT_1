<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-body px-lg-5 py-lg-5">
          <div class="text-center text-muted mb-4">
            <p>아래 항목을 입력하면 회원가입이 완료됩니다.</p>
          </div>
          <form role="form">
            <div class="d-flex justify-content-center">
              <div class="custom-control custom-radio custom-control-inline">
                <input
                  type="radio"
                  id="customRadioInline1"
                  name="customRadioInline1"
                  class="custom-control-input"
                  v-model="model.accounts"
                />
                <label class="custom-control-label" for="customRadioInline1"
                  >Editor</label
                >
              </div>
              <div class="custom-control custom-radio custom-control-inline">
                <input
                  type="radio"
                  id="customRadioInline2"
                  name="customRadioInline1"
                  class="custom-control-input"
                  v-model="model.accounts"
                />
                <label class="custom-control-label" for="customRadioInline2"
                  >Non-Editor</label
                >
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
                >중복 체크</base-button
              >
            </div>
            <div class="text-center">
              <base-button type="primary" class="my-4">회원가입</base-button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common";
export default {
  name: "SocialRegister",
  data() {
    return {
      model: {
        name: "",
        accounts: "",
      },
    };
  },
};
</script>

<style></style>
