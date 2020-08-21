<template>
  <div id="lst" style="max-height: 1200px;">
    <div class="card p-4">
      <div class="d-flex justify-content-between align-item-center">
        <span>전체 {{totalPage}}페이지 중 {{currentPage}}페이지</span>
        <div>
          <div class="mr-2 d-inline-flex">
            <button class="btn btn-danger" @click="$emit('clear-sort')">전체 편집자 보기</button>
          </div>
          <base-dropdown>
            <base-button slot="title" type="primary" class="dropdown-toggle">{{sortKey}}</base-button>
            <a class="dropdown-item" @click="fetchSortKey('NICKNAME_ASC')">이름순</a>
            <a class="dropdown-item" @click="fetchSortKey('SCORE_DESC')">평점순</a>
            <a class="dropdown-item" @click="fetchSortKey('PRICE_ASC')">낮은 가격순</a>
            <a class="dropdown-item" @click="fetchSortKey('PRICE_DESC')">높은 가격순</a>
          </base-dropdown>
        </div>
      </div>
      <div v-if="message" class="emptyResult">
        <h1>{{message}}</h1>
        <br />
        <li>단어의 철자가 정확한지 확인해 보세요.</li>
        <li>한글을 영어로 혹은 영어를 한글로 입력했는지 확인해 보세요.</li>
        <li>검색어의 단어 수를 줄이거나, 보다 일반적인 검색어로 다시 검색해 보세요.</li>
        <li>두 단어 이상의 검색어인 경우, 띄어쓰기를 확인해 보세요.</li>
        <li>검색 옵션을 변경해서 다시 검색해 보세요.</li>
      </div>
      <ul class="list-unstyled mt-4">
        <li class="mb-4" v-for="editor in currentEditors" :key="`currEdit_${editor.uid}`">
          <div class="container">
            <div class="row">
              <div class="col-3">
                <!-- 영상 미리보기 -->
                <router-link :to="`/portfolio?no=${editor.uid}`">
                  <!-- <img src alt /> -->
                  <LazyYoutubeVideo v-if="editor.url" :src="editor.url" style="width: 100%;" />
                  <img
                    v-else
                    src="/img/theme/empty.PNG"
                    alt="영상없음"
                    style="width: 100%; border: 1px solid #555; height: 135px;"
                    />
                </router-link>
              </div>
              <div class="col-9 pt-2 d-flex flex-column justify-content-around">
                <div class="d-flex align-items-center">
                  <!-- 포트폴리오 닉네임 -->
                  <router-link :to="`/portfolio?no=${editor.uid}`">
                    <div class="d-inline-flex">
                      <h1 class="mt-0 mb-1">{{ editor.nickname }}</h1>
                    </div>
                  </router-link>
                  <!-- 북마크 -->
                  <div class="d-inline-flex flex-column ml-3">
                    <base-button
                      v-if="!editor.togleBookmark"
                      outline
                      type="danger"
                      size="sm"
                      icon="ni ni-favourite-28"
                      @click="addBookmark(editor.uid, editor.togleBookmark)"
                    >{{editor.bookmarkNumber}}</base-button>
                    <base-button
                      v-else
                      type="danger"
                      size="sm"
                      icon="ni ni-favourite-28"
                      @click="addBookmark(editor.uid, editor.togleBookmark)"
                    >{{editor.bookmarkNumber}}</base-button>
                  </div>
                </div>
                <div class="d-flex">
                  <!-- 평점 -->
                  <!-- <span><i class="fas fa-star"></i> {{Number((editor.avgScore).toFixed(1))}}점</span> -->
                  <span>
                    <i class="fas fa-star"></i>
                    {{ round(editor.avgScore) }}점
                  </span>
                  <!-- 예상 견적 -->
                  <div class="col-4 text-danger">예상 견적: 분당 {{ numberWithCommas(editor.payMin) }}원</div>
                </div>
                <div class="row">
                  <!-- 태그 -->
                  <div class="col-8">
                    <button
                      class="btn btn-info btn-sm mb-1"
                      :key="`tag_${index}`"
                      @click="searchTag(tag)"
                      v-for="(tag, index) in editor.tags"
                    >{{ tag }}</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
      <div class="card-footer d-flex justify-content-center">
        <base-pagination
          id="pagination"
          :total="editorsData.length"
          :perPage="editorsPerPage"
          :value="currentPage"
          align="center"
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
    editorsData: {
      type: Array,
    },
    message: String,
  },
  components: {
    LazyYoutubeVideo,
  },
  computed: {
    currentEditors() {
      let start = (this.currentPage - 1) * this.editorsPerPage;
      let end = this.currentPage * this.editorsPerPage;
      this.getBookmarkCount(this.editorsData);
      return this.editorsData.slice(start, end);
    },
    totalPage() {
      return Math.ceil(this.editorsData.length / this.editorsPerPage);
    },
  },
  data() {
    return {
      // 태그들
      // tags: [],
      // tag: "",
      editorsPerPage: 5,
      currentPage: 1,
      sortKey: "정렬",
    };
  },
  created() {},
  methods: {
    round(score) {
      return Number(score.toFixed(1));
    },
    numberWithCommas(price) {
      return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    fetchPage(val) {
      this.currentPage = val;
    },
    fetchSortKey(val) {
      if (val == "NICKNAME_ASC") {
        this.sortKey = "이름순";
      } else if (val == "SCORE_DESC") {
        this.sortKey = "평점순";
      } else if (val == "PRICE_ASC") {
        this.sortKey = "낮은 가격순";
      } else if (val == "PRICE_DESC") this.sortKey = "높은 가격순";
      this.$emit("sort-by", val);
      this.message = "";
    },
    getBookmarkCount(editorsList) {
      editorsList.forEach((editor) => {
        http
          .get("/bookmark/cnt/" + editor.uid)
          .then(({ data }) => {
            if (data.data == "success") {
              data.object.forEach((obj) => {
                if (obj.userInfoUid == this.$session.get("uid")) {
                  editor.togleBookmark = true;
                  editor.bookmarkNumber = data.object.length;
                }
              });
              return;
            } else {
              return;
            }
          })
          .catch((error) => {
            console.log(error);
            return;
          });
      });
    },
    addBookmark(uid, togleBookmark) {
      if (!togleBookmark) {
        http
          .post("/bookmark", {
            uid: this.$session.get("uid"),
            muid: uid,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.editorsData.forEach((editor) => {
                if (editor.uid == uid) {
                  editor.bookmarkNumber += 1;
                  editor.togleBookmark = true;
                }
              });
              return;
            } else {
              return;
            }
          });
      } else {
        http
          .post("/bookmark/delete", {
            uid: this.$session.get("uid"),
            muid: uid,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.editorsData.forEach((editor) => {
                if (editor.uid == uid) {
                  editor.bookmarkNumber -= 1;
                  editor.togleBookmark = false;
                }
              });
              return;
            } else {
              return;
            }
          });
      }
    },
    searchTag(tag) {
      // Editors.vue로 props를 이용하여 보내 태그 검색이 가능하도록 한다.
      this.$emit("click-search-tag", tag);
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

.emptyResult li {
  margin-left: 30%;
}

.emptyResult > h1 {
  text-align: center;
}

#pagination {
  cursor: pointer;
}
</style>
