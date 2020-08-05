<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- Card stats -->
      <div class="row justify-content-center">
        <div class="col-xl-1 col-md-4">
          <base-dropdown>
            <base-button slot="title" type="secondary" class="dropdown-toggle">정렬</base-button>
            <a class="dropdown-item" href="#">이름순</a>
            <a class="dropdown-item" href="#">조회순</a>
            <a class="dropdown-item" href="#">별점순</a>
            <a class="dropdown-item" href="#">경력순</a>
          </base-dropdown>
        </div>
        <div class="col-xl-8 col-md-8">
          <base-input placeholder="검색어를 입력해보세요"></base-input>
        </div>
        <div class="col-xl-1 col-md-12">
          <base-button type="primary">검색</base-button>
        </div>
        <div class="col-xl-1 col-md-12">
          <base-dropdown class="nav-link pr-0">
            <base-button outline slot="title" type="primary" size="sm" class="dropdown-toggle">필터 추가</base-button>
            <template>
              <router-link :to="{name: 'editors'}" class="dropdown-item">
                <!-- <i class="ni ni-single-02"></i> -->
                <span>영상 길이</span>
              </router-link>
              <router-link :to="{name: 'editors'}" class="dropdown-item">
                <!-- <i class="ni ni-settings-gear-65"></i> -->
                <span>영상 종류</span>
              </router-link>
              <router-link :to="{name: 'editors'}" class="dropdown-item">
                <!-- <i class="ni ni-calendar-grid-58"></i> -->
                <span>편집 기술</span>
              </router-link>
              <router-link :to="{name: 'editors'}" class="dropdown-item">
                <!-- <i class="ni ni-support-16"></i> -->
                <span>작업날짜(데드라인)</span>
              </router-link>
            </template>
          </base-dropdown>
        </div>
      </div>
    </base-header>

    <div class="container-fluid mt--7 mb-5">
      <editors-table title="Light Table"></editors-table>
    </div>
  </div>
</template>
<script>
import EditorsTable from "./Tables/EditorsTable";
export default {
  name: "editors",
  components: {
    EditorsTable,
  },
};
</script>
<style></style>
