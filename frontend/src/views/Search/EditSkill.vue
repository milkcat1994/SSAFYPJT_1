<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">추가하실 편집 기술이 있으신가요?</h1>

    <div class="d-flex my-4">
      <div class="d-flex flex-column mr-4">
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="bright" v-model="skills.skill1">
            <span class="display-4">밝기/대비 조정</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="color" v-model="skills.skill2">
            <span class="display-4">색 보정</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="audio" v-model="skills.skill3">
            <span class="display-4">오디오/음악</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="motion" v-model="skills.skill4">
            <span class="display-4">모션그래픽</span>
          </base-checkbox>
        </div>
      </div>
      <div class="d-flex flex-column">
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="caption" v-model="skills.skill5">
            <span class="display-4">자막</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="intro" v-model="skills.skill6">
            <span class="display-4">인트로</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" name="outro" v-model="skills.skill7">
            <span class="display-4">아웃트로</span>
          </base-checkbox>
        </div>
        <div class="d-inline-flex p-2">
          <base-checkbox class="custom-control-alternative" :name="etc" v-model="skills.skill8">
            <input type="text" placeholder="기타" v-model="etc" />
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
      skills: {
        skill1: false,
        skill2: false,
        skill3: false,
        skill4: false,
        skill5: false,
        skill6: false,
        skill7: false,
        skill8: false,
      },
      etc: null,
    };
  },
  validations() {
    // 기타 선택 시 input에 값이 입력되었을 때만 validation 걸어주기
    if (this.etc === null || this.etc === '') {
      return {
        skills: {
          required
        }
      }
    } else {
      return {
        etc: {
          required
        }
      }
    }
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
        this.$v.skills.$touch();
        this.setSkills()
      }
    },
    skills: {
      deep: true,
      handler(val) {
        this.$store.commit('setSkills', {value: val})
      }
    },
    etc(val) {
      this.$store.commit('setOtherSkill', {value: val})
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
