<template>
  <div class="container mx-auto py-4" style="width: 80%;">
    
    <h1 class="display-3 mb-4 text-center">어떤 종류의 영상인가요?</h1>

    <div class="container py-4">
      <div class="row">
        <!-- 영상 종류별 버튼 -->
        <div class="col-xl-3 col-lg-4 col-md-6" v-for="item of videoStyle" :key="item.value">
          <button
            type="button"
            class="btn my-2"
            style="min-width: 180px; min-height: 60px; !important"
            :class="{'btn-primary': !!item.status, 'btn-outline-primary': !item.status}"
            @click="selectVideoStyle(item.value)"
          >
            <div>
              <i :class="item.icon"></i>
              <span>{{item.name}}</span>
              <i v-if="!!item.status" class="fas fa-check fa-sm"></i>
            </div>
          </button>
        </div>

      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";

export default {
  name: "video-style",
  computed: {
    ...mapState({
      videoStyle: state => state.stepper.videoStyle
    }),
  },
  methods: {
    selectVideoStyle(value) {
      this.$store.commit('stepper/selectVideoStyle', value)
    }
  }
};
</script>
<style scoped>
.card {
  min-height: 757px;
  cursor: pointer;
}
</style>
