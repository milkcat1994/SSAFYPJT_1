<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">추가하실 편집 기술이 있으신가요?</h1>

    <div class="d-flex my-4">
      <div class="d-flex flex-column mr-4">
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="bright" v-model="checkbox.skill1">
            <span class="display-4">밝기/대비 조정</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="color" v-model="checkbox.skill2">
            <span class="display-4">색 보정</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="audio" v-model="checkbox.skill3">
            <span class="display-4">오디오/음악</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="motion" v-model="checkbox.skill4">
            <span class="display-4">모션그래픽</span>
          </base-checkbox>
        </div>
      </div>
      <div class="d-flex flex-column">
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="caption" v-model="checkbox.skill5">
            <span class="display-4">자막</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="intro" v-model="checkbox.skill6">
            <span class="display-4">인트로</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="outro" v-model="checkbox.skill7">
            <span class="display-4">아웃트로</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="etc" v-model="checkbox.skill8">
            <input type="text" placeholder="기타" v-model="checkbox.etc" />
          </base-checkbox>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "edit-skill",
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
      checkbox: {
        skill1: false,
        skill2: false,
        skill3: false,
        skill4: false,
        skill5: false,
        skill6: false,
        skill7: false,
        skill8: false,
        etc: null,
      },
    };
  },
  methods: {
    setSkills() {
      this.$store.commit('setSkills', {value: this.checkbox})
    }
  },
  validations: {
    // 기타 선택 시 input에 값이 입력되었을 때만 validation 걸어주기
    checkbox: {
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
        this.$v.checkbox.$touch();
        this.setSkills()
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
