<template>
  <div class="d-flex justify-content-center">
    <div class="border-top-0 text-center mx-4 py-1"
      v-for="stage of stages"
      :key="stage.name"
      :class="{done: !!stage.status, notyet: !stage.status}"
      @click="changeStep(stage.num)">
      <span class="stage-name">{{stage.name}}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'progress-bar',
  data() {
    return {
      initStages: [
        {name: '편집 목적', num: 0, status: false},
        {name: '영상 종류', num: 1, status: false},
        {name: '편집 스킬', num: 2, status: false},
        {name: '원본 길이', num: 3, status: false},
        {name: '완성 길이', num: 4, status: false},
        {name: '마감 기한', num: 5, status: false},
      ]
    }
  },
  computed: {
    stages() {
      this.initStages.forEach(stage => {
        if (this.$store.getters['stepper/isChecked'](stage.num)) {
          stage.status = true
        } else {
          stage.status = false
        }
      })
      return this.initStages
    }
  },
  methods: {
    changeStep(stageNum) {
      this.$emit('change-step', stageNum)
    },
  },
}
</script>

<style scoped>
.done {
  cursor: pointer;
  border-bottom: 8px solid darkblue;
}
.notyet {
  cursor: default;
  border-bottom: 8px solid lightgray;
}
.stage-name {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>