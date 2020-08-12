<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-2 text-center">추가하실 편집 기술이 있으신가요?</h1>

    <div class="container">
      <div class="row d-flex justify-content-center">
        <div class="card col-4 col-md-3 m-2 p-2 text-center"
            v-for="item of videoSkills"
            :key="item.value"
            :class="{selected: !!item.status}"
            @click="selectVideoSkills(item.value)">
          <div class="card-body">
            <img class="icon" :src="item.img" :alt="item.name">
            <h2 class="card-text mt-2">{{item.name}}</h2>
            <!-- <span v-if="skill.name === 'motion'"
                  :data-tooltip-text="tooltip"> -->
              <!-- <i class="far fa-question-circle fa-md"></i>
            </span> -->
          </div>
          <!-- <i v-if="!!skill.status" class="fas fa-check-circle fa-sm"></i> -->
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import { mapState } from "vuex";

export default {
  name: "video-skills",
  computed: {
    ...mapState({
      videoSkills: state => state.stepper.videoSkills
    }),
  },
  methods: {
    selectVideoSkills(value) {
      this.$store.commit('stepper/selectVideoSkills', value)
    },
  },
};
</script>
<style scoped>
.card {
  min-height: 100px;
}
.icon {
  /* display: inline-block; */
  width: 100px;
  height: 100px;
}
.selected {
  border: 1px solid darkblue;
}
</style>
