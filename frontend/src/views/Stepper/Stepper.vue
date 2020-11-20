<template>
  <div>
    <div class="bg-gradient-success pb-8 pt-8">
      <div class="container">
        <div class="row">
          <div class="col-md-12 col-lg-6">
            <search-card
              title="단계별 질문을 통해 원하는 편집 방향을 설정해주세요"
              type="gradient-info"
              sub-title="작업 요청서 작성하기"
              icon="ni ni-scissors"
              class="mb-0 mb-xl-0"
            ></search-card>
          </div>
        </div>
      </div>
    </div>

    <div class="container mt--7 mb-8">
      <div class="card shadow border-0">
        <div class="card-header pb-0">
          <progress-bar :currentStep="currentStep" @change-step="changeStep"></progress-bar>
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
            <button class="btn ex-btn" @click.prevent="backStep">이전</button>
            <button class="btn btn-primary ml-4" @click.prevent="nextStep">다음</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import alertify from "alertifyjs";

import ProgressBar from "./ProgressBar.vue";
import VideoType from "./VideoType.vue";
import VideoStyle from "./VideoStyle.vue";
import VideoSkills from "./VideoSkills.vue";
import VideoOriginLength from "./VideoOriginLength.vue";
import VideoFinalLength from "./VideoFinalLength.vue";
import VideoDeadline from "./VideoDeadline.vue";

export default {
  name: "stepper",
  components: {
    ProgressBar,
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
    };
  },
  methods: {
    backStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      } else {
        this.$router.push({ name: "mainpage" });
      }
    },
    changeStep(stepNum) {
      if (this.$store.getters["stepper/isChecked"](stepNum)) {
        this.currentStep = stepNum;
      }
    },
    // 각 단계 verification 필요 ==> 다음 버튼 활성화
    nextStep() {
      if (this.currentStep < 5) {
        switch (this.currentStep) {
          case 0:
            if (!this.$store.getters["stepper/getSelectedVideoType"]) {
              alertify.notify("편집 목적을 선택해주세요", "warning", 3);
            } else {
              this.currentStep++;
            }
            break;
          case 1:
            if (!this.$store.getters["stepper/getSelectedVideoStyle"]) {
              alertify.notify("영상 종류를 선택해주세요", "warning", 3);
            } else {
              this.currentStep++;
            }
            break;
          case 2:
            if (
              this.$store.getters["stepper/getSelectedVideoSkills"].length === 0
            ) {
              alertify.notify("편집 스킬을 선택해주세요", "warning", 3);
            } else {
              this.currentStep++;
            }
            break;
          case 3:
            if (!this.$store.getters["stepper/getOriginLength"]) {
              alertify.notify("원본 길이를 선택해주세요", "warning", 3);
            } else {
              this.currentStep++;
            }
            break;
          case 4:
            if (!this.$store.getters["stepper/getFinalLength"]) {
              alertify.notify("완성 길이를 선택해주세요", "warning", 3);
            } else {
              this.currentStep++;
            }
            break;
        }
      } else {
        // after final step
        if (this.$store.getters["stepper/getDeadline"].length < 2) {
          alertify.notify("마감 기한을 선택해주세요", "warning", 3);
        } else {
          this.$router.push("/editors");
        }
      }
    },
    // Vuex stepper 초기화
    clearSteps() {
      this.$store.commit("stepper/clearStepperStatus");
    },
  },
  created() {
    this.clearSteps();
  },
};
</script>

<style scoped>
#maincard {
  box-shadow: 3px 3px 3px gray;
}

.ex-btn:hover{
  box-shadow:none;
}
</style>