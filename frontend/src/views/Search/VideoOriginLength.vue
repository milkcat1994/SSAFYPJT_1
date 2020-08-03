<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">원본 영상의 길이는 어느 정도인가요?</h1>

    <div class="container py-4">
      
      <base-radio class="custom-control-alternative" name="one-min" v-model="origin">
        <span class="display-4">1분 이내</span>
      </base-radio>

      <base-radio class="custom-control-alternative" name="five-min" v-model="origin">
        <span class="display-4">5분 이내</span>
      </base-radio>

      <base-radio class="custom-control-alternative" name="thirty-min" v-model="origin">
        <span class="display-4">30분 이내</span>
      </base-radio>
      
      <base-radio class="custom-control-alternative" name="one-hr" v-model="origin">
        <span class="display-4">1시간 이내</span>
      </base-radio>
      
      <base-radio class="custom-control-alternative" name="over-hr" v-model="origin">
        <span class="display-4">1시간 이상</span>
      </base-radio>

    </div>
  </div>
</template>
<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "video-origin-length",
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
      origin: null,
    };
  },
  validations: {
    origin: {
      required,
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
        this.$v.origin.$touch();
      }
    },
    origin(val) {
      this.$store.commit('setOriginLength', {value: val})
    }
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
