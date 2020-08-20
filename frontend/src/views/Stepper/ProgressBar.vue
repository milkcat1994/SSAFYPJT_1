<template>
  <div class="d-flex justify-content-center">
    <div
      class="border-top-0 text-center px-4 py-1"
      v-for="stage of stages"
      :key="stage.name"
      :class="{ done: !!stage.status, notyet: !stage.status,current: currentStep == stage.num }"
      @click="changeStep(stage.num)"
    >
      <span class="stage-name"
      >{{ stage.name }}</span>
      <!-- :class ="{current: currentStep == stage.num}" -->
    </div>

    <!-- <div class="d-flex justify-content-center mx-4 p-2 bg-white rounded">
      <div class="m-2 p-2">
        <button class="btn btn-success btn-sm" v-for="filter in selectedFilters" :key="filter">{{filter}}</button>
      </div>
    </div> -->
  </div>
</template>

<script>
export default {
  name: "progress-bar",
  props: ['currentStep'],
  data() {
    return {
      initStages: [
        {name: '편집 목적', value: '없음', num: 0, status: false},
        {name: '영상 종류', value: '없음', num: 1, status: false},
        {name: '편집 스킬', value: '없음', num: 2, status: false},
        {name: '원본 길이', value: '없음', num: 3, status: false},
        {name: '완성 길이', value: '없음', num: 4, status: false},
        {name: '마감 기한', value: '없음', num: 5, status: false},
      ],
    }
  },
  computed: {
    stages() {
      this.initStages.forEach(stage => {
        if (this.$store.getters['stepper/isChecked'](stage.num)) {
          if (stage.num == 0) {
            stage.value = this.$store.getters['stepper/getSelectedVideoType'].name
          } else if (stage.num == 1) {
            stage.value = this.$store.getters['stepper/getSelectedVideoStyle'].name
          } else if (stage.num == 2) {
            let skills = this.$store.getters['stepper/getSelectedVideoSkillsName']
            if (skills.length > 1) {
              stage.value = skills[0] + '외 ' + (skills.length - 1) + '개'
            } else {
              stage.value = skills.toString()
            }
          } else if (stage.num == 3) {
            stage.value = this.$store.getters['stepper/getOriginLength'].name
          } else if (stage.num == 4) {
            stage.value = this.$store.getters['stepper/getFinalLength'].name
          } else if (stage.num == 5) {
            let deadline = this.$store.getters['stepper/getDeadline']
            stage.value = deadline[0] + '부터 ' + deadline[1] + '까지'
          }
          stage.status = true
        } else {
          stage.status = false
          stage.value = '없음'
        }
      })
      return this.initStages
    },
    selectedFilters() {
      let arr = new Array();
      for (const item of this.initStages) {
        if (item.status) {
          arr.push(item.value)
        }
      }
      return arr
    }
  },
  // watch:{
  //   currentStep(){
  //     console.log(this.currentStep)
  //   }
  // },
  methods: {
    changeStep(stageNum) {
      this.$emit("change-step", stageNum);
    },
  },
};
</script>

<style scoped>
.done {
  cursor: pointer;
  border-bottom: 10px solid darkblue;
}
.notyet {
  cursor: default;
  border-bottom: 10px solid lightgray;
}

.stage-name {
  font-size: 1.5rem;
  font-weight: bold;
}

.current{
  cursor: default;
  border-bottom: 10px solid #bcb9ed ;
}
</style>