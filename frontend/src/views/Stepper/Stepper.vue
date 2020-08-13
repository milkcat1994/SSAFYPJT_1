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
          <h3 class="text-center mt-2">헤더(progress bar) 들어갈 자리</h3>
        </div>
        <div class="card-body border-0">
          <video-type v-if="currentStep === 0"></video-type>
          <video-style v-if="currentStep === 1"></video-style>
          <video-skills v-if="currentStep === 2"></video-skills>
          <video-origin-length v-if="currentStep === 3"></video-origin-length>
          <video-final-length v-if="currentStep === 4"></video-final-length>
          <video-deadline v-if="currentStep === 5"></video-deadline>
        </div>
        <div class="card-footer d-flex justify-content-end">
          <div class="px-2">
            <a href="#" @click.prevent="backStep">이전</a>
            <button class="btn btn-primary ml-4" @click.prevent="nextStep">다음</button>
            </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
// import http from "@/util/http-common";
// import { mapGetters } from 'vuex';

import VideoType from './VideoType.vue';
import VideoStyle from './VideoStyle.vue';
import VideoSkills from './VideoSkills.vue';
import VideoOriginLength from './VideoOriginLength.vue';
import VideoFinalLength from './VideoFinalLength.vue';
import VideoDeadline from './VideoDeadline.vue';

export default {
  name: "stepper",
  components: {
    // HorizontalStepper,
    VideoType,
    VideoStyle,
    VideoSkills,
    VideoOriginLength,
    VideoFinalLength,
    VideoDeadline,
  },
  data() {
    return {
      currentStep: 0,
      searchSteps: [
        {
          icon: "help",
          name: "first",
          title: "영상 종류",
          subtitle: "",
          component: VideoType,
          completed: false,
        },
        {
          icon: "help",
          name: "second",
          title: "영상 특징",
          subtitle: "",
          component: VideoStyle,
          completed: false,
        },
        {
          icon: "help",
          name: "fifth",
          title: "편집 기술",
          subtitle: "",
          component: VideoSkills,
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
          name: "sixth",
          title: "희망 기한",
          subtitle: "",
          component: VideoDeadline,
          completed: false,
        },
      ],
    };
  },
  methods: {
    backStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      } else {
        // this.currentStep = 0
        this.$router.push({name: 'mainpage'})
      }
    },
    nextStep() {
      if (this.currentStep < 5) {
        this.currentStep++;
      } else {
        // after final step
        this.$router.push("/editors");
      }
    },
    // Vuex stepper 초기화
    clearSteps() {
      this.$store.commit('stepper/clearStepperStatus')
    }
    // 각 단계 verification 필요 ==> 다음 버튼 활성화

  },
  created() {
    this.clearSteps()
  }
};
</script>

<style>
</style>