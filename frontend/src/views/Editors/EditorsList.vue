<template>
  <div style="max-height: 1200px;">
    <div class="card p-4">
      <div class="d-flex justify-content-end">
        <base-dropdown>
          <base-button slot="title" type="primary" class="dropdown-toggle"
            >정렬</base-button
          >
          <a class="dropdown-item">이름순</a>
          <a class="dropdown-item">평점순</a>
          <a class="dropdown-item">가격낮은순</a>
          <a class="dropdown-item">가격높은순</a>
        </base-dropdown>
      </div>
      <ul class="list-unstyled mt-4">
        <li class="mb-4" v-for="editor in currentEditors" :key="editor.uid">
          <div class="container">
            <div class="row">
              <div class="col-3">
                <!-- 영상 미리보기 -->
                <router-link :to="`/portfolio?no=${editor.uid}`">
                  <img src="" alt="" />
                  <LazyYoutubeVideo
                    :src="editor.urls[0]"
                    style="width: 100%;"
                  />
                </router-link>
              </div>
              <div class="col-9 d-flex flex-column justify-content-around">
                <div class="d-flex align-items-center">
                  <!-- 포트폴리오 닉네임 -->
                  <router-link :to="`/portfolio?no=${editor.uid}`">
                    <div class="d-inline-flex">
                      <h1 class="mt-0 mb-1">{{ editor.nickname }}</h1>
                    </div>
                  </router-link>
                  <!-- 북마크 -->
                  <div class="d-inline-flex flex-column ml-3">
                    <!-- <base-button :outline="!isBookmarked(editor.uid)" type="danger" size="sm" icon="ni ni-favourite-28" @click="addBookmark()"> -->
                    <base-button
                      outline
                      disabled
                      type="danger"
                      size="sm"
                      icon="ni ni-favourite-28"
                    >
                      {{ editor.bookmarkCount }}
                    </base-button>
                  </div>
                </div>
                <div class="row">
                  <!-- 태그 -->
                  <div class="col-8">
                    <button
                      class="btn btn-info btn-sm mb-1"
                      :key="index"
                      v-for="(tag, index) in editor.tags"
                    >
                      {{ tag }}
                    </button>
                  </div>
                  <!-- 예상 견적 -->
                  <div class="col-4 text-danger">
                    예상 견적: 분당 {{ editor.payMin }}원
                  </div>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
      <div
        class="card-footer d-flex justify-content-center"
        :class="type === 'dark' ? 'bg-transparent' : ''"
      >
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
import http from "@/util/http-common";
import LazyYoutubeVideo from "vue-lazy-youtube-video";

export default {
  name: "editors-list",
  props: {
    type: {
      type: String,
    },
    title: String,
  },
  components: {
    LazyYoutubeVideo,
  },
  computed: {
    currentEditors() {
      let start = (this.currentPage - 1) * this.editorsPerPage;
      let end = this.currentPage * this.editorsPerPage;
      return this.editorsData.slice(start, end);
    },
  },
  data() {
    return {
      // 태그들
      tags: [],
      tag: "",
      editorsPerPage: 5,
      currentPage: 1,
      editorsData: [
        // 백엔드 API 호출 시 반환 자료형
        // {
        //   uid: "포트폴리오 UID",
        //   nickname: "포트폴리오 닉네임",
        //   payMin: "분당 가격",
        //   bookmarkCount: "북마크 개수",
        //   tags: ["편집자 관련 태그", ...],
        //   urls: ["편집자 대표 URL", "기타 URL1", ...]
      ],
    };
  },
  created() {
    this.fetchEditors();
  },
  methods: {
    fetchEditors() {
      http
        .get("/search/listAll")
        .then((res) => {
          // console.log(res)
          if (res.data.status) {
            // console.log(res.data.object)
            this.editorsData = res.data.object;
          } else {
            console.log(res.data.status);
          }
        })
        .catch((err) => console.error(err));
    },
    fetchPage(val) {
      this.currentPage = val;
    },
    // 북마크 로직(미완성)
    isBookmarked(portfolioUID) {
      // login 되어있는 사용자만?
      http
        .get("/bookmark/cnt/" + portfolioUID)
        .then((res) => {
          if (res.data == "success") {
            let isBooked = false;
            res.object.forEach((obj) => {
              if (obj.userInfoUid == this.$session.get("uid")) {
                isBooked = true;
              }
            });
            return isBooked;
          }
        })
        .catch((err) => {
          console.error(err);
        });
    },
    addBookmark(portfolioUID) {
      if (!this.isBookmarked(portfolioUID)) {
        http
          .post("/bookmark", {
            uid: this.$session.get("uid"),
            muid: portfolioUID,
          })
          .then((res) => {
            // 리스트 전체 돌면서
            // 리스트에 있는 개별 포트폴리오 UID == 북마크한 포트폴리오 UID
            // 일 경우 북마크 개수 업데이트? (아니면 Vue.js가 자동으로 업데이트해주는지?)
            console.log(res);
          });
      }
    },
  },
};
</script>
<style scoped>
.nickname {
  font-size: 30px !important;
}

.dropdown-item:hover {
  cursor: pointer;
}
</style>
