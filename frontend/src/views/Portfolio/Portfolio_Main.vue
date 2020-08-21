<template>
  <div>
    <base-header
      class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;"
    >
      <span class="mask bg-gradient-success opacity-8"></span>
      <!-- 에디터 본인 일 경우에만 활성화 되어야한다. -->
      <router-link v-if="uid == $session.get('uid')" :to="'/portfolio/edit?no=' + this.uid">
        <base-button
          v-if="!haveTags && !haveVideo"
          type="info"
          class="btn btn-info float-right"
        >등록하기</base-button>
        <base-button
          v-if="haveTags || haveVideo"
          size="sm"
          type="info"
          class="btn btn-info float-right"
          icon="ni ni-settings"
        >수정하기</base-button>
      </router-link>
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-5 order-xl-2 mb-5 mb-xl-0">
          <div class="card card-profile shadow" style="min-height: 455px;">
            <div class="card-header">
              <h1>
                {{ portfolio.nickname }}
                <base-button
                  v-if="uid != $session.get('uid') && !togleBookmark"
                  outline
                  type="danger"
                  icon="ni ni-favourite-28"
                  @click="addBookmark()"
                >{{ portfolio.markCnt }}</base-button>
                <base-button
                  v-if="uid != $session.get('uid') && togleBookmark"
                  type="danger"
                  icon="ni ni-favourite-28"
                  @click="addBookmark()"
                >{{ portfolio.markCnt }}</base-button>
                <base-button
                  v-if="uid == $session.get('uid')"
                  disabled
                  type="danger"
                  icon="ni ni-favourite-28"
                >{{ portfolio.markCnt }}</base-button>
                <base-button
                  v-if="!isLogin"
                  size="sm"
                  type="default float-right"
                  @click="alertModal.show = true"
                >작업 요청하기</base-button>
                <base-button
                  v-if="uid != $session.get('uid') && isLogin"
                  size="sm"
                  type="default float-right"
                  @click="openRequestForm()"
                >작업 요청하기</base-button>
              </h1>
              <h3>{{ portfolio.description }}</h3>
              <div class="col">
                <button
                  class="btn btn-info btn-sm mb-1"
                  :key="index"
                  @click="searchTag(tag)"
                  v-for="(tag, index) in tags"
                >#{{ tag }}</button>
              </div>
            </div>
            <div class="card-body pt-0 pt-md-4">
              <div class="row">
                <div class="col">
                  <div class="text">
                    <!-- <div id="editor_skill"> -->
                    <h4 style="margin-bottom:3%;">
                      {{ portfolio.nickname }}님은 아래와 같은 기술을 보유하고
                      있습니다.
                    </h4>
                    <div class="col">
                      <div class="row">
                        <div class="col-6" v-for="(skill, index) in portfolio.skills" :key="index">
                          <div class="row">
                            <i class="ni ni-check-bold"></i>
                            <h3>{{ skill }}</h3>
                          </div>
                        </div>
                      </div>
                    </div>
                    <hr />
                    <div class="row">
                      <div class="col">
                        <h3>{{ portfolio.payMin }}원</h3>
                        <h5>분당가격</h5>
                      </div>

                      <div class="col">
                        <h3>{{ portfolio.workCnt }}건</h3>
                        <h5>총작업개수</h5>
                      </div>

                      <div class="col">
                        <h3>{{ portfolio.totalRate }}</h3>
                        <h5>총만족도</h5>
                      </div>

                      <div class="col">
                        <h3>{{ portfolio.responseTime }}</h3>
                        <h5>평균응답시간</h5>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-xl-7 order-xl-1">
          <card shadow type="secondary">
            <div class="HeadVideo">
              <LazyYoutubeVideo v-if="mainVideo.length > 31" :src="mainVideo" style="width: 100%" />
              <img v-else src="/img/theme/empty.PNG" alt="영상없음" style="width: 100%;"/>
            </div>
          </card>
        </div>
      </div>
    </div>

    <div class="container-fluid" style="margin-top: 20px">
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-header bg-transparent">
              <h2 class="mb-0">그 외 영상들</h2>
            </div>

            <div class="card-body">
              <div class="row">
                <div
                  class="col-xl-4 col-lg-6 mb-30"
                  style="margin-bottom:30px;"
                  v-for="(video, index) in videos"
                  :key="index"
                >
                  <LazyYoutubeVideo :src="video.url" :aspect-ratio="video.aspectRatio" />
                </div>
              </div>
              <hr />
              <div class="row" style="margin-top: 30px">
                <calendar
                  :eventCategories="eventCategories"
                  :events="events"
                  :offDays="offdays"
                  ref="calendar"
                  style="width: 100%; height: 100%"
                />
                <div class="row" style="margin-left: 40%;">
                  <i
                    class="fas fa-circle"
                    style="color: #6699ff; margin-right: 100px; margin-top: 20px; margin-bottom: 10px;"
                  >진행중 작업</i>
                  <i
                    class="fas fa-circle"
                    style="color: #c9c9c9; margin-right: 100px; margin-top: 20px; margin-bottom: 10px;"
                  >휴일</i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container-fluid" style="margin-top: 20px; margin-bottom: 50px;">
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-header bg-transparent">
              <h1 class="mb-0">서비스 평가</h1>
            </div>
            <div class="card-body pt-0 pt-md-4">
              <div class="row" style="margin-bottom: 30px; text-align:center;">
                <div class="col-xl-4 col-lg-6">
                  <div class="row">
                    <h3 style="margin-top:10px; margin-left:22%;">영상만족도</h3>
                    <rate id="satisfy" :length="5" :value="videoAvg" :disabled="true" />
                    <p style="margin-left:20%;">최종 영상에 대해 {{reviews.length}}분이 평가한 결과입니다.</p>
                  </div>
                </div>
                <div class="col-xl-4 col-lg-6 mb-30">
                  <div class="row">
                    <h3 style="margin-top:10px; margin-left:20%;">친절도</h3>
                    <rate :length="5" :value="kindnessAvg" :disabled="true" />
                    <p style="margin-left:10%;">
                      편집자가 얼마나 친절히 응대하고
                      <br />
                      소통하였는지에 대해 {{reviews.length}}분이 평가한 결과입니다.
                    </p>
                  </div>
                </div>
                <div class="col-xl-4 col-lg-6 mb-30">
                  <div class="row">
                    <h3 style="margin-top:10px; margin-left:20%;">마감 속도</h3>
                    <rate :length="5" :value="finishAvg" :disabled="true" />
                    <p style="margin-left:20%;">
                      마감 기한을 잘 맞춰주었는지에 대해
                      <br />
                      {{ reviews.length }}분이 평가한 결과입니다.
                    </p>
                  </div>
                </div>
              </div>
              <!-- </div> -->
              <div class="row">
                <div class="col ml-4">
                  <h2 style="margin-bottom:20px;">한줄평({{ reviews.length }})</h2>
                  <div class="col" v-for="(review, index) in reviewsMain" :key="index">
                    <div class="row">
                      <img
                        class="profile"
                        src="img/theme/user-logos.png"
                        style="margin-top:5px; margin-left:1.5%; width: 45px; height:30px;"
                      />
                      <h4
                        style="margin-top:11px; margin-right:10px; margin-left:7px;"
                      >{{ review.nickname.slice(0, 1) }}***</h4>
                      <h5
                        style="margin-top:11px; margin-right:10px;"
                      >{{ getFormatDate(review.createdDate) }}</h5>
                      <rate :length="5" :value="review.scoreAvg" :disabled="true" />
                    </div>
                    <div class="col" style="margin-top:10px; margin-left:20px;">
                      <h4>{{ review.comment }}</h4>
                    </div>
                    <hr class="mr-6" />
                  </div>
                  <div>
                    {{ this.reviewMessage }}
                    <base-button
                      v-if="this.reviewLoad"
                      size="sm"
                      type="float-right"
                      icon="ni ni-fat-add"
                      @click="loadMoreReviews()"
                    >더보기</base-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <modal :show.sync="modal.show">
      <template slot="header">
        <h5 class="modal-title" id="requestForm">작업 요청서</h5>
      </template>
      <div class="ml-2">
        <div class="row">
          <h3>작업 의뢰인</h3>
          <base-input
            class="mx-3"
            alternative
            placeholder="의뢰인"
            input-classes="form-control-alternative"
            disabled
            v-model="request_info.request_nickname"
          />
        </div>
        <div class="row">
          <h3>편집자</h3>
          <base-input
            class="mx-3"
            alternative
            placeholder="편집자"
            input-classes="form-control-alternative"
            disabled
            v-model="request_info.response_nickname"
          />
        </div>
        <div class="row">
          <h3>편집 목적</h3>
        </div>
        <div class="row d-flex justify-content-center">
          <div class="custom-control custom-radio">
            <input
              type="radio"
              name="video_type"
              value="pers"
              v-model="video_type.pers"
              id="pers"
              class="custom-control-input"
            />
            <label class="custom-control-label mr-4" for="pers">개인용</label>
          </div>
          <div class="custom-control custom-radio">
            <input
              type="radio"
              name="video_type"
              value="comm"
              v-model="video_type.comm"
              id="comm"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="comm">상업용</label>
          </div>
        </div>
        <div class="row">
          <h3 class="mb-3">영상 종류</h3>
        </div>
        <div class="row d-flex justify-content-center">
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="kids"
              v-model="video_style.kids"
              id="kids"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="kids">키즈</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="game"
              v-model="video_style.game"
              id="game"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="game">게임</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="musi"
              v-model="video_style.musi"
              id="musi"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="musi">음악/댄스</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="food"
              v-model="video_style.food"
              id="food"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="food">푸드/쿠킹</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="vlog"
              v-model="video_style.vlog"
              id="vlog"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="vlog">V-log</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="movi"
              v-model="video_style.movi"
              id="movi"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="movi">영화/애니메이션</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="anim"
              v-model="video_style.anim"
              id="anim"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="anim">동물</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="beau"
              v-model="video_style.beau"
              id="beau"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="beau">뷰티/패션</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="spor"
              v-model="video_style.spor"
              id="spor"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="spor">스포츠</label>
          </div>
          <div class="custom-control custom-radio col-5">
            <input
              type="radio"
              name="video_style"
              value="etcs"
              v-model="video_style.etcs"
              id="etcs"
              class="custom-control-input"
            />
            <label class="custom-control-label" for="etcs">기타</label>
          </div>
        </div>
        <div class="row d-flex justify-content-center mt-3">
          <base-input
            class="col-11 d-flex align-items-center"
            alternative
            placeholder="기타 선택 시 영상 종류를 입력해주세요"
            input-classes="form-control-alternative"
            v-model="request_info.video_style"
            disabled
            v-if="!video_style.etcs"
          />
          <base-input
            class="col-11 d-flex align-items-center"
            alternative
            placeholder="기타 선택 시 영상 종류를 입력해주세요"
            input-classes="form-control-alternative"
            v-model="request_info.video_style"
            v-if="!!video_style.etcs"
          />
        </div>
        <div class="row">
          <h3 class="mb-3">작업 기간</h3>
          <base-input addon-left-icon="ni ni-calendar-grid-50">
            <flat-picker
              slot-scope="{ focus, blur }"
              @on-open="focus"
              @on-close="blur"
              placeholder="시작날짜 - 종료날짜"
              :config="{ allowInput: true, mode: 'range' }"
              class="form-control datepicker"
              v-model="dates.range"
            ></flat-picker>
          </base-input>
        </div>
        <div class="row">
          <h3 class="mr-3">원본 영상 길이(분)</h3>
          <base-input
            alternative
            placeholder="80분 이내"
            input-classes="form-control-alternative"
            v-model="request_info.video_origin_length"
          />
        </div>
        <div class="row">
          <h3 class="mr-3">최종 영상 길이(분)</h3>
          <base-input
            alternative
            placeholder="15분 이내"
            input-classes="form-control-alternative"
            v-model="request_info.video_result_length"
          />
        </div>
        <div class="row">
          <h3 class="mb-3">원하는 영상 편집 기술</h3>
        </div>
        <div class="row d-flex justify-content-center">
          <base-checkbox
            class="mb-3"
            name="video_skill"
            value="colr"
            v-model="video_skill.colr"
          >색, 밝기 조정</base-checkbox>
          <base-checkbox
            class="mb-3"
            name="video_skill"
            value="audi"
            v-model="video_skill.audi"
          >음향(오디오, 음악)</base-checkbox>
          <base-checkbox
            class="mb-3"
            name="video_skill"
            value="moti"
            v-model="video_skill.moti"
          >모션그래픽</base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="capt" v-model="video_skill.capt">자막</base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="intr" v-model="video_skill.intr">인트로</base-checkbox>
          <base-checkbox
            class="mb-3"
            name="video_skill"
            value="outr"
            v-model="video_skill.outr"
          >아웃트로</base-checkbox>
        </div>
        <h3 class="my-3">기타 요구사항</h3>
        <textarea
          class="form-control form-control-alternative"
          id="description"
          v-model="request_info.request_description"
          rows="3"
          placeholder="기타 요구사항을 작성해주세요."
        ></textarea>
      </div>
      <template slot="footer">
        <base-button type="secondary" @click="modal.show = false">Close</base-button>
        <base-button type="primary" @click="beforeCheck()">요청하기</base-button>
      </template>
    </modal>

    <modal
      :show.sync="alertModal.show"
      gradient="danger"
      modal-classes="modal-danger modal-dialog-centered"
    >
      <div class="py-3 text-center">
        <i class="ni ni-bell-55 ni-3x"></i>
        <h4 class="heading mt-4">로그인 후 이용이 가능한 서비스입니다.</h4>
      </div>

      <template slot="footer">
        <router-link to="/login">
          <base-button class text-color="white" type="link" @click="alertModal.show = false">로그인</base-button>
        </router-link>
        <router-link to="/user">
          <base-button type="link" text-color="white" @click="alertModal.show = false">회원가입</base-button>
        </router-link>
        <br />
        <base-button
          type="link"
          text-color="white"
          class="ml-auto"
          @click="alertModal.show = false"
        >Close</base-button>
      </template>
    </modal>
  </div>
</template>

<script>
// import InputTag from 'vue-input-tag';
import LazyYoutubeVideo from "vue-lazy-youtube-video";
import { Rate } from "vue-rate";
// import { BadgerAccordion, BadgerAccordionItem } from "vue-badger-accordion";
import flatPicker from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import "vue-rate/dist/vue-rate.css";

import { Calendar } from "vue-sweet-calendar";
import "vue-sweet-calendar/dist/SweetCalendar.css";

import alertify from "alertifyjs";

// axios 초기 설정파일
import http from "@/util/http-common";

import moment from "moment";
require("moment-timezone");
moment.tz.setDefault("Asia/Seoul");
moment.locale("ko");

// import { mapGetters } from "vuex";
// import store from "@/store/store.js";
// 날짜 계산 파일
// import { getFormatDate } from "@/util/day-common";
// import { getEndDate, getFormatDate } from "@/util/day-common";

export default {
  name: "user-portfolio",
  components: {
    LazyYoutubeVideo,
    Rate,
    flatPicker,
    // InputTag,
    Calendar,
  },
  data() {
    return {
      uid: "",
      isLogin: false,
      userType: "",
      haveVideo: true,
      haveTags: true,

      //닉네임, 소개
      portfolio: {
        nickname: "",
        description: "",
        payMin: "",
        skills: [],
        markCnt: 0,
        workCnt: 0,
        totalRate: "",
        responseTime: "",
      },
      // 각 평점
      videoAvg: 0,
      kindnessAvg: 0,
      finishAvg: 0,

      //리뷰들
      reviews: [],
      reviewsMain: [],
      cnt: 1,
      reviewMessage: "",
      reviewLoad: false,

      //태그들
      tags: [],
      tag: "",

      // 북마크 토글
      togleBookmark: false,

      // 스케줄
      eventCategories: [
        {
          id: 1,
          title: "holiday",
          textColor: "white",
          backgroundColor: "#ff0066",
        },
        {
          id: 2,
          title: "selected",
          textColor: "white",
          backgroundColor: "#f29661",
        },
        {
          id: 3,
          title: "In Progress",
          textColor: "white",
          backgroundColor: "#6699ff",
        },
        {
          id: 4,
          title: "DisabledDates",
          textColor: "black",
          backgroundColor: "#c9c9c9",
        },
      ],
      events: [],
      offdays: [],

      mainVideo: "https://www.youtube.com/embed/",
      videos: [],

      modal: {
        show: false,
      },
      alertModal: {
        show: false,
      },

      dates: {
        range: "",
      },

      request_info: {
        request_nickname: "",
        response_nickname: "",
        start_date: "",
        end_date: "",
        video_origin_length: "",
        video_result_length: "",
        video_type: "",
        request_description: "",
        // rid: 0,
        tag_list: "",
        video_skill: "",
        video_style: "",
        done_flag: 0,
      },
      video_skill: {
        colr: false,
        audi: false,
        moti: false,
        capt: false,
        intr: false,
        outr: false,
      },
      video_type: {
        pers: false,
        comm: false,
      },
      video_style: {
        kids: false,
        game: false,
        musi: false,
        food: false,
        vlog: false,
        movi: false,
        anim: false,
        beau: false,
        spor: false,
        etcs: false,
      },
    };
  },
  computed: {
    queryNo() {
      return this.$route.query.no;
    },
  },
  mounted() {
    //스크롤 맨 위로 올리기
    window.scrollTo(0, 0);
  },
  watch: {
    // 같은 화면에서 다른 query값을 이용하기 위한 watch
    queryNo() {
      this.uid = this.$route.query.no;

      let URL = "/portfolio";
      //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
      //포트폴리오 정보
      this.getPortfolio(URL);

      //포트폴리오 리뷰
      this.getReviewInfo(URL);

      // 포트폴리오 영상
      this.getVideoInfo(URL);

      // 포트폴리오 스케쥴
      this.getScheduleInfo(URL);

      // 포트폴리오 태그
      this.getTagInfo(URL);

      // 북마크 정보 가져와서 북마크 한 인원수 보여주기
      this.getBookmarkCount();
    },
  },
  created() {
    // 잘못된 url 접근시 이동
    if (
      !this.$route.query.no ||
      this.$route.query.no == "" ||
      !this.checkNumberFormat(this.$route.query.no)
    ) {
      this.$router.push("/");
      return;
    }
    if (this.$session.exists()) {
      this.isLogin = true;
    }

    this.uid = this.$route.query.no;
    this.request_info.request_nickname = this.$session.get("nickname");
    let URL = "/portfolio";

    //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
    //포트폴리오 정보
    this.getPortfolio(URL);

    //포트폴리오 리뷰
    this.getReviewInfo(URL);

    // 포트폴리오 영상
    this.getVideoInfo(URL);

    // 포트폴리오 스케쥴
    this.getScheduleInfo(URL);

    // 포트폴리오 태그
    this.getTagInfo(URL);

    // 북마크 정보 가져와서 북마크 한 인원수 보여주기
    this.getBookmarkCount();

    // console.log(this.events);
  },
  methods: {
    beforeCheck() {
      // 요청 양식에 따라 가공
      // this.dates.range (YYYY-MM-DD || YYYY-MM-DD to YYYY-MM-DD)
      let dates = this.dates.range.split("to");
      //dates의 length가 1이라면 term=0
      //dates의 length가 2라면 term은 두 날의 차이이다.
      this.request_info.start_date = dates[0];
      if (dates.length == 1) {
        this.request_info.end_date = dates[0];
      } else if (dates.length == 2) {
        this.request_info.end_date = dates[1];
      }

      if (this.video_type.pers) {
        this.request_info.video_type = "pers";
      }
      if (this.video_type.comm) {
        this.request_info.video_type = "comm";
      }

      if (this.video_style.kids) {
        this.request_info.video_style = "kids";
      }
      if (this.video_style.game) {
        this.request_info.video_style = "game";
      }
      if (this.video_style.musi) {
        this.request_info.video_style = "musi";
      }
      if (this.video_style.food) {
        this.request_info.video_style = "food";
      }
      if (this.video_style.vlog) {
        this.request_info.video_style = "vlog";
      }
      if (this.video_style.movi) {
        this.request_info.video_style = "movi";
      }
      if (this.video_style.anim) {
        this.request_info.video_style = "anim";
      }
      if (this.video_style.beau) {
        this.request_info.video_style = "beau";
      }
      if (this.video_style.spor) {
        this.request_info.video_style = "spor";
      }

      if (this.video_skill.colr) {
        this.request_info.video_skill += ",colr";
      }
      if (this.video_skill.audi) {
        this.request_info.video_skill += ",audi";
      }
      if (this.video_skill.moti) {
        this.request_info.video_skill += ",moti";
      }
      if (this.video_skill.capt) {
        this.request_info.video_skill += ",capt";
      }
      if (this.video_skill.intr) {
        this.request_info.video_skill += ",intr";
      }
      if (this.video_skill.outr) {
        this.request_info.video_skill += ",outr";
      }
      this.checkRequestForm();
    },
    checkRequestForm() {
      let valid = true;
      let message = "";
      // 필수 입력이 되었는지 확인
      valid &&
        !this.request_info.request_nickname &&
        ((valid = false), (message = "의뢰인의 닉네임이 없습니다."));
      valid &&
        !this.request_info.response_nickname &&
        ((valid = false), (message = "편집자의 닉네임이 없습니다."));
      valid &&
        !this.request_info.video_type &&
        ((valid = false), (message = "편집 목적을 선택해주세요."));
      valid &&
        !this.request_info.video_style &&
        ((valid = false), (message = "영상 종류를 선택해주세요."));
      valid &&
        !this.dates.range &&
        ((valid = false), (message = "기간을 입력해주세요"));
      valid &&
        !this.request_info.video_origin_length &&
        ((valid = false), (message = "원본 영상 길이를 입력해주세요"));
      valid &&
        !this.request_info.video_result_length &&
        ((valid = false), (message = "최종 영상 길이를 입력해주세요"));

      //숫자인지 체크
      // valid &&
      //   !this.checkNumberFormat(this.request_info.video_origin_length) &&
      //   ((valid = false), (message = "숫자만 입력해주세요"));
      // valid &&
      //   !this.checkNumberFormat(this.request_info.video_result_length) &&
      //   ((valid = false), (message = "숫자만 입력해주세요"));
      // valid && !this.request_info.video_type && ((valid = false), (message = '원하는 영상 종류를 입력해주세요'))

      if (valid) this.requestForm();
      else {
        alertify.error(message, 3);
        return;
      }
    },
    checkNumberFormat(number) {
      let exptext = /^[0-9]/;
      //숫자로만 이루어져 있는지 확인
      if (exptext.test(number) == false) {
        return false;
      }
      return true;
    },
    requestForm() {
      http
        .post("/request", this.request_info)
        .then(({ data }) => {
          if (data == "success") {
            // console.log("요청사항 완료")
            this.initModalRequest();
            this.$store.commit("stepper/clearFilterFinderStatus");
            alertify.notify("작업 요청 완료", "success", 3);
            return;
          } else {
            // console.log("요청사항 실패")
            // fail
            return;
          }
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    initModalRequest() {
      this.modal.show = false;
      this.request_info = {
        request_nickname: this.$session.get('nickname'),
        response_nickname: this.portfolio.nickname,
        start_date: "",
        end_date: "",
        video_origin_length: "",
        video_result_length: "",
        video_type: "",
        request_description: "",
        tag_list: "",
        video_skill: "",
        video_style: "",
        done_flag: 0,
      };
      this.dates= {
        range: ""
      };
      this.video_skill= {
        colr: false,
        audi: false,
        moti: false,
        capt: false,
        intr: false,
        outr: false,
      };
      this.video_type= {
        pers: false,
        comm: false,
      };
      this.video_style= {
        kids: false,
        game: false,
        musi: false,
        food: false,
        vlog: false,
        movi: false,
        anim: false,
        beau: false,
        spor: false,
        etcs: false,
      };
    },
    getWorkCount() {
      http
        .get("/request/res/" + this.portfolio.nickname + "/2")
        .then(({ data }) => {
          this.portfolio.workCnt = data.length;
          if (data.length > 0) this.getAvgResponseTime(data);
          else {
            this.portfolio.responseTime = "-";
          }
          return;
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getWorkCount2() {
      http
        .get("/request/res/" + this.portfolio.nickname + "/3")
        .then(({ data }) => {
          this.portfolio.workCnt += data.length;
          if (data.length > 0) this.getAvgResponseTime(data);
          else {
            this.portfolio.responseTime = "-";
          }
          return;
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getInprogressDate() {
      http
        .get("/request/res/" + this.portfolio.nickname + "/1")
        .then(({ data }) => {
          if (data.length > 0) {
            this.makeInprogressDateArray(data);
          }
          return;
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getScheduleInfo(URL) {
      http
        .get(URL + "/schedule/" + this.uid)
        .then(({ data }) => {
          if (data.data == "success") {
            // scheduleType=0 기본
            // let result = data.object.filter(schedule => schedule.scheduleType == 0);
            this.makeScheduleArray(data.object);
            // console.log(data.object);
            return;
          } else {
            return;
          }
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getTagInfo(URL) {
      http.get(URL + "/tag/" + this.uid).then(({ data }) => {
        //성공시 평균 계산 필요 추출 필요
        if (data.data == "success") {
          this.haveTags = true;
          this.tags = [];
          data.object.forEach((obj) => {
            this.tags.push(obj.tagName);
          });
          return;
        } else {
          this.haveTags = false;
          return;
        }
      });
    },
    getReviewInfo(URL) {
      http
        .get(URL + "/review/" + this.uid)
        .then(({ data }) => {
          //성공시 평균 계산 필요 추출 필요
          if (data.data == "success") {
            this.reviews = data.object;
            let videoAvg = 0,
              kindnessAvg = 0,
              finishAvg = 0;
            data.object.forEach((obj) => {
              videoAvg += obj.videoScore;
              kindnessAvg += obj.kindnessScore;
              finishAvg += obj.finishScore;
            });
            let length = data.object.length;
            this.videoAvg = videoAvg / length;
            this.kindnessAvg = kindnessAvg / length;
            this.finishAvg = finishAvg / length;
            this.portfolio.totalRate =
              ((this.videoAvg + this.kindnessAvg + this.finishAvg) / 3) * 20;
            if (isNaN(this.portfolio.totalRate)) this.portfolio.totalRate = "-";
            else
              this.portfolio.totalRate = this.round(this.portfolio.totalRate);
            // console.log(this.portfolio.totalRate);

            if (this.reviews.length == 0) {
              this.reviewLoad = false;
              this.reviewMessage = "아직 리뷰가 존재하지 않습니다.";
            } else {
              this.reviewsMain = this.reviews.reverse();
              if (this.reviews.length > 5) {
                this.reviewLoad = true;
                this.reviewsMain = this.reviewsMain.slice(0, 5);
              } else {
                this.reviewLoad = false;
              }
            }
            return;
          } else {
            return;
          }
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getVideoInfo(URL) {
      //1은 session uid
      http
        .get(URL + "/video/" + this.uid)
        .then(({ data }) => {
          //성공시 video 추출 필요
          if (data.data == "success") {
            this.haveVideo = true;
            //main이 아닌것들만 추출
            let result = data.object.filter((video) => video.mainFlag == 0);
            this.videos = this.makeVideosArray(result);

            result = data.object.filter((video) => video.mainFlag == 1);
            this.mainVideo = result[0].url;
            return;
          } else {
            this.haveVideo = false;
            return;
          }
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getPortfolio(URL) {
      //1은 session uid
      http
        .get(URL + "/portfolio/" + this.uid)
        .then(({ data }) => {
          //성공시 video 추출 필요
          if (data.data == "success") {
            this.portfolio.nickname = data.object.nickname;
            // console.log(data.object);
            this.request_info.response_nickname = data.object.nickname;
            this.portfolio.description = data.object.description;
            this.portfolio.payMin = data.object.payMin;
            // this.portfolio.skill = data.object.skill;
            this.portfolio.skills = data.object.skill.split(",");
            // console.log(this.portfolio.skills);
            this.getInprogressDate();
            this.getWorkCount();
            this.getWorkCount2();
            return;
          } else {
            return;
          }
        })
        .catch((error) => {
          console.log(error);
          return;
        });
    },
    getBookmarkCount() {
      http
        .get("/bookmark/cnt/" + this.uid)
        .then(({ data }) => {
          if (data.data == "success") {
            this.portfolio.markCnt = data.object.length;
            data.object.forEach((obj) => {
              if (obj.userInfoUid == this.$session.get("uid")) {
                this.togleBookmark = true;
                return;
              } else {
                this.togleBookmark = false;
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
    },
    addBookmark() {
      if (!this.togleBookmark) {
        http
          .post("/bookmark", {
            uid: this.$session.get("uid"),
            muid: this.uid,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.togleBookmark = true;
              this.getBookmarkCount();
              return;
            } else {
              return;
            }
          });
      } else {
        http
          .post("/bookmark/delete", {
            uid: this.$session.get("uid"),
            muid: this.uid,
          })
          .then(({ data }) => {
            if (data.data == "success") {
              this.togleBookmark = false;
              this.getBookmarkCount();
              return;
            } else {
              return;
            }
          });
      }
    },
    //성공했을 경우 각 객체 돌면서 비디오 추출 필요
    makeVideosArray(result) {
      let obj;
      let res = [];
      result.forEach((element) => {
        obj = new Object();
        obj.url = element.url;
        res.push(obj);
      });
      return res;
    },
    makeScheduleArray(result) {
      let date;
      result.forEach((element) => {
        date = new Object();
        date.start = new Date(element.startDate);
        date.end = new Date(element.endDate);
        date.categoryId = element.scheduleType;
        date.repeat = "never";
        this.events.push(date);
      });
    },
    makeInprogressDateArray(result) {
      let obj;
      result.forEach((element) => {
        obj = new Object();
        obj.start = new Date(element.start_date);
        obj.end = new Date(element.end_date);
        obj.categoryId = 3;
        obj.repeat = "never";
        this.events.push(obj);
      });
    },
    goToday() {
      this.$refs.calendar.goToday();
    },
    loadMoreReviews() {
      this.cnt++;
      // console.log(this.cnt);
      if (this.reviewsMain.length < this.reviews.length) {
        let sub = this.reviews.length - this.reviewsMain.length;
        if (sub < 5) {
          this.reviewLoad = false;
          this.reviewsMain = this.reviews.slice(0, this.reviews.length);
        } else {
          this.reviewLoad = true;
          this.reviewsMain = this.reviews.slice(0, 5 * this.cnt);
        }
      } else {
        this.reviewLoad = false;
        this.reviewMessage = "더 이상 후기가 존재하지 않습니다";
      }
    },
    openRequestForm() {
      this.modal.show = true;
      let selectedType = this.$store.getters["stepper/getSelectedVideoType"];
      if (selectedType) {
        if (selectedType.value == "pers") this.video_type.pers = "pers";
        else if (selectedType.value == "comm") this.video_type.comm = "comm";
      }
      let selectedStyle = this.$store.getters["stepper/getSelectedVideoStyle"].value;
      if (selectedStyle) {
        if (selectedStyle == "kids") this.video_style.kids = "kids";
        else if (selectedStyle == "game") this.video_style.game = "game";
        else if (selectedStyle == "musi") this.video_style.musi = "musi";
        else if (selectedStyle == "food") this.video_style.food = "food";
        else if (selectedStyle == "vlog") this.video_style.vlog = "vlog";
        else if (selectedStyle == "movi") this.video_style.movi = "movi";
        else if (selectedStyle == "anim") this.video_style.anim = "anim";
        else if (selectedStyle == "beau") this.video_style.beau = "beau";
        else if (selectedStyle == "spor") this.video_style.spor = "spor";
        else if (selectedStyle == "etc") this.video_style.etcs = "etc";
      }
      let selectedSkills = this.$store.getters["stepper/getSelectedVideoSkills"];
      if (selectedSkills) {
        selectedSkills.forEach((item) => {
          if (item == "colr") this.video_skill.colr = true;
          else if (item == "capt") this.video_skill.capt = true;
          else if (item == "audi") this.video_skill.audi = true;
          else if (item == "moti") this.video_skill.moti = true;
          else if (item == "outr") this.video_skill.outr = true;
          else if (item == "intr") this.video_skill.intr = true;
        });
      }
      let originLength = this.$store.getters["stepper/getOriginLength"];
      this.request_info.video_origin_length = originLength.name;

      let finalLength = this.$store.getters["stepper/getFinalLength"];
      this.request_info.video_result_length = finalLength.name;
      this.dates.range = this.$store.getters["stepper/getDeadline"];
      // console.log(this.dates.range);
    },
    getFormatDate(regtime) {
      return moment(new Date(regtime)).format("YY.MM.DD hh:mm");
    },
    round(score) {
      var result = Number(score.toFixed(1));
      return result + "%";
    },
    getAvgResponseTime(list) {
      let time;
      let totalTime = 0;
      list.forEach((element) => {
        if (element.response_date != null && element.request_date != null) {
          time = moment(element.response_date).diff(
            moment(element.request_date),
            "minutes"
          );
          totalTime += time;
          // console.log(element.response_date + " " + element.request_date + " " + totalTime);
        }
      });
      totalTime /= list.length;
      if (totalTime >= 720) {
        this.portfolio.responseTime = "하루 이내";
      } else if (360 < totalTime && totalTime < 720) {
        this.portfolio.responseTime = "12시간 이내";
      } else if (180 < totalTime && totalTime <= 360) {
        this.portfolio.responseTime = "6시간 이내";
      } else if (120 < totalTime && totalTime <= 180) {
        this.portfolio.responseTime = "3시간 이내";
      } else if (60 < totalTime && totalTime <= 120) {
        this.portfolio.responseTime = "2시간 이내";
      } else {
        this.portfolio.responseTime = "1시간 이내";
      }
      // console.log(totalTime);
    },
    searchTag(tag) {
      // Editors.vue로 props를 이용하여 보내 태그 검색이 가능하도록 한다.
      // console.log(tag);
      this.$router.push({ name: "editors", params: { clickSearchTag: tag } });
    },
  },
};
</script>
<style scoped>
/* // #editor_skill{
//   background-color: #d6d6d6;
// } */
.custom-control {
  margin-right: 40px;
}

.custom-control {
  padding-right: 10px;
  margin-right: 0px;
}
</style>
