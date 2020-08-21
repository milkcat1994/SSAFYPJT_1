<template>
  <div style="width:100%">
    <base-header type="gradient-success" class="pt-5 pt-md-8 px-lg-5 mb-2">
      <!-- 검색 -->
      <div class="row mt-3">
        <div class="col-5 ml-5">
          <!-- <h1 class="display-2 text-white">어떤 편집자를 찾으시나요?</h1> -->

          <!-- 검색 input -->

          <h1 class="display-3 mb-3">어떤 편집자를 찾으시나요?</h1>
          <div class="ml-1 mb-3">
            <h3 class="mb-1" style="color:#f1f4fb">
              자신의 이름으로 승부하는
              <span class="text-primary mr-1" style="font-size:1.2em">진짜 편집자</span>와 일해보세요.
            </h3>
            <h3 class="mb-1" style="color:#f1f4fb;">
              <span class="text-primary mr-1" style="font-size:1.2em">편집</span>의 비즈니스모델은
              <span class="text-primary mr-1" style="font-size:1.2em">당신의 성공</span>이기에.
            </h3>
            <h3 class="mb-1" style="color:#f1f4fb;">
              당연하게도 매칭수수료
              <span class="text-primary mr-1" style="font-size:1.2em">0%</span>
            </h3>
          </div>
          <div class="d-flex mx-1">
            <b-button variant="primary" class="mr-4" @click="moveStepper()">맞춤 편집자 찾기</b-button>
            <b-button variant="secondary" @click="moveList()">전체 편집자 보기</b-button>
          </div>
        </div>
        <div class="col-6 ml-4 mr-0 pr-0 mb-5">
          <img src="img/theme/friends2.png" alt="png from pngtree.com" style="width:80%" />
        </div>
      </div>

      <!-- 슈퍼편집자/맞춤편집자 -->
      <div class="contaienr">
        <div class="row">
          <!-- 슈퍼편집자 -->
          <div class="col-12 mb-3 px-5">
            <div class="my-6">
              <h1 class="hr-sect">슈퍼편집자를 만나보세요.</h1>
            </div>

            <div class="px-4 d-flex justify-content-center">
              <b-carousel
                id="carousel-1"
                v-model="slide"
                :interval="3000"
                controls
                indicators
                background="white"
                class="mb-4"
                @sliding-start="onSlideStart"
                @sliding-end="onSlideEnd"
              >
                <b-carousel-slide v-for="editor in currentEditors" :key="editor.uid">
                  <template v-slot:img>
                    <router-link :to="`/portfolio?no=${editor.uid}`">
                      <img
                        v-if="editor.url"
                        class="zoom"
                        :src="'https://img.youtube.com/vi/' + editor.url.substr(30) + '/0.jpg'"
                        style="width: 100%;"
                      />
                      <img
                        v-else
                        src="img/theme/empty.PNG"
                        alt="영상없음"
                        style="width: 100%;"
                      />
                      <div class="d-flex justify-content-end mt-3 mx-3">
                        <h2
                          class="mt-0 mb-1 mr-2"
                          style="color:white decoration:none"
                        >{{ editor.nickname }}</h2>
                      </div>
                      <div class="d-flex justify-content-end mr-3 mb-2">
                        <div
                          v-for="t in editor.tags.slice(0, 5)"
                          :key="t"
                          class="text-right mt-2"
                          style="display:inline-block"
                        >
                          <span class="mr-2"># {{ t }}</span>
                        </div>
                      </div>

                      <div class="d-flex justify-content-end mb-3 mx-3">
                        <h4>
                          <i class="fas fa-heart mr-2" style="color:red"></i>
                          <span class="mr-3">{{ editor.bookmarkNumber }}</span>
                        </h4>
                        <h4>
                          <i class="fas fa-star mr-2" style="color:#ffbf00"></i>
                          <span class="mr-3">{{ round(editor.avgScore) }}</span>
                        </h4>
                      </div>
                    </router-link>
                  </template>
                </b-carousel-slide>
              </b-carousel>
            </div>
          </div>

          <!-- 맞춤편집자 -->
          <div class="col-12 mb-8 px-5">
            <div class="my-5">
              <h1 class="hr-sect">맞춤 편집자를 만나보세요.</h1>
            </div>

            <div class="px-4" v-if="isLoggedIn && !bookmarkemp">
              <div id="app">
                <b-container>
                  <b-row>
                    <b-col cols="12">
                      <carousel :perPage="4" style="height:600px">
                        <slide
                          class="p-2 zoom"
                          v-for="recEditor in recommendData.slice(0, 10)"
                          :key="recEditor.uid"
                        >
                          <b-card id="maincard" class="recCard" @click="movePortfolio(recEditor.uid)">
                            <img src alt />
                            <img
                              v-if="recEditor.url"
                              :src="'https://img.youtube.com/vi/' + recEditor.url.substr(30) + '/0.jpg'"
                              style="width: 100%;"
                            />
                            <img
                              v-else
                              src="/img/theme/empty.png"
                              alt="영상없음"
                              style="width: 100%; height: 127.969px;"
                            />
                            <hr />
                            <b-card-text>
                              <div class="d-inline-flex">
                                <h3 class="mt-0 mb-1">{{ recEditor.nickname }}</h3>
                              </div>

                              <div
                                v-for="(t, index) in recEditor.tag
                                  .split(', ')
                                  .slice(0, 3)"
                                :key="index"
                                class="d-flex"
                              >
                                <span class="d-flex"># {{ t }}</span>
                              </div>
                              <p class="text-right">₩ {{ recEditor.pay }}</p>
                            </b-card-text>
                          </b-card>
                        </slide>
                      </carousel>
                    </b-col>
                  </b-row>
                </b-container>
              </div>
            </div>

            <div class="px-4" v-if="!isLoggedIn || bookmarkemp">
              <b-container>
                <b-row>
                  <b-col cols="12" class="d-flex justify-content-center">
                    <img src="img/theme/dogcat1.png" alt="강아지고양이" style="width:50%" class="my-0" />
                  </b-col>
                  <b-col cols="12" class="d-flex justify-content-center mb-2">
                    <span style="color:white">Designed by PngTree</span>
                  </b-col>
                  <b-col cols="12" class="d-flex justify-content-center">
                    <h1 v-if="!isLoggedIn">로그인이 필요해요!</h1>
                    <h1 v-if="isLoggedIn && bookmarkemp">자신에 스타일에 맞는 편집자를 <span style="color:#ff0080;"> 찜!</span> 해주세요</h1>
                  </b-col>
                </b-row>
              </b-container>
            </div>
          </div>
        </div>
      </div>
    </base-header>
  </div>
