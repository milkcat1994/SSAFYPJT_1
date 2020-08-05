<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- 카테고리 필터 -->
      <div class="filter-finder d-flex flex-column mb-4">
        <div class="video-type row justify-content-center">
          <div class="align-items-center col-2 bg-default">
            <div class="m-2 p-2"><h3 class="text-white">영상 종류</h3></div>
          </div>
          <div class="col-8 border-bottom bg-white">
            <div class="m-2 p-2">
              <button class="btn btn-sm" :class="{'btn-success': !!type.status, 'btn-outline-dark': !type.status}" v-for="type in videoTypes" :key="type.name" @click="toggleFilter(type)"><span class="mx-2">{{type.name}}</span></button>
            </div>
          </div>
        </div>
        <div class="video-theme row justify-content-center">
          <div class="align-items-center col-2 bg-default">
            <div class="m-2 p-2"><h3 class="text-white">영상 특징</h3></div>
          </div>
          <div class="col-8 border-bottom  bg-white">
            <div class="m-2 p-2">
              <button class="btn btn-sm" :class="{'btn-success': !!theme.status, 'btn-outline-dark': !theme.status}" v-for="theme in videoTheme" :key="theme.name" @click="toggleFilter(theme)"><span class="mx-2">{{theme.name}}</span></button>
            </div>
          </div>
        </div>
        <div class="edit-skill row justify-content-center">
          <div class="align-items-center col-2 bg-default">
            <div class="m-2 p-2"><h3 class="text-white">편집 기술</h3></div>
          </div>
          <div class="col-8 bg-white">
            <div class="m-2 p-2">
              <button class="btn btn-sm" :class="{'btn-success': !!skill.status, 'btn-outline-dark': !skill.status}" v-for="skill in editSkills" :key="skill.name" @click="toggleFilter(skill)"><span class="mx-2">{{skill.name}}</span></button>
            </div>
          </div>
        </div>
        <div class="filter-selected row justify-content-center">
          <div class="align-items-center col-2 bg-light">
            <div class="m-2 p-2">
              <button class="btn-outline-dark" @click="clearFilterAll">전체 해제</button>
            </div>
          </div>
          <div class="col-8 bg-light">
            <div class="m-2 p-2">
              <button class="btn btn-success btn-sm" v-for="category in selectedCategories" :key="category" @click="clearFilter(category)">{{category}}</button>
            </div>
          </div>
          <!-- 적용 버튼 -->
        </div>
      </div>
      <!-- 검색 바 -->
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
      </div>
    </base-header>

    <div class="container-fluid mt--7 mb-5">
      <!-- 여기에 BE에서 요청받아온 List를 뿌려보자 -->
      <vuescroll :ops="ops">
        <editors-table title="편집자"></editors-table>
      </vuescroll>
    </div>
  </div>
</template>
<script>
import vuescroll from "vuescroll"
import EditorsTable from "./Tables/EditorsTable";
import http from "@/util/http-common"
export default {
  name: "editors",
  components: {
    EditorsTable,
    vuescroll,
  },
  data() {
    return {
      editors: {},
      ops: {
        vuescroll: {
          mode: 'native',
          sizeStrategy: 'percent',
          detectResize: true,
          pushLoad: {
            enable: false,
            tips: {
              deactive: 'Push to Load',
              active: 'Release to Load',
              start: 'Loading...',
              beforeDeactive: 'Load Successfully!'
            },
            auto: false,
            autoLoadDistance: 0
          },
        },
      },
      videoTypes: [
        {name: '개인', status: false}, {name: '사업', status: false}, {name: '기념일', status: false},
      ],
      videoTheme: [
        {name: '코믹', status: false}, {name: '화려', status: false}, {name: '갬성', status: false},
      ],
      editSkills: [
        {name: '밝기/대비', status: false}, {name: '색', status: false}, {name: '오디오', status: false}, {name: '모션그래픽', status: false}, {name: '자막', status: false}, {name: '인트로/아웃트로', status: false}, 
      ],

      // 검색하고자 하는 카테고리 모음
      selectedCategories: []
    }
  },
  methods: {
    fetchList() {
      http.get('/search/list')
        .then(res => {
          console.log(res)
        })
        .catch(err => {
          console.error(err)
        })
      return
    },
    toggleFilter(category) {
      console.log(category)
      // category.status = !category.status
      if (category.status) {
        this.clearFilter(category.name)
        // let index = this.selectedCategories.indexOf(category.name)
        // if (index > -1) {
        //   this.selectedCategories.splice(index, 1)
        // }
        category.status = false
      } else {
        this.selectedCategories.push(category.name)
        category.status = true
      }
    },
    clearFilter(value){
      let index = this.selectedCategories.indexOf(value)
      if (index > -1) {
        this.selectedCategories.splice(index, 1)
      }
    },
    clearFilterAll() {
      // 필터된 카테고리들 토글(활성화 <-> 비활성화)
      this.selectedCategories.forEach(category => {
        this.videoTypes.forEach(filter => {
          if (filter.name === category) {
            filter.status = false
          }
        })
        this.videoTheme.forEach(filter => {
          if (filter.name === category) {
            filter.status = false
          }
        })
        this.editSkills.forEach(filter => {
          if (filter.name === category) {
            filter.status = false
          }
        })
      })
      // selectedCategories 배열 clear
      this.selectedCategories.length = 0
    }
  },
  mounted() {
    this.fetchList()
  },
};
</script>
<style></style>
