<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-0">희망 완료일을 알려주세요!</h1>

    <div class="container my-4 py-4">
      <base-input addon-left-icon="ni ni-calendar-grid-58">
        <flat-picker
          v-model="dates"
          slot-scope="{focus, blur}"
          @on-open="focus"
          @on-close="blur"
          @on-change="doSomethingOnChange"
          :config="config"
          class="form-control datepicker"
          placeholder="시작날짜 - 마감날짜"
        ></flat-picker>
      </base-input>
    </div>
  </div>
</template>
<script>
import flatPicker from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";

export default {
  name: "video-deadline",
  components: { flatPicker },
  methods: {
    setDeadline(date) {
      this.$store.commit('stepper/setDeadline', date)
    },
    doSomethingOnChange(selectedDates, dateStr){
      console.log(selectedDates)
      // dateStr: Y-m-d
      console.log(dateStr)
      this.$store.commit('stepper/setDeadline', dateStr)
    }
  },
  data() {
    return {
      dates: new Date(),
      config: {
        allowInput: true,
        mode: 'range',
        minDate: "today",
      }
    };
  },
};
</script>
<style>
</style>
