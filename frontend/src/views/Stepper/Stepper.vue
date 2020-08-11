<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-2 pt-md-6">
      <!-- 요청 정보 목록 -->
      <div class="row">
        <div class="col-xl-6 col-lg-8">
          <search-card
            title="단계별 질문을 통해 원하는 편집 방향을 설정해주세요"
            type="gradient-info"
            sub-title="작업 요청서 작성하기"
            icon="ni ni-scissors"
            class="mb-0 mb-xl-0"
          >
          </search-card>
        </div>
      </div>
    </base-header>
    <div class="container-fluid mt--7 mb-4">
      <div class="card shadow border-0">
        <div class="card-header">
          헤더입니다
        </div>
        <div class="card-body border-0">
          바디입니다
        </div>
        <div class="card-footer d-flex justify-content-end">
          <div class="px-2">
            <a href="#">이전</a>
            <button class="btn btn-primary ml-4">다음</button>
            </div>
        </div>
        <!-- <div class="container my-4">
          <div class="column is-8 is-offset-2">
            <horizontal-stepper
              :steps="searchSteps"
              @completed-step="completeStep"
              @active-step="isStepActive"
              @stepper-finished="sendRequest"
            ></horizontal-stepper>
          </div>
        </div> -->
      </div>
    </div>
  </div>

  <!-- <section class="section">
    
  </section> -->
</template>

<script>
// import http from "@/util/http-common";
import { mapGetters } from 'vuex';

import VideoCategory from './VideoCategory.vue';
import VideoTheme from './VideoTheme.vue';
import VideoOriginLength from './VideoOriginLength.vue';
import VideoFinalLength from './VideoFinalLength.vue';
import EditSkill from './EditSkill.vue';
import EditTerm from './EditTerm.vue';

export default {
  name: "stepper",
  components: {
    // HorizontalStepper,
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