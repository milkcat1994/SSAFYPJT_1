<template>
  <div class="container mx-auto my-4 py-4">
    <h1 class="display-3 text-center">추가하실 편집 기술이 있으신가요?</h1>
    <div class="text-center mb-4">
      <small>(제목에 마우스를 올리면 설명을 볼 수 있습니다)</small>
    </div>
    <div class="container">
      <div class="row d-flex justify-content-center">
        <div
          class="zoom card p-0 col-md-6 col-lg-3 text-center"
          style="margin: 0.5rem 1rem;"
          v-for="item of videoSkills"
          :key="item.value"
          :class="{selected: !!item.status}"
          @click="selectVideoSkills(item.value)"
        >
          <div class="card-body">
            <img class="icon mb-2" :src="item.img" :alt="item.name" />
            <div class="card-title mt-2 mb-0">
              <b>{{item.name}}</b>
              <div class="tooltip-text">
                {{
                item.description
                }}
              </div>
            </div>
            <!-- <p class="card-text">{{item.description}}</p> -->
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
.card {
  min-height: 100px;
  cursor: pointer;
}
.icon {
  /* display: inline-block; */
  width: 100px;
  height: 100px;
}
.selected {
  border: 1px solid darkblue;
}

.card-title {
  /* position: relative; */
  /* display: inline-block; */
  /* border-bottom: 1px dotted black; */
}

.card-title .tooltip-text {
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

.card-title:hover .tooltip-text {
  visibility: visible;
}

.zoom:hover {
  -ms-transform: scale(1.1);
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
  z-index: 2;
}
</style>
