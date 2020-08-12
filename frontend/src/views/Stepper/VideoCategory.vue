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

      <!-- <base-radio class="custom-control-alternative" :name="etc" v-model="category">
        <input type="text" placeholder="기타" v-model.lazy="etc" />
      </base-radio> -->
      
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
      etc: '',
    }
  },
  validations() {
    // 기타 선택 시 input에 값이 입력되었을 때만 validation 걸어주기
    return {
      category: {
        required
      }
    }
    // if (this.category === 'etc') {
    //   return {
    //     etc: {
    //       required
    //     }
    //   }
    // } else {
    //   return {
    //     category: {
    //       required
    //     }
    //   }
    // }
  },
  watch: {
    $v: {
      deep: true,
      handler: function (val) {
        if (!val.$invalid) {
          this.$emit("can-continue", { value: true });
        } else {
          this.$emit("can-continue", { value: false });
        }
      },
    },
    clickedNext(val) {
      if (val === true) {
        this.$v.category.$touch();
      }
    },
    category(val) {
      // if (val !== 'etc') {
        this.$store.commit('setCategory', {value: val})
      // }
    },
    // etc(val) {
    //   if (val === 'etc') {
    //     this.$store.commit('setCategory', {value: val})
    //   }
    // },
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
