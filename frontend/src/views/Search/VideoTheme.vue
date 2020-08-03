<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">원하시는 영상의 특징을 골라주세요!</h1>

    <div class="container py-4">

      <base-radio class="custom-control-alternative" name="comic" v-model="radio.theme">
        <span class="display-4">코믹</span>
      </base-radio>
    
      <base-radio class="custom-control-alternative" name="gang" v-model="radio.theme">
        <span class="display-4">화려한 조명이 감싸는</span>
      </base-radio>
    
      <base-radio class="custom-control-alternative" name="insta" v-model="radio.theme">
        <span class="display-4">갬성 충만</span>
      </base-radio>
    
      <base-radio class="custom-control-alternative" name="trim" v-model="radio.theme">
        <span class="display-4">보정 only</span>
      </base-radio>
    
      <base-radio class="custom-control-alternative" name="etc" v-model="radio.theme">
        <input type="text" placeholder="기타" v-model="radio.etc" />
      </base-radio>
        
    </div>
  </div>
</template>
<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "video-theme",
  props: {
    type: {
      type: String,
    },
    title: String,
    clickedNext: {
      type: Boolean,
    },
    currentStep: {
      type: Object,
    },
  },
  mixins: [validationMixin],
  data() {
    return {
      radio: {
        theme: null,
        etc: null,
      }
    };
  },
  methods: {
    setTheme() {
      this.$store.commit('setTheme', {value: this.radio.theme})
    },
  },
  validations: {
    // 기타 선택 시 input에 값이 입력되었을 때만 validation 걸어주기
    radio: {
      theme: {
        required,
      }
    },
  },
  watch: {
    $v: {
      handler: function (val) {
        if (!val.$invalid) {
          this.$emit("can-continue", { value: true });
        } else {
          this.$emit("can-continue", { value: false });
        }
      },
      deep: true,
    },
    clickedNext(val) {
      if (val === true) {
        this.$v.radio.$touch();
      }
    },
  },
  mounted() {
    if (!this.$v.$invalid) {
      this.$emit("can-continue", { value: true });
    } else {
      this.$emit("can-continue", { value: false });
    }
  },
};
</script>
<style>
</style>
