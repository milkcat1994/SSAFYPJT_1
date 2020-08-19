<template>
  <div class="container mx-auto py-4" style="width: 90%">
    
    <h1 class="display-3 mb-0 text-center">편집된 영상의 길이를 선택해주세요</h1>

    <div class="container py-4">
      <div class="row d-flex justify-content-center">
        <button
          v-for="item of finalLength"
          :key="item.value"
          type="button"
          class="btn my-2"
          style="min-width: 150px; min-height: 60px; !important"
          :class="{'btn-primary': !!item.status, 'btn-outline-primary': !item.status}"
          @click="setFinalLength(item.value)"
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
</template>
<script>
import { mapState } from "vuex";

export default {
  name: "video-final-length",
  computed: {
    ...mapState({
      finalLength: (state) => state.stepper.finalLength,
    }),
  },
  methods: {
    setFinalLength(value) {
      this.$store.commit("stepper/setFinalLength", value);
    },
  },
};
</script>
<style scoped>
.card {
  cursor: pointer;
}
.selected {
  border: solid 1px darkblue;
}

.card:hover {
  -webkit-transition-duration: 0.4s;
  transition-duration: 0.4s;
  background-color: darkblue;
  color: white;
}

.card:hover.card-title:hover {
  color: white;
}
</style>
