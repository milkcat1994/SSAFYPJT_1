<template>
  <div class="d-flex justify-content-center">
    <div class="border-top-0 text-center mx-4"
      v-for="stage of stages"
      :key="stage.name"
      :class="{completed: !!stage.status, ready: !stage.status}">
      <span class="stage-name">{{stage.name}}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'progress-bar',
  props: {
    currentStep: {
      type: Number
    }
  },
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
        if (stage.num <= this.currentStep) {
          stage.status = true
        } else {
          stage.status = false
        }
      })
      return this.initStages
    }
  },
}
</script>

<style scoped>
.card {
  min-width: 100px;
}
.completed {
  border-bottom: 8px solid darkblue;
}
.ready {
  border-bottom: 8px solid lightgray;
}
.stage-name {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>