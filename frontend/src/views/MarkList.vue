<template>
  <div>
    <base-header
      class="header pb-4 pt-2 pt-lg-5 d-flex align-items-center"
      style="
        min-height: 100px;
        background-size: cover;
        background-position: center top;
      "
    >
      <!-- Mask -->
      <span class="mask bg-gradient-success opacity-8"></span>
    </base-header>

    <br />

    <hr />
    <h2 class="text-center">{{nickname}}님의 북마크 리스트</h2>
    <br />
    <div class="container bookmark">
      <div class="row justify-content-center">
        <div
          v-for="(bookmark, index) in bookmarks"
          :key="index"
          class="card col-lg-3 col-6 mr-5 mb-3 zoom"
          style="width: 18rem; cursor:pointer;"
          @click="movePortfolio(bookmark.muid)"
        >
          <br />
          <!-- src는 해당 포트폴리오의 main 동영상 링크 가져오기 -->
          <!-- 227.5 * 127.969 -->
          <img v-if="bookmark.mainUrl" :src="'https://img.youtube.com/vi/' + getMainVideo(bookmark.mainUrl).substr(30) + '/0.jpg'" 
            style="width: 100%;"/>
          <!-- <LazyYoutubeVideo
            v-if="bookmark.mainUrl"
            :src="getMainVideo(bookmark.mainUrl)"
            style="width: 100%;"
          /> -->
          <!-- main video가 없을때 보여지는 img -->
          <img
            v-else
            src="/img/theme/empty.PNG"
            alt="영상없음"
            style="width: 100%; height: 127.969px;"
          />
          <!-- src="https://www.youtube.com/embed/5iSlfF8TQ9k" -->
          <hr class="my-1" />
          <!-- card body에 해당 포트폴리오로 이동 할 수 있도록 링크 달기 -->
          <div class="card-body">
            <h3 class="card-title">{{bookmark.nickname}}</h3>
            <p class="card-text">
              <!-- 해당 포트폴리오의 태그 불러와서 나열 -->
              {{getTag(bookmark.tags)}}
            </p>
            <hr class="my-1" />
            <p class="card-text">
              <!-- 해당 포트폴리오의 pay/min 불러와서 작성 -->
              가격: {{getPayMin(bookmark.payMin)}}
              <!-- <br /> -->
              <!-- 해당 포트폴리오의 평점 계산해서 작성 -->
              <!-- <h3> -->
              <!-- 평점: {{getAvg(bookmark)}} -->
              <!-- </h3> -->
            </p>
            <!-- 아래는 평점을 별로 나타내려 하였으나 2줄로 출력되어 보류 -->
            <!-- <rate :length="5" :value="getAvg(bookmark)" :readonly="true" /> -->
            <!-- style="bottom:15px;position:absolute;right:15px;" -->
            <div class="d-flex justify-content-end">
              <!-- 하트 표시 삭제 -->
              <!-- <div @click.stop="deleteBookmark(bookmark.muid)">
                <svg
                  width="1em"
                  height="1em"
                  viewBox="0 0 16 16"
                  class="bi bi-heart-fill"
                  fill="red"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fill-rule="evenodd"
                    d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"
                  />
                </svg>
              </div>-->
              <div @click.stop="deleteBookmark(bookmark.muid)">
                <base-button type="danger" class="btn">삭제</base-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br />
  </div>
</template>

<script>
// import { Rate } from "vue-rate";
// import 'vue-rate/dist/vue-rate.css'
// import LazyYoutubeVideo from "vue-lazy-youtube-video";

import http from "@/util/http-common";
import alertify from "alertifyjs";
export default {
  name: "marklist",
  components: {
    // LazyYoutubeVideo,
    // Rate,
  },
  data() {
    return {
      nickname: "",
      bookmarks: [
        /*
          {
            muid:'',
            nickname:'',
            payMin:'',
            mainUrl:'',
            // avgV:'',
            // avgK:'',
            // avgF:'',
            tags:[],
          }
        */
      ],
    };
  },
  created() {
    if (this.$session.exists()) {
      this.nickname = this.$session.get("nickname");
    }
    // 현재 사용자의 북마크 리스트 가져오기
    this.bookmarksInit();
  },
  methods: {
    bookmarksInit() {
      http
        .get("/bookmark/" + this.$session.get("uid"))
        .then(({ data }) => {
          // 북마크 조회 성공
          if (data.data == "success") {
            //추후 삭제 필요
            // alertify.notify("북마크 조회 성공", "success", 3);
            this.bookmarks = data.object;
          }
          return;
        })
        .catch(() => {
          // 북마크 조회 실패
          alertify.error("북마크 조회가 실패하였습니다.", 3);
          return;
        });
    },
    // 평점의 평균 구하기
    // getAvg(bookmark){
    //   return ((Number(bookmark.avgV) + Number(bookmark.avgK)+ Number(bookmark.avgF))/3).toFixed(1);
    // },
    getPayMin(payMin) {
      if (payMin == "") {
        return "미정";
      } else {
        return payMin + " 원/분";
      }
    },
    getTag(tags) {
      if (tags.length == 0) {
        return "";
      } else {
        let message = "";
        for (let i = 0; i < tags.length && i < 5; ++i) {
          message += "#" + tags[i] + " ";
        }
        return message;
      }
    },
    getMainVideo(mainUrl) {
      if (mainUrl == null || mainUrl == "") {
        return "";
      } else {
        return mainUrl;
      }
    },
    deleteBookmark(muid) {
      let message = "";
      let uid = this.$session.get("uid");
      let vue = this;
      alertify.confirm(
        "북마크 삭제",
        "삭제 하시겠습니까?",
        function () {
          http
            .post("/bookmark/delete", {
              uid: uid,
              muid: muid,
            })
            .then(({ data }) => {
              if (data.data == "success") {
                message = "삭제가 완료되었습니다.";
                alertify.notify(message, "success", 3);
                // 삭제 완료한 뒤 북마크 갱신
                vue.bookmarksInit();
                // vue.$router.go();
                return;
              } else {
                message = "삭제에 실패하였습니다.";
                alertify.error(message, 3);
                return;
              }
            })
            .catch(() => {
              message = "북마크 삭제 서버 통신 실패";
              alertify.error(message, 3);
              return;
            });
        },
        function () {
          // alertify.error('취소되었습니다.')
        }
      );
    },
    movePortfolio(muid) {
      this.$router.push("/portfolio?no=" + muid);
    },
  },
};
</script>

<style>
.bookmark {
  min-height: 50vh;
}

.zoom:hover {
  -ms-transform: scale(1.03);
  -webkit-transform: scale(1.03);
  transform: scale(1.03);
}
</style>