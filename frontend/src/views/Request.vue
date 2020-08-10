<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-2 pt-md-6">
      <!-- 요청 정보 목록 -->
      <div class="row">
        <div class="col-xl-6 col-lg-8">
          <search-card
            title="단계별 질문을 통해 원하는 편집 방향을 설정해주세요"
            type="gradient-info"
            sub-title="작업 요청서 작성하기"
            icon="ni ni-scissors"
            class="mb-0 mb-xl-0"
          >
          </search-card>
        </div>
      </div>
    </base-header>

    <div class="container-fluid mt--7 mb-4">
      <div class="row">
        <div class="col">
          <div class="card shadow border-0">
            <stepper></stepper>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Stepper from './Request/Stepper'

export default {
  name: "request",
  components: {
    Stepper,
  }
};
</script>
<style>
</style>
