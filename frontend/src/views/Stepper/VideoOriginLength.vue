<template>
  <div class="container mx-auto py-4" style="width: 90%">
    
    <h1 class="display-3 mb-0 text-center">원본 영상의 길이를 선택해주세요</h1>

    <div class="container py-4">
      <div class="row d-flex justify-content-center">
        <button
          v-for="item of originLength"
          :key="item.value"
          type="button"
          class="btn my-2"
          style="min-width: 150px; min-height: 60px; !important"
          :class="{'btn-primary': !!item.status, 'btn-outline-primary': !item.status}"
          @click="setOriginLength(item.value)"
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
  name: "video-origin-length",
  computed: {
    ...mapState({
      originLength: (state) => state.stepper.originLength,
    }),
  },
  methods: {
    setOriginLength(value) {
      this.$store.commit("stepper/setOriginLength", value);
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
</style>
