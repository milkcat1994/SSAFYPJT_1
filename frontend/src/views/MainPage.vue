<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8 px-lg-5">
      <!-- 검색 -->
      <div class="row align-items-center mx-lg-n5">
        <div class="col py-3 px-lg-5">
          <div
            class="d-flex flex-column justify-content-center col-lg-12 col-12"
          >
            <!-- <h1 class="display-2 text-white">어떤 편집자를 찾으시나요?</h1> -->

            <!-- 검색 input -->

            <h1 class="display-3">어떤 편집자를 찾으시나요?</h1>
            <base-input
              placeholder="편집할 영상의 스타일을 입력해보세요!"
              addon-left-icon="fas fa-search"
              v-model="keyword"
              @keyup.enter="createRequest"
            >
            </base-input>

            <!-- 카테고리 icons -->
            <div class="container px-lg-5">
              <!-- <div class="row mx-lg-n5 text-center text-white"> -->
              <div class="row mx-lg-n5">
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-utensils fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconFood"
                      alt="food icon"
                      id="food"
                    />
                    <small><strong>먹방</strong></small>
                  </div>
                </div>
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-plane fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconPlane"
                      alt="plane icon"
                      id="travel"
                    />
                    <small><strong>여행</strong></small>
                  </div>
                </div>
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-gamepad fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconGame"
                      alt="game icon"
                      id="game"
                    />
                    <small><strong>게임</strong></small>
                  </div>
                </div>
                <div class="w-100"></div>
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-bicycle fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconExercise"
                      alt="dumbbell icon"
                      id="exercise"
                    />
                    <small><strong>운동</strong></small>
                  </div>
                </div>
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-microphone fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconASMR"
                      alt="talk icon"
                      id="talk"
                    />
                    <small><strong>토크</strong></small>
                  </div>
                </div>
                <div class="col py-3 px-lg-5">
                  <div
                    class="d-flex flex-column align-items-center text-center"
                    @click="iconSearch"
                  >
                    <!-- <i class="fa fa-music fa-4x mb-2" aria-hidden="true"></i> -->
                    <img
                      class="icon m-2"
                      :src="iconMusic"
                      alt="guitar icon"
                      id="music"
                    />
                    <small><strong>음악</strong></small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 추천 -->
        <div class="col py-3 px-lg-5">
          <div class="d-flex flex-column justify-content-around">
            <!-- 슈퍼 편집자 -->
            <div class="find" @click="findSuper">
              <stats-card
                title="어떤 영상이라도 문제없다!"
                type="gradient-red"
                sub-title="슈퍼 편집자 찾아보기"
                icon="ni ni-like-2"
                class="mb-4 mb-xl-4"
              >
                <template slot="footer">
                  <!-- <span class="text-success mr-2"><i class="fa fa-arrow-up"></i>87%</span> -->
                  <span class="mr-2"><strong>98%</strong></span>
                  <span class="text-nowrap">의 이용자가 선택합니다</span>
                </template>
              </stats-card>
            </div>

            <!-- 맞춤 편집자 -->
            <div class="find" @click="getSuggestion">
              <stats-card
                title="얼마나 맞을지 궁금하죠?"
                type="gradient-green"
                sub-title="맞춤 편집자 추천받기"
                icon="ni ni-compass-04"
                class="mb-4 mb-xl-0"
              >
                <template slot="footer">
                  <!-- <span class="text-success mr-2"><i class="fa fa-arrow-up"></i> 96%</span> -->
                  <span class="mr-2"><strong>96%</strong></span>
                  <span class="text-nowrap">의 이용자가 만족합니다.</span>
                </template>
              </stats-card>
            </div>
          </div>
        </div>
      </div>
    </base-header>

    <!-- <div class="container-fluid mt--7 mb-4">
        <div class="row">
            <div class="col">
                <div class="card shadow border-0">
                    <div style="height: 600px;">헛헛함을 달래줄 컨텐츠 삽입</div>
                </div>
            </div>
        </div>
    </div> -->
  </div>
</template>
<script>
import http from "@/util/http-common.js";

export default {
  components: {},
  data() {
    return {
      keyword: null,
    };
  },
  methods: {
    keywordSearch(keyword) {
      // console.log(keyword)
      // BE URL 확인 후 변경
      http.get(`/search/${keyword}`).then((res) => {
        console.log(res);
        this.$router.push({ name: "search" });
      });
    },
    iconSearch(event) {
      // console.log(event.target.id)
      let kw = event.target.id;
      if (kw === "food") {
        this.keywordSearch(kw);
      } else if (kw === "travel") {
        this.keywordSearch(kw);
      } else if (kw === "game") {
        this.keywordSearch(kw);
      } else if (kw === "exercise") {
        this.keywordSearch(kw);
      } else if (kw === "talk") {
        this.keywordSearch(kw);
      } else if (kw === "music") {
        this.keywordSearch(kw);
      } else {
        this.keywordSearch(kw);
      }
    },
    createRequest(event) {
      console.log(event.target.value);
      this.$router.push(
        { name: "search" }
        // {params: {
        // 둘 중 하나 사용
        //    keyword: this.keyword
        //    keyword: event.target.value
        // }}
      );
    },
    findSuper() {
      // 슈퍼편집자 조건으로 검색 조건 + router로 push
      this.$router.push({ name: "search" });
    },
    getSuggestion() {
      // 현재 로그인한 사용자의 태그나 이용정보 바탕으로 검색 조건 + router로 push
      this.$router.push({ name: "search" });
    },
  },
  computed: {
    iconASMR() {
      return require("@/assets/main/mic.svg");
    },
    iconCelebration() {
      return require("@/assets/main/balloon.svg");
    },
    iconExercise() {
      return require("@/assets/main/dumbbell.svg");
    },
    iconFood() {
      return require("@/assets/main/cutlery.svg");
    },
    iconGame() {
      return require("@/assets/main/gamepad.svg");
    },
    iconMusic() {
      return require("@/assets/main/guitar.svg");
    },
    iconPlane() {
      return require("@/assets/main/plane.svg");
    },
  },
  mounted() {},
};
</script>
<style scoped>
.icon {
  /* display: inline-block; */
  width: 50px;
  height: 50px;
}

.icon:hover {
  cursor: pointer;
}

.find:hover {
  cursor: pointer;
}

/* small {
  color: white;
} */
</style>
