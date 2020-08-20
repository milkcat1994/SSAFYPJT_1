<template>
  <div class="container mx-auto py-4" style="width: 80%">

    <h1 class="display-3 mb-0 text-center">영상 편집의 용도/목적을 알려주세요!</h1>

    <div class="container py-4">
      <div class="row">
        <!-- 개인 -->
        <div class="col-lg-6 col-md-12 p-4 zoom" v-for="item of videoType" :key="item.value">
          <div
            class="card text-center"
            :class="{selected: !!item.status}"
            @click.prevent="selectVideoType(item.value)"
          >
            <div class="card-body">
              <img class="icon mb-4" :src="item.img" :alt="item.name" />
              <h3 class="card-title">{{item.name}}</h3>
              <p class="card-text">{{item.description}}</p>
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
  name: "video-type",
  computed: {
    ...mapState({
      videoType: (state) => state.stepper.videoType,
    }),
  },
  methods: {
    selectVideoType(value) {
      this.$store.commit("stepper/selectVideoType", value);
    },
  },
};
</script>
<style scoped>
.card {
  min-height: 300px;
  cursor: pointer;
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
.zoom:hover {
  -ms-transform: scale(1.1);
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
