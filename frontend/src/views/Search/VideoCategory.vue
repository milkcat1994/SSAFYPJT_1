<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">

    <h1 class="display-3 mb-0 text-center">영상 편집의 용도/목적을 알려주세요!</h1>

    <div class="container py-4">
      <div class="row">
        <!-- 개인 -->
        <div class="col-sm-6 p-4">
          <div class="card text-center" :class="{selected: isPersonalSelected}" @click.prevent="category = 'personal'">
            <div class="card-body">
              <img class="icon mb-4" src="img\icons\search\personal.svg" alt="">
              <h3 class="card-title">개인용</h3>
              <p class="card-text">일상의 즐거움을 담은 vlog, 결혼식이나 돌잔치 영상 등 개인 영상을 편집하시는 경우 선택해주세요</p>
            </div>
          </div>
        </div>
        <!-- 소장 -->
        <div class="col-sm-6 p-4">
           <div class="card text-center" :class="{selected: isCommercialSelected}" @click.prevent="category = 'commercial'">
            <div class="card-body">
              <img class="icon mb-4" src="img\icons\search\commercial.svg" alt="">
              <h3 class="card-title">상업용</h3>
              <p class="card-text">기업에서 맡기시는 경우 선택해주세요</p>
            </div>
          </div>
        </div>
      </div>
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
  validations() {
    return {
      category: {
        required
      }
    }
  },
  computed: {
    isPersonalSelected() {
      if (this.category == 'personal') {
        return true
      } else {
        return false
      }
    },
    isCommercialSelected() {
      if (this.category == 'commercial') {
        return true
      } else {
        return false
      }
    }
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
        this.$store.commit('setCategory', {value: val})
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
<style scoped>
.card {
  min-height: 300px;
}
.icon {
  /* display: inline-block; */
  width: 100px;
  height: 100px;
}
.selected {
  border: solid thick darkblue;
}
</style>