</template>
<script>
import http from "@/util/http-common.js";
import { Carousel, Slide } from "vue-carousel";
// import LazyYoutubeVideo from "vue-lazy-youtube-video";
// import { mapGetters } from "vuex";

export default {
  components: {
    // LazyYoutubeVideo,
    Carousel,
    Slide,
  },
  data() {
    return {
      slide: 0,
      sliding: null,
      keyword: null,
      editorsPerPage: 6,
      currentPage: 1,
      editorsData: [],
      recommendData: [],
      avgScore: 0,

      bookmarkemp: true,

    };
  },
  created() {
    this.fetchEditors();
  },
  methods: {
    recommendEditors() {
      if (!this.$session.exists)
        this.$store.commit("auth/mutateIsLogin", false);
      http
        .post("/recommend/" + this.$session.get("uid"))
        .then((res) => {
          if (res.data == "bookmark is empty") {
            this.bookmarkemp = true;
            return;
          }
          if (res.data) {
            this.recommendData = res.data;
            this.bookmarkemp = false;
          }
        })
        .catch((err) => console.log(err));
    },
    fetchEditors() {
      http
        .post("/search", {
          searchTags: [""],
          searchText: "",
          searchType: "ALL",
          sortType: "SCORE_DESC",
          videoSkills: [],
          videoStyles: [],
          videoTypes: [],
        })
        .then((res) => {
          if (res.data.status) {
            this.editorsData = res.data.object;
          } else {
            console.log(res.data.status);
          }
        })
        .catch((err) => console.error(err));
    },
    onSlideStart() {
      this.sliding = true;
    },
    onSlideEnd() {
      this.sliding = false;
    },
    keywordSearch(keyword) {
      http.get(`/search/${keyword}`).then((res) => {
        console.log(res);
        this.$router.push({ name: "search" });
      });
    },
    createRequest(event) {
      console.log(event.target.value);
      this.$router.push({ name: "search" });
    },
    moveList() {
      this.$router.push({ name: "editors" });
    },
    moveStepper() {
      this.$router.push({ name: "search" });
    },
    fetchPage(val) {
      this.currentPage = val;
    },
    movePortfolio(uid) {
      this.$router.push("/portfolio?no=" + uid);
    },

    round(score) {
      return Number(score.toFixed(1));
    },
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters["auth/isLoggedIn"];
    },
    // ...mapGetters(["auth/isLoggedIn"]),
    currentEditors() {
      let start = (this.currentPage - 1) * this.editorsPerPage;
      let end = this.currentPage * this.editorsPerPage;
      return this.editorsData.slice(start, end);
    },
  },
  mounted() {
    this.recommendEditors();
  },
};
</script>
<style scoped>
.hr-sect {
  display: flex;
  flex-basis: 100%;
  align-items: center;
  margin: 8px 0px;
}
.hr-sect::before,
.hr-sect::after {
  content: "";
  flex-grow: 1;
  background: rgba(0, 0, 0, 0.35);
  height: 1px;
  line-height: 0px;
  margin: 0px 16px;
}

#maincard:hover {
  cursor: pointer;
}

#carousel-1 {
  width: 700px;
  /* box-shadow: 3px 3px 3px gray; */
}

/* #maincard {
  box-shadow: 3px 3px 3px gray;
} */

button:focus {
  border: none !important;
  outline: none !important;
}

.recCard:hover {
  -ms-transform: scale(1.07);
  -webkit-transform: scale(1.07);
  transform: scale(1.07);
  border-radius:1em;
}
</style>
