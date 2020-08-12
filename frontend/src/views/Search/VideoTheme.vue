<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0 text-center">어떤 종류의 영상인가요?</h1>

    <div class="container py-4">
      <div class="row">
        <!-- 영상 종류별 버튼 -->
        <div class="col-lg-3 col-md-6" v-for="(theme, index) in themes" :key="theme.name + index">
          <button
            type="button"
            class="btn my-2"
            style="min-width: 200px;"
            :class="{'btn-primary': !!theme.status, 'btn-outline-primary': !theme.status}"
            @click="selectTheme(theme.name)"
          >
            <div>
              <i :class="theme.icon"></i>
              <span>{{theme.name}}</span>
              <i v-if="!!theme.status" class="fas fa-check fa-sm"></i>
            </div>
          </button>
        </div>
      </div>
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
      theme: null,
      themes: [
          // 유튜브 카테고리를 차용
          {
            name: "키즈",
            icon: "fas fa-baby",
            status: false,
          },
          {
            name: "게임",
            icon: "fas fa-gamepad",
            status: false,
          },
          {
            name: "음악/댄스",
            icon: "fas fa-music",
            status: false,
          },
          {
            name: "푸드/쿠킹",
            icon: "fas fa-utensils",
            status: false,
          },
          {
            name: "V-log",
            icon: "fas fa-user",
            status: false,
          },
          {
            name: "영화/애니메이션",
            icon: "fas fa-film",
            status: false,
          },
          {
            name: "동물",
            icon: "fas fa-paw",
            status: false,
          },
          {
            name: "뷰티/패션",
            icon: "fas fa-spray-can",
            status: false,
          },
          {
            name: "스포츠",
            icon: "fas fa-dumbbell",
            status: false,
          },
          {
            name: "기타",
            icon: "fas fa-question",
            status: false,
          },
      ]
    };
  },
  methods: {
    selectTheme(name) {
      this.themes.forEach(e => {
        if (e.name === name) {
          e.status = true
          this.theme = name
        } else {
          e.status = false
        }
      })
    },
  },
  validations() {
    return {
      theme: {
        required,
      },
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
        this.$v.radio.$touch();
      }
    },
    theme(val) {
      this.$store.commit("setTheme", { value: val });
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
