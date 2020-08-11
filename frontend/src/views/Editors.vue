<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- 카테고리 필터 -->
      <div class="filter-finder d-flex flex-column mx-4 mb-4">
        <!-- 영상 종류 -->
        <div class="video-type px-4 mb-2 bg-white rounded-pill shadow-sm">
          <div
            class="d-inline-flex justify-content-center align-items-center mx-2 p-2"
          >
            <span class="text-eunjung font-weight-bold">영상 목적 </span>
          </div>
          <div class="d-inline-flex m-2 p-2">
            <button
              class="btn btn-sm"
              :class="{
                'btn-success': !!type.status,
                'btn-outline-eunjung': !type.status,
              }"
              v-for="type in videoTypes"
              :key="type.name"
              @click="toggleFilter(type)"
            >
              <span class="mx-2">{{ type.name }}</span>
            </button>
          </div>
        </div>
        <!-- 영상 특징 -->
        <div class="video-type px-4 mb-2 bg-white rounded-pill shadow-sm">
          <div
            class="d-inline-flex justify-content-center align-items-center mx-2 p-2"
          >
            <span class="text-eunjung font-weight-bold">영상 종류</span>
          </div>
          <div class="d-inline-flex m-2 p-2">
            <button
              class="btn btn-sm"
              :class="{
                'btn-success': !!theme.status,
                'btn-outline-eunjung': !theme.status,
              }"
              v-for="theme in videoTheme"
              :key="theme.name"
              @click="toggleFilter(theme)"
            >
              <span class="mx-2">{{ theme.name }}</span>
            </button>
          </div>
        </div>
        <!-- 편집 기술 -->
        <div class="edit-skill px-4 mb-2 bg-white rounded-pill shadow-sm">
          <div
            class="d-inline-flex justify-content-center align-items-center mx-2 p-2"
          >
            <span class="text-eunjung font-weight-bold">편집 기술</span>
          </div>
          <div class="d-inline-flex m-2 p-2">
            <button
              class="btn btn-sm"
              :class="{
                'btn-success': !!skill.status,
                'btn-outline-eunjung': !skill.status,
              }"
              v-for="skill in editSkills"
              :key="skill.name"
              @click="toggleFilter(skill)"
            >
              <span class="mx-2">{{ skill.name }}</span>
            </button>
          </div>
        </div>
        <!-- 선택된 필터 표시 바 -->
        <!-- <div class="filter-selected row d-flex align-items-stretch justify-content-between mx-4 p-2 bg-white rounded"> -->
        <!-- 전체 해제 -->
        <!-- <div class="m-2 p-2">
            <button class="btn btn-primary btn-sm" @click="clearFilterAll"><i class="fa fa-redo-alt"></i> 전체 해제</button>
          </div> -->
        <!-- 필터들 -->
        <!-- <div class="m-2 p-2">
            <button class="btn btn-success btn-sm" v-for="category in selectedCategories" :key="category" @click="clearFilter(category)">{{category}}</button>
          </div> -->
        <!-- 적용 버튼 -->
        <!-- <div class="m-2 p-2">
            <button class="btn btn-primary btn-sm" style="font-size: 0.6rem;">적용</button>
          </div> -->
        <!-- </div> -->
      </div>
      <!-- 검색 바 -->
      <div class="d-flex justify-content-center mx-4 px-2">
        <base-dropdown>
          <base-button slot="title" type="secondary" class="dropdown-toggle">{{
            searchBy
          }}</base-button>
          <a class="dropdown-item" href="#" @click.prevent="searchBy = '이름'"
            >이름</a
          >
          <a class="dropdown-item" href="#" @click.prevent="searchBy = '태그'"
            >태그</a
          >
        </base-dropdown>
        <base-input
          placeholder="검색어를 입력해보세요"
          v-model="keyword"
          @keyup.enter="searchKeyword"
          style="width:100%"
        ></base-input>
      </div>
    </base-header>

    <div class="container-fluid mt--7 mb-5">
      <!-- 편집자 목록 -->
      <editors-list title="편집자"></editors-list>
    </div>
  </div>
</template>
<script>
import EditorsList from "./Editors/EditorsList";
// import http from "@/util/http-common"
export default {
  name: "editors",
  components: {
    EditorsList,
  },
  data() {
    return {
      editors: {},
      videoTypes: [
        { name: "개인용", status: false },
        { name: "상업용", status: false },
      ],
      videoTheme: [
        { name: "키즈", status: false },
        { name: "게임", status: false },
        { name: "음악/댄스", status: false },
        { name: "푸드/쿠킹", status: false },
        { name: "브이로그", status: false },
        { name: "영화/애니메이션", status: false },
        { name: "동물", status: false },
        { name: "뷰티/패션", status: false },
        { name: "스포츠", status: false },
        { name: "기타", status: false },
      ],
      editSkills: [
        { name: "색, 밝기 조정", status: false },
        { name: "자막", status: false },
        { name: "오디오/음악", status: false },
        { name: "모션그래픽", status: false },
        { name: "인트로", status: false },
        { name: "아웃트로", status: false },
      ],
      // 검색 기준
      searchBy: "검색",
      // 검색 단어
      keyword: "",
      // 검색하고자 하는 카테고리 모음
      selectedCategories: [],
    };
  },
  methods: {
    fetchList() {
      // http.get('/search/list')
      //   .then(res => {
      //     console.log(res)
      //   })
      //   .catch(err => {
      //     console.error(err)
      //   })
      // return
    },
    // 이름 또는 단어 검색
    searchKeyword() {
      console.log(this.keyword);
    },
    toggleFilter(category) {
      // 로직 수정 필요
      console.log(category);
      if (category.status) {
        this.clearFilter(category.name);
        category.status = false;
      } else {
        this.selectedCategories.push(category.name);
        category.status = true;
      }
    },
    clearFilter(value) {
      let index = this.selectedCategories.indexOf(value);
      if (index > -1) {
        this.selectedCategories.splice(index, 1);
      }
      this.videoTypes.forEach((filter) => {
        if (filter.name === value) {
          filter.status = false;
        }
      });
      this.videoTheme.forEach((filter) => {
        if (filter.name === value) {
          filter.status = false;
        }
      });
      this.editSkills.forEach((filter) => {
        if (filter.name === value) {
          filter.status = false;
        }
      });
    },
    clearFilterAll() {
      // 필터된 카테고리들 토글(활성화 <-> 비활성화)
      (this.videoTypes = [
        { name: "개인용", status: false },
        { name: "상업용", status: false },
      ]),
        (this.videoTheme = [
          { name: "키즈", status: false },
          { name: "게임", status: false },
          { name: "음악/댄스", status: false },
          { name: "푸드/쿠킹", status: false },
          { name: "브이로그", status: false },
          { name: "영화/애니메이션", status: false },
          { name: "동물", status: false },
          { name: "뷰티/패션", status: false },
          { name: "스포츠", status: false },
          { name: "기타", status: false },
        ]),
        (this.editSkills = [
          { name: "색, 밝기 조정", status: false },
          { name: "자막", status: false },
          { name: "오디오/음악", status: false },
          { name: "모션그래픽", status: false },
          { name: "인트로", status: false },
          { name: "아웃트로", status: false },
        ]),
        // selectedCategories 배열 clear
        (this.selectedCategories.length = 0);
    },
  },
  mounted() {
    // this.fetchList()
  },
};
</script>
<style scoped></style>
