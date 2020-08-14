<template>
  <div>
    <base-header type="gradient-success" class="pt-5 pt-md-8 px-lg-5 mb-4">
      <!-- 검색 -->
      <div class="row mt-7">
        <div class="col-5 ml-5">
          <!-- <h1 class="display-2 text-white">어떤 편집자를 찾으시나요?</h1> -->

          <!-- 검색 input -->

          <h1 class="display-3 mb-3">어떤 편집자를 찾으시나요?</h1>
          <div class="mx-3 mb-3">
            <h3 class="text-secondary mb-1">
              자신의 이름으로 승부하는
              <span class="text-primary">'진짜 편집자'</span>와 일해보세요.
            </h3>
            <h3 class="text-secondary mb-1">
              <span class="text-primary">편집</span>의 비즈니스모델은
              <span class="text-primary">'당신의 성공'</span>이기에.
            </h3>
            <h3 class="text-secondary mb-1">
              당연하게도 매칭수수료
              <span class="text-primary">'0%'</span>
            </h3>
          </div>
          <div class="d-flex mx-3">
            <b-button variant="primary" class="mr-4">편집자 찾아보기</b-button>
            <b-button variant="secondary">편집자 구경하기</b-button>
          </div>
        </div>
        <div class="col-6 ml-4">
          <img
            src="img/theme/friends.png"
            alt="png from pngtree.com"
            style="width:90%"
          />
        </div>
      </div>
    </base-header>

    <!-- 슈퍼편집자/맞춤편집자 -->
    <div class="contaienr">
      <div class="row">
        <!-- 슈퍼편집자 -->
        <div class="col-12 mb-8 px-5">
          <div class="my-5">
            <h1 class="hr-sect">슈퍼편집자를 만나보세요.</h1>
          </div>

          <div class="px-4">
            <b-carousel
              id="carousel-1"
              v-model="slide"
              :interval="3000"
              controls
              indicators
              background="#ccc"
              img-width="1024"
              img-height="480"
              class="mb-4"
              style="text-shadow: 1px 1px 2px #000;"
              @sliding-start="onSlideStart"
              @sliding-end="onSlideEnd"
            >
              <!-- Text slides with image -->
              <b-carousel-slide
                caption="First slide"
                text="Nulla vitae elit libero, a pharetra augue mollis interdum."
                img-src="https://picsum.photos/1024/480/?image=52"
              ></b-carousel-slide>

              <!-- Slides with custom text -->
              <b-carousel-slide
                img-src="https://picsum.photos/1024/480/?image=54"
              >
                <h1>Hello world!</h1>
              </b-carousel-slide>

              <!-- Slides with image only -->
              <b-carousel-slide
                img-src="https://picsum.photos/1024/480/?image=58"
              ></b-carousel-slide>

              <!-- Slides with img slot -->
              <!-- Note the classes .d-block and .img-fluid to prevent browser default image alignment -->
              <b-carousel-slide>
                <template v-slot:img>
                  <img
                    class="d-block img-fluid w-100"
                    width="1024"
                    height="480"
                    src="https://picsum.photos/1024/480/?image=55"
                    alt="image slot"
                  />
                </template>
              </b-carousel-slide>

              <!-- Slide with blank fluid image to maintain slide aspect ratio -->
              <b-carousel-slide
                caption="Blank Image"
                img-blank
                img-alt="Blank image"
              >
                <p>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                  Suspendisse eros felis, tincidunt a tincidunt eget, convallis
                  vel est. Ut pellentesque ut lacus vel interdum.
                </p>
              </b-carousel-slide>
            </b-carousel>
            <div class="d-flex justify-content-end">
              <b-button variant="primary">더 보러가기</b-button>
            </div>
          </div>
        </div>

        <!-- 맞춤편집자 -->
        <div class="col-12 mb-8 px-5">
          <div class="my-5">
            <h1 class="hr-sect">맞춤 편집자를 만나보세요.</h1>
          </div>

          <div class="px-4">
            <div id="app">
              <b-container>
                <b-row>
                  <b-col cols="12">
                    <carousel :perPage="3">
                      <slide
                        class="p-2"
                        v-for="editor in currentEditors"
                        :key="editor.uid"
                      >
                        <b-card id="maincard">
                          <router-link :to="`/portfolio?no=${editor.uid}`">
                            <img src alt />
                            <LazyYoutubeVideo
                              :src="editor.urls[0]"
                              style="width: 100%;"
                              class="mb-2"
                            />
                          </router-link>
                          <hr />
                          <b-card-text>
                            <router-link :to="`/portfolio?no=${editor.uid}`">
                              <div class="d-inline-flex">
                                <h3 class="mt-0 mb-1">{{ editor.nickname }}</h3>
                              </div>
                            </router-link>

                            <p class="text-right">₩ {{ editor.payMin }}</p>
                          </b-card-text>
                        </b-card>
                      </slide>
                    </carousel>
                  </b-col>
                </b-row>
              </b-container>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import http from "@/util/http-common.js";
import { Carousel, Slide } from "vue-carousel";
import LazyYoutubeVideo from "vue-lazy-youtube-video";

export default {
  components: {
    LazyYoutubeVideo,
    Carousel,
    Slide,
  },
  data() {
    return {
      slide: 0,
      sliding: null,
      keyword: null,
      editorsPerPage: 5,
      currentPage: 1,
      editorsData: [],
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
    onSlideStart() {
      this.sliding = true;
    },
    onSlideEnd() {
      this.sliding = false;
    },
    keywordSearch(keyword) {
      // console.log(keyword)
      // BE URL 확인 후 변경
      http.get(`/search/${keyword}`).then((res) => {
        console.log(res);
        this.$router.push({ name: "search" });
      });
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
    currentEditors() {
      let start = (this.currentPage - 1) * this.editorsPerPage;
      let end = this.currentPage * this.editorsPerPage;
      return this.editorsData.slice(start, end);
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
</style>
