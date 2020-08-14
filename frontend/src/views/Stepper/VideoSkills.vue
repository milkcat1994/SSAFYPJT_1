<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-4 text-center">추가하실 편집 기술이 있으신가요?</h1>
    <div class="container">
      <div class="row d-flex justify-content-center">
        <div class="card col-4 col-md-3 m-2 text-center"
            style="min-width: 275px;"
            v-for="item of videoSkills"
            :key="item.value"
            :class="{selected: !!item.status}"
            @click="selectVideoSkills(item.value)">
          <div class="card-body">
            <img class="icon" :src="item.img" :alt="item.name">
            <h2 class="card-title mt-2" :id="'tootlip-target-'+item.value">{{item.name}}
              <!-- <i :id="'tootlip-target-'+item.value" class="far fa-question-circle fa-md"></i> -->
              <!-- <b-icon :id="'tootlip-target-'+item.value" icon="exclamation-circle-fill" variant="primary"></b-icon> -->
            </h2>
            <p class="card-text">{{item.description}}</p>
            <!-- <b-tooltip :target="'tootlip-target-'+item.value" triggers="hover">{{item.description}}</b-tooltip> -->
          </div>
          <!-- <i v-if="!!skill.status" class="fas fa-check-circle fa-sm"></i> -->
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import { mapState } from "vuex";
// import Vue from "vue";
// import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

// // Install BootstrapVue
// Vue.use(BootstrapVue);
// // Optionally install the BootstrapVue icon components plugin
// Vue.use(IconsPlugin);

export default {
  name: "video-skills",
  data() {
    return {
      activeItem: null
    }
  },
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
