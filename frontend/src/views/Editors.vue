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
            <span class="text-eunjung font-weight-bold">영상 목적</span>
          </div>
          <div class="d-inline-flex m-2 p-2">
            <button
              class="btn btn-sm"
              :class="{
                'btn-success': !!item.status,
                'btn-outline-eunjung': !item.status,
              }"
              v-for="item in videoType"
              :key="item.name"
              @click="toggleFilter(item)"
            >
              <span class="mx-2">{{ item.name }}</span>
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
                'btn-success': !!item.status,
                'btn-outline-eunjung': !item.status,
              }"
              v-for="item in videoStyle"
              :key="item.name"
              @click="toggleFilter(item)"
            >
              <span class="mx-2">{{ item.name }}</span>
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
              v-for="skill in videoSkills"
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
            <button class="btn btn-success btn-sm" v-for="category in selectedFilters" :key="category" @click="clearFilter(category)">{{category}}</button>
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
// import http from "@/util/http-common";
// import { mapGetters } from "vuex";
import EditorsList from "./Editors/EditorsList";
export default {
  name: "editors",
  components: {
    EditorsList,
  },
  data() {
    return {
      // 검색 필터 모음
      videoType: [
        {name: '개인', value: 'pers', status: false},
        {name: '상업', value: 'comm', status: false},
      ],
      videoStyle: [
        {name: '키즈', value: 'kids', status: false}, 
        {name: '게임', value: 'game', status: false}, 
        {name: '음악/댄스', value: 'musi', status: false}, 
        {name: '푸드/쿠킹', value: 'food', status: false},
        {name: 'V-log', value: 'vlog', status: false},
        {name: '영화/애니메이션', value: 'movi', status: false},
        {name: '동물', value: 'anim', status: false},
        {name: '뷰티/패션', value: 'beau', status: false},
        {name: '스포츠', value: 'spor', status: false},
        {name: '기타', value: 'etcs', status: false},
      ],
      videoSkills: [
        {name: '색/밝기 조정', value: 'colr', status: false}, 
        {name: '자막', value: 'capt', status: false}, 
        {name: '오디오/음악', value: 'audi', status: false}, 
        {name: '인트로', value: 'intr', status: false}, 
        {name: '아웃트로', value: 'outr', status: false}, 
        {name: '모션그래픽', value: 'moti', status: false}, 
      ],
      // 검색 기준
      searchBy: "검색",
      // 검색 단어
      keyword: "",
      // 선택한 필터들
      selectedFilters: [],
    };
  },
  created() {
    
  },
  methods: {
    // 이름 또는 단어 검색
    searchKeyword() {      

      // 검색어
      // 정렬 방식 == this.searchBy
      if (this.searchBy == '이름') {
        console.log(this.keyword);
        // 이름 검색 API 요청
      } else if (this.searchBy == '태그') {
        console.log(this.keyword);
        // 태그 검색 API 요청
      }

      // Vuex가 아닌 현재 컴포넌트에 있는 필터링 데이터를 기반으로 검색
      console.log(this.videoType);
      console.log(this.videoStyle);
      console.log(this.videoSkills);
      // 버튼이 눌릴 때마다 리스트를 업데이트할 것인지,
      // '적용' 버튼을 사용해 요청할 것인지 결정해야함

    },
    toggleFilter(val) {
      if (val.status) {
        this.clearFilter(val.value);
        val.status = false;
      } else {
        this.selectedFilters.push(val.value);
        val.status = true;
      }
    },
    clearFilter(value) {
      let index = this.selectedFilters.indexOf(value);
      if (index > -1) {
        this.selectedFilters.splice(index, 1);
      }
      this.videoType.forEach(e => {
        if (e.value === value) {
          e.status = false;
        }
      });
      this.videoStyle.forEach(e => {
        if (e.value === value) {
          e.status = false;
        }
      });
      this.videoSkills.forEach(e => {
        if (e.value === value) {
          e.status = false;
        }
      });
    },
    clearFilterAll() {
      // 필터된 카테고리들 토글(활성화 <-> 비활성화)
      // 작업요청서 필터(Vuex)와 동기화하지 않고 지역 변수 사용
      (this.videoType = [
        {name: '개인', value: 'pers', status: false},
        {name: '상업', value: 'comm', status: false},
      ]),
      (this.videoStyle = [
        {name: '키즈', value: 'kids', status: false}, 
        {name: '게임', value: 'game', status: false}, 
        {name: '음악/댄스', value: 'musi', status: false}, 
        {name: '푸드/쿠킹', value: 'food', status: false},
        {name: 'V-log', value: 'vlog', status: false},
        {name: '영화/애니메이션', value: 'movi', status: false},
        {name: '동물', value: 'anim', status: false},
        {name: '뷰티/패션', value: 'beau', status: false},
        {name: '스포츠', value: 'spor', status: false},
        {name: '기타', value: 'etcs', status: false},
      ]),
      (this.videoSkills = [
        {name: '색/밝기 조정', value: 'colr', status: false}, 
        {name: '자막', value: 'capt', status: false}, 
        {name: '오디오/음악', value: 'audi', status: false}, 
        {name: '인트로', value: 'intr', status: false}, 
        {name: '아웃트로', value: 'outr', status: false}, 
        {name: '모션그래픽', value: 'moti', status: false}, 
      ]),
      // selectedFilters 배열 clear
      (this.selectedFilters.length = 0);
    },
    fetchFilter() {
      let selectedType = this.$store.getters['stepper/getSelectedVideoType']
      if (selectedType) {
        this.videoType.forEach(item => {
          if (item.value == selectedType) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
      let selectedStyle = this.$store.getters['stepper/getSelectedVideoStyle']
      if (selectedStyle) {
        this.videoStyle.forEach(item => {
          if (item.value == selectedStyle) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
      let selectedSkills = this.$store.getters['stepper/getSelectedVideoSkills']
      if (selectedSkills) {
        this.videoSkills.forEach(item => {
          if (selectedSkills.includes(item.value)) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
    }
  },
};
</script>
<style scoped></style>
