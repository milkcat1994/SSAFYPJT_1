<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">

    <h1 class="display-3 mb-0 text-center">편집된 영상의 길이를 선택해주세요</h1>
    
    <div class="container py-4">

      <div class="row d-flex justify-content-center">

        <!-- Time Picker로 UI 바꾸기!!! -->
        <div class="card text-center col-sm-2 m-2" 
            v-for="(length, index) in lengths"
            :class="{selected: !!length.status}"
            :key="length.min + index"
            @click.prevent="selectLength(length.min)">
          <div class="card-body px-0 pt-4">
            <h2 class="card-title">{{length.min}}</h2>
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
  name: "video-final-length",
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
      final: null,
      lengths: [
        {
          min: '1분 이내',
          status: false,
        },
        {
          min: '5분 이내',
          status: false,
        },
        {
          min: '10분 이내',
          status: false,
        },
        {
          min: '30분 이내',
          status: false,
        },
        {
          min: '30분 이상',
          status: false,
        },
      ]
    };
  },
  methods: {
    selectLength(min) {
      this.lengths.forEach(e => {
        if (e.min === min) {
          e.status = true
          this.final = min
        } else {
          e.status = false
        }
      })
    },
  },
  validations: {
    final: {
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
        this.$v.final.$touch();
        this.setFinalLength()
      }
    },
    final(val) {
      this.$store.commit('setFinalLength', {value: val})
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
.selected {
  border: solid thick darkblue;
}
</style>
