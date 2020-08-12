<template>
  <div class="container mx-auto my-4 py-4" style="width: 90%">
    
    <h1 class="display-3 mb-0 text-center">편집된 영상의 길이를 선택해주세요</h1>

    <div class="container py-4">

      <div class="row d-flex justify-content-center">

        <div class="card text-center col-sm-2 m-2" 
            v-for="item of finalLength"
            :class="{selected: !!item.status}"
            :key="item.name"
            @click="setFinalLength(item.name)">
          <div class="card-body px-0 pt-4">
            <h2 class="card-title">{{item.name}}</h2>
          </div>
        </div>
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
      finalLength: state => state.stepper.finalLength
    }),
  },
  methods: {
    setFinalLength(name) {
      this.$store.commit('stepper/setFinalLength', name)
    }
  }
};
</script>
<style scoped>
.selected {
  border: solid 1px darkblue;
}
</style>
