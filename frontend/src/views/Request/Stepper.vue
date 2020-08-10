<template>
  <section class="section">
    <div class="container my-4">
      <div class="columns">
        <div class="column is-8 is-offset-2">
          <horizontal-stepper
            :steps="searchSteps"
            @completed-step="completeStep"
            @active-step="isStepActive"
            @stepper-finished="sendRequest"
          ></horizontal-stepper>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import http from "@/util/http-common";
import HorizontalStepper from "vue-stepper";
import { mapGetters } from 'vuex';

import VideoCategory from './VideoCategory.vue';
import VideoTheme from './VideoTheme.vue';
import VideoOriginLength from './VideoOriginLength.vue';
import VideoFinalLength from './VideoFinalLength.vue';
import EditSkill from './EditSkill.vue';
import EditTerm from './EditTerm.vue';

const URL = http.baseURL;

export default {
  name: "stepper",
  components: {
    HorizontalStepper,
  },
  data() {
    return {
      searchSteps: [
        {
          icon: "help",
          name: "first",
          title: "영상 종류",
          subtitle: "",
          component: VideoCategory,
          completed: false,
        },
        {
          icon: "help",
          name: "second",
          title: "영상 특징",
          subtitle: "",
          component: VideoTheme,
          completed: false,
        },
        {
          icon: "help",
          name: "third",
          title: "원본 길이",
          subtitle: "",
          component: VideoOriginLength,
          completed: false,
        },
        {
          icon: "help",
          name: "fourth",
          title: "영상 길이",
          subtitle: "",
          component: VideoFinalLength,
          completed: false,
        },
        {
          icon: "help",
          name: "fifth",
          title: "편집 기술",
          subtitle: "",
          component: EditSkill,
          completed: false,
        },
        {
          icon: "help",
          name: "sixth",
          title: "희망 기한",
          subtitle: "",
          component: EditTerm,
          completed: false,
        },
      ],
    };
  },
  computed: {
    ...mapGetters(['getRequests']),
  },
  methods: {
    // Executed when @completed-step event is triggered
    completeStep(payload) {
      this.searchSteps.forEach((step) => {
        if (step.name === payload.name) {
          step.completed = true;
        }
      });
    },
    // Executed when @active-step event is triggered
    isStepActive(payload) {
      this.searchSteps.forEach((step) => {
        if (step.name === payload.name) {
          if (step.completed === true) {
            step.completed = false;
          }
        }
      });
    },
    // Executed when @stepper-finished event is triggered
    sendRequest() {
      // 종료되었을 때 현재 작업요청서에 저장된 내용을 보내기
      console.log(this.getRequests)
      this.$router.push("/editors");
    },
  },
  created() {
    console.log(URL)
  }
};
</script>

<style>
</style>