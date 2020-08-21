<template>
  <div>
    <div class="bg-gradient-success pb-8 pt-8">
      <div class="container">
        <div class="filter-finder d-flex flex-column mt-4" style>

          <!-- 영상 종류 -->
          <div class="video-type px-4 mb-2 bg-white rounded-pill shadow-sm">
            <div
              class="d-inline-flex justify-content-center align-items-center mx-2 p-2"
            >
              <span class="text-primary font-weight-bold">영상 목적</span>
            </div>
            <div class="d-inline-flex m-2 p-2">
              <button
                class="btn btn-sm"
                :class="{
                  'btn-success': !!item.status,
                  'btn-outline-primary': !item.status,
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
              <span class="text-primary font-weight-bold">영상 종류</span>
            </div>
            <div class="d-inline-flex m-2 p-2">
              <button
                class="btn btn-sm"
                :class="{
                  'btn-success': !!item.status,
                  'btn-outline-primary': !item.status,
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
              <span class="text-primary font-weight-bold">편집 기술</span>
            </div>
            <div class="d-inline-flex m-2 p-2">
              <button
                class="btn btn-sm"
                :class="{
                  'btn-success': !!skill.status,
                  'btn-outline-primary': !skill.status,
                }"
                v-for="skill in videoSkills"
                :key="skill.name"
                @click="toggleFilter(skill)"
              >
                <span class="mx-2">{{ skill.name }}</span>
              </button>
            </div>
          </div>

          <!-- 필터 적용 및 초기화 -->
          <div class="d-flex justify-content-end">
            <div class="m-2">
              <button class="btn btn-primary btn-sm" @click="clearFilterAll"><i class="fa fa-redo-alt"></i> 전체 해제</button>
            </div>
            <div class="m-2">
              <button class="btn btn-primary btn-sm" @click="fetchEditors">적용</button>
            </div>
          </div>
        </div>

        <!-- 검색 바 -->
        <div class="d-flex justify-content-center mx-4 px-2">
          <div class="d-inline-flex">
            <base-dropdown>
              <base-button slot="title" type="secondary" class="dropdown-toggle">{{
                searchKey
              }}</base-button>
              <!-- <a class="dropdown-item" href="#" @click.prevent="searchKey = '전체'"
                >전체</a
              > -->
              <a class="dropdown-item" href="#" @click.prevent="searchKey = '이름'"
                >이름</a
              >
              <a class="dropdown-item" href="#" @click.prevent="searchKey = '태그'"
                >태그</a
              >
            </base-dropdown>
          </div>
          <div class="d-inline-flex">
            <base-input
              placeholder="검색어를 입력하세요."
              v-model="keyword"
              @keyup.enter="fetchEditors"
              style="width:500px;">
            </base-input>
          </div>
          <div class="d-inline-flex">
            <button class="btn btn-primary ml-1" style="max-height: 43px;" @click="fetchEditors">검색</button>
          </div>
        </div>
        
      </div>
    </div>

    <div class="container mt--7 mb-5">
      <!-- 편집자 목록 -->
      <div class="visableText">
        <span v-if="visable">총 {{editorsAllCnt}}명의 편집자 중에 {{editorsFilterCnt}}명의 편집자가 검색되었습니다.</span>
      </div>
      <editors-list
        title="편집자"
        :editorsData="editors"
        :message="message"
        @sort-by="setSortKey"
        @clear-sort="resetAll"
        @click-search-tag="reSearchTag">
      </editors-list>
    </div>
  </div>
