<template>
  <div class="container mx-auto py-4">
    
    <h1 class="display-3 text-center">추가하실 편집 기술이 있으신가요?</h1>
    <div class="text-center mb-4">
      <small>(제목에 마우스를 올리면 설명을 볼 수 있습니다 / 중복선택 가능)</small>
    </div>
    <div class="container">
      <div class="row d-flex justify-content-center">
        <div
          class="zoom p-0 col-md-6 col-lg-3 mb-2 text-center"
          style="margin: 0.5rem 1rem;"
          v-for="item of videoSkills"
          :key="item.value"
          :class="{ selected: !!item.status }"
          @click="selectVideoSkills(item.value)"
        >
          <div class="check">
            <img class="icon my-4" :src="item.img" :alt="item.name" />
            <div class="title mt-2 mb-3">
              <b>{{ item.name }}</b>
              <div class="tooltip-text h5 mx-2">
                {{ item.description }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";

export default {
  name: "video-skills",
  data() {
    return {
      activeItem: null,
    };
  },
  computed: {
    ...mapState({
      videoSkills: (state) => state.stepper.videoSkills,
    }),
  },
  methods: {
    selectVideoSkills(value) {
      this.$store.commit("stepper/selectVideoSkills", value);
    },
  },
};
</script>
<style scoped>
.check {
  min-height: 100px;
  cursor: pointer;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-radius: 0.375rem;
}
.icon {
  /* display: inline-block; */
  width: 100px;
  height: 100px;
}
.selected {
  border: 1px solid darkblue;
  border-radius: 0.375rem;
}

.check .tooltip-text {
  visibility: hidden;
  width: 220px;
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;

  /* Position the tooltip */
  position: absolute;
  /* left: 0; */
  z-index: 99999 !important;
}

.check:hover .tooltip-text {
  visibility: visible;
}

.zoom:hover {
  -ms-transform: scale(1.1);
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
  z-index: 2;
}
</style>
