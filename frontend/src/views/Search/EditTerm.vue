<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">희망 완료일을 알려주세요!</h1>

    <div class="container my-4 py-4">
      <base-input addon-left-icon="ni ni-calendar-grid-58">
        <flat-picker
          slot-scope="{focus, blur}"
          @on-open="focus"
          @on-close="blur"
          :config="{allowInput: true}"
          class="form-control datepicker"
          v-model="dates.simple"
        ></flat-picker>
      </base-input>
    </div>
  </div>
</template>
<script>
import flatPicker from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";

import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "edit-term",
  components: { flatPicker },
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
      dates: {
        simple: "2020-07-29",
      },
    };
  },
  methods: {
    setTerm() {
      this.$store.commit('setTerm', {value: this.dates.simple})
    },
  },
  validations: {
    dates: {
      simple: {
        required,
      },
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
        this.$v.dates.$touch();
        this.setTerm()
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