</template>
<script>
import http from "@/util/http-common";
import EditorsList from "./Editors/EditorsList";
export default {
  name: "editors",
  components: {
    EditorsList,
  },
  props:{
    clickSearchTag: {
      type: String,
      default : ''
    }
  },
  data() {
    return {
      editors: [],
      editorsAllCnt: 0,
      editorsFilterCnt: 0,
      visable: false,
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
      searchKey: "이름",
      // 검색 단어
      keyword: "",
      // 정렬 기준
      sortBy: "NICKNAME_ASC",
      // 선택한 필터들
      selectedFilters: [],

      message: "",
    };
  },
  computed: {
    searchBy() {
      if (this.searchKey == '이름') {
        return 'NICKNAME'
      } else if (this.searchKey == '태그') {
        return 'TAG'
      } else {
        return 'ALL'
      }
    },
    selectedType() {
      let res = new Array();
      for (const item of this.videoType) {
        if (item.status == true) {
          res.push(item.value)
        }
      }
      return res;
    },
    selectedStyle() {
      let res = new Array();
      for (const item of this.videoStyle) {
        if (item.status == true) {
          res.push(item.value)
        }
      }
      return res;
    },
    selectedSkills() {
      let res = new Array();
      for (const item of this.videoSkills) {
        if (item.status == true) {
          res.push(item.value)
        }
      }
      return res;
    },
  },
  created() {
    //태그 클릭으로 들어왔을경우
    if(this.clickSearchTag != ''){
      this.searchKey = '태그';
      this.keyword = this.clickSearchTag;
    }

    this.fetchEditorsCnt();
    this.fetchFilter();
  },
  mounted() {
    this.fetchEditors();
  },
  methods: {
    reSearchTag(keyword){
      this.searchKey = '태그';
      this.keyword = keyword;
      this.fetchEditors();
    },
    setSortKey(key) {
      this.sortBy = key
      this.fetchEditors()
    },
    fetchEditors() {
      this.message = "";
      http
        .post("/search", {
          searchTags: this.keyword.trim().split(" "),
          searchText: this.keyword,
          searchType: this.searchBy,
          sortType: this.sortBy,
          videoSkills: this.selectedSkills,
          videoStyles: this.selectedStyle,
          videoTypes: this.selectedType
        })
        .then((res) => {
          if (res.data.status) {
            this.editors = res.data.object;
            if(this.editors.length == 0){
              this.message = "검색 결과가 없습니다.";
            }
            if(this.selectedSkills.length > 0 || this.selectedStyle.length > 0 || this.selectedType.length > 0){
              this.editorsFilterCnt = this.editors.length;
              this.visable = true;
            } else {
              this.visable = false;
            }
            // console.log(res.data.object);
          } else {
            console.log(res.data.status);
          }
        })
        .catch((err) => console.error(err));
    },
    fetchEditorsCnt() {
      this.message = "";
      http
        .post("/search", {
          searchTags: this.keyword.trim().split(" "),
          searchText: this.keyword,
          searchType: 'ALL',
          sortType: this.sortBy,
          videoSkills: [],
          videoStyles: [],
          videoTypes: []
        })
        .then((res) => {
          if (res.data.status) {
            this.editorsAllCnt = res.data.object.length;
          } else {
            console.log(res.data.status);
          }
        })
        .catch((err) => console.error(err));
    },
    fetchFilter() {
      this.message = "";
      let initType = this.$store.getters['stepper/getSelectedVideoType']
      if (initType) {
        this.videoType.forEach(item => {
          if (item.value == initType.value) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
      let initStyle = this.$store.getters['stepper/getSelectedVideoStyle']
      if (initStyle) {
        this.videoStyle.forEach(item => {
          if (item.value == initStyle.value) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
      let initSkills = this.$store.getters['stepper/getSelectedVideoSkills']
      if (initSkills.length > 0) {
        this.videoSkills.forEach(item => {
          if (initSkills.includes(item.value)) {
            item.status = true
            this.selectedFilters.push(item.value)
          }
        })
      }
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
      this.message = "";
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
      this.message = "";
    },
    resetAll() {
      this.clearFilterAll();
      this.keyword = "";
      this.sortBy = "NICKNAME_ASC";
      this.searchKey = "이름";
      this.fetchEditors();
      this.message = "";
    }
  },
};
</script>
<style scoped>
.visableText{
  text-align: right;
  font-size: 14px;
  margin-bottom: 1%;
}
</style>
