<template>
  <div style="max-height: 1200px;">
    <div class="card p-4">
      <ul class="list-unstyled mt-4">
        <li class="mb-4" v-for="editor in currentEditors" :key="editor.uid">
          <router-link :to="`/portfolio?no=${editor.uid}`">
          <div class="container">
            <div class="row">
              <div class="col-3">
                <LazyYoutubeVideo :src="editor.urls[0]" style="width: 100%" />
                <!-- <img :src="editor.imgURL" :alt="editor.nickname" class="mr-3" style="max-width: 180px;"> -->
              </div>
              <div class="col-9">
                <div class="d-flex align-items-stretch mt-4">
                  <div class="d-inline-flex ml-2">
                    <!-- nickname and bookmarks -->
                    <span class="display-1 mt-0 mb-1 text-default">{{editor.nickname}}</span>
                  </div>
                  <div class="d-inline-flex flex-column ml-3 mt-3">
                    <div><i class="display-4 ni ni-like-2"></i></div>
                    <div>{{editor.bookmarkCount}}</div>
                  </div>
                </div>
                <div class="ml-2 mt-4">
                  <!-- tags and estimated price -->
                  <div class="row">
                    <div class="col-6">
                      <button class="btn btn-info btn-sm" :key="index" v-for="(tag, index) in editor.tags">{{tag}}</button>
                      </div>
                    <div class="col-6 text-danger">예상 견적: 분당 {{editor.payMin}}원</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          </router-link>
        </li>
      </ul>
      <div class="card-footer d-flex justify-content-end"
         :class="type === 'dark' ? 'bg-transparent': ''">
        <base-pagination
          :total="editorsData.length"
          :perPage="editorsPerPage"
          :value="currentPage"
          @input="fetchPage"
        ></base-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import http from "@/util/http-common"
import LazyYoutubeVideo from "vue-lazy-youtube-video";

export default {
  name: 'editors-table',
  props: {
    type: {
      type: String
    },
    title: String,
  },
  components:{
    LazyYoutubeVideo,
  },
  computed: {
    currentEditors() {
      let start = (this.currentPage-1) * this.editorsPerPage
      let end = this.currentPage * this.editorsPerPage
      return this.editorsData.slice(start, end)
    },
  },
  data() {
    return {
      // 태그들
      tags:[],
      tag: '',
      editorsPerPage: 4,
      currentPage: 1,
      editorsData: [
        // 백엔드 API 호출 시 반환 자료형
        // {
        //   uid: "포트폴리오 UID",
        //   nickname: "편집자 닉네임",
        //   payMin: "분당 가격",
        //   bookmarkCount: "북마크 개수",
        //   tags: ["편집자 관련 태그", ...],
        //   urls: ["편집자 대표 URL", "기타 URL1", ...]
      ]
    }
  },
  created() {
    this.fetchEditors()
  },
  methods: {
    fetchEditors() {
      http.get('/search/listAll')
        .then(res => {
          // console.log(res)
          if (res.data.status){
            // console.log(res.data.object)
            this.editorsData = res.data.object
          } else {
            console.log(res.data.status)
          }
        })
        .catch(err => console.error(err))
    },
    fetchPage(val) {
      this.currentPage = val
    }
  },
}
</script>
<style>
</style>
