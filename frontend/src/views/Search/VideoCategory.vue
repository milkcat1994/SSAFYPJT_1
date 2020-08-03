<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">

    <h1 class="display-3 mb-0">어떤 영상인지 알려주세요!</h1>

    <div class="container py-4">

      <!-- <base-radio class="custom-control-alternative" name="personal" v-model="radio.category"> -->
      <base-radio class="custom-control-alternative" name="personal" v-model="category">
        <span class="display-4">개인 소장</span>
      </base-radio>

      <base-radio class="custom-control-alternative" name="business" v-model="category">
        <span class="display-4">사업용</span>
      </base-radio>

      <base-radio class="custom-control-alternative" name="celebration" v-model="category">
        <span class="display-4">기념 행사(생일, 결혼 등) 영상</span>
      </base-radio>

      <base-radio class="custom-control-alternative" name="etc" v-model="category">
        <span class="display-4">기타</span>
      </base-radio>
      
    </div>
  </div>
</template>
<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "video-category",
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
      category: null,
    }
  },
  methods: {
    setCategory() {
      this.$store.commit('setCategory', {value: this.final})
    },
  },
  validations: {
    category: {
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
        this.$v.category.$touch();
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
