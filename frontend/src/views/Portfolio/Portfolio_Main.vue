<template>
  <div>
    <base-header class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;">
      <span class="mask bg-gradient-success opacity-8"></span>
      <!-- 에디터 본인 일 경우에만 활성화 되어야한다. -->
      <router-link v-if="uid == $session.get('uid') " :to="'/portfolio/edit?no='+this.uid">
        <base-button size="sm" type="info" class="btn btn-info float-right"
          >Edit profile</base-button
        >
      </router-link>
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-7 order-xl-2 mb-5 mb-xl-0">
          <div class="card card-profile shadow">
            <div class="card-body pt-0 pt-md-4" style="height: 295px;">
              <div class="row">
                <div class="col">
                  <div class="text">
                    <h1>
                      {{portfolio.nickname}}
                      <base-button outline type="danger" icon="ni ni-favourite-28" @click="addBookmark()">
                      {{portfolio.markCnt}}
                      </base-button>
                      <!-- 에디터 본인 일 경우에만 비활성화 되어야한다. -->
                      <base-button size="sm" type="default float-right" @click="modal.show=true"> 작업 요청하기 </base-button>
                    </h1>
                    <h3>
                      Skills: {{portfolio.skill}}
                    </h3>
                    <h3>
                      Pay/Wages: {{portfolio.payMin}}
                    </h3>
                    <h3>
                      {{portfolio.description}}
                    </h3>
                    
                    <div class="col-xl-5 order-xl-1">
                      <!-- <div class="row"> -->
                      <i class="ni ni-tag"></i>
                      <input-tag v-model="tags" :read-only="true" style="width:500px;"></input-tag>
                      </div>
                    <!-- </div> -->
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-xl-5 order-xl-1">
          <card shadow type="secondary">
            <div class="HeadVideo">
              <LazyYoutubeVideo :src="mainVideo" style="width: 100%" />
            </div>
            <h3 class="mb-0" style="text-align:center">대표영상</h3>
          </card>
        </div>
      </div>
    </div>

    <div class="container-fluid" style="margin-top: 20px">
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-header bg-transparent">
              <h3 class="mb-0">그 외 영상들</h3>
            </div>

            <div class="card-body">
              <div class="row">
                <div class="col-xl-4 col-lg-6 mb-30" v-for="(video, index) in videos" :key="index">
                  <LazyYoutubeVideo
                    :src="video.url"
                    :preview-image-size="video.previewImageSize"
                    :aspect-ratio="video.aspectRatio"
                  />
                </div>
              </div>

              <div class="row" style="margin-top: 30px">
                <div class="col-xl-4 col-lg-6">
                  <h3>
                    영상만족도
                    <rate
                      id="satisfy"
                      :length="5"
                      :value="videoAvg"
                      :disabled="true"
                    />
                  </h3>
                  <h3>
                    친절도
                    <rate :length="5" :value="kindnessAvg" :disabled="true" />
                  </h3>
                  <h3>
                    마감 속도
                    <rate :length="5" :value="finishAvg" :disabled="true" />
                  </h3>
                </div>
                <div class="col-xl-4 col-lg-6">
                  <vc-calendar
                    
                    
                    :disabled-dates='disableDates'
                    :attributes='events'
                  />
                  {{events[1]}}
                  <!-- {{events.dates}} -->
                  <!-- <calendar
                    :eventCategories="eventCategories"
                    :events="events"
                    :offDays="disableDates"
                    ref="calendar"
                    style="float:left; width: 40%; height: 100%"
                  /> -->
                </div>
                <div class="col-xl-4 col-lg-6">
                  <h3>한줄평</h3>
                  <badger-accordion>
                    <badger-accordion-item v-for="(review, index) in reviews" :key="index">
                      <template slot="header">{{review.comment}}</template>    
                      <template slot="content">
                        <h3>
                          영상만족도
                          <rate
                            :length="5"
                            :value="review.videoScore"
                            :disabled="true"
                          />
                        </h3>
                        <h3>
                          친절도
                          <rate
                            :length="5"
                            :value="review.kindnessScore"
                            :disabled="true"
                          />
                        </h3>
                        <h3>
                          마감 속도
                          <rate
                            :length="5"
                            :value="review.finishScore"
                            :disabled="true"
                          />
                        </h3>
                      </template>
                    </badger-accordion-item>
                  </badger-accordion>
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
      <div>
        <div class="row">
          <h3>작업 의뢰인</h3>
          <base-input
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
            alternative
            placeholder="편집자"
            input-classes="form-control-alternative"
            disabled
            v-model="request_info.response_nickname"
          />
        </div>
        <div class="row">
          <h3>작업 기간</h3>
          <base-input addon-left-icon="ni ni-calendar-grid-50">
            <flat-picker
              slot-scope="{ focus, blur }"
              @on-open="focus"
              @on-close="blur"
              :config="{ allowInput: true, mode: 'range' }"
              class="form-control datepicker"
              v-model="dates.range"
            >
            </flat-picker>
          </base-input>
        </div>
        <div class="row">
          <h3>원본 영상 길이(분)</h3>
          <base-input
            alternative
            placeholder="80"
            input-classes="form-control-alternative"
            v-model="request_info.video_origin_length"
          />
        </div>
        <div class="row">
          <h3>최종 영상 길이(분)</h3>
          <base-input
            alternative
            placeholder="15"
            input-classes="form-control-alternative"
            v-model.number="request_info.video_result_length"
          />
        </div>
        <div class="row">
          <h3>원하는 영상</h3>
          <base-input
            alternative
            placeholder="인트로"
            input-classes="form-control-alternative"
            v-model="request_info.video_type"
          />
        </div>
          <label for="description">기타 요구사항</label>
          <textarea class="form-control form-control-alternative" id="description" v-model="request_info.request_description" rows="3" placeholder="기타 요구사항을 작성해주세요."></textarea>
        </div>
     <template slot="footer">
         <base-button type="secondary" @click="modal.show = false">Close</base-button>
         <base-button type="primary" @click="checkRequestForm()">요청하기</base-button>
     </template>
   </modal>
  </div>
</template>
<script>
import InputTag from 'vue-input-tag';
import LazyYoutubeVideo from "vue-lazy-youtube-video";
import { Rate } from "vue-rate";
import { BadgerAccordion, BadgerAccordionItem } from "vue-badger-accordion";
import flatPicker from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import "vue-rate/dist/vue-rate.css";

// import { Calendar } from "vue-sweet-calendar";
// import "vue-sweet-calendar/dist/SweetCalendar.css";

import alertify from "alertifyjs"

// axios 초기 설정파일
import http from "@/util/http-common";
// 날짜 계산 파일
import { getFormatDate } from "@/util/day-common";
// import { getEndDate, getFormatDate } from "@/util/day-common";

// import { mapGetters } from "vuex";
// import store from "@/store/store.js";

  export default {
    name: 'user-portfolio',
    components: {
      LazyYoutubeVideo,
      Rate,
      BadgerAccordion,
      BadgerAccordionItem,
      flatPicker,
      InputTag,
      // Calendar
    },
    data() {
      return {
        uid:'',
        //닉네임, 소개
        portfolio: {
          nickname:'',
          description:'',
          payMin: '',
          skill: '',
          markCnt: 0,
        },
        // 각 평점
        videoAvg: 0,
        kindnessAvg: 0,
        finishAvg: 0,

        //리뷰들
        reviews: [],

        //태그들
        tags:[],
        tag: '',

        // 북마크 토글
        togleBookmark: false,

        // 스케줄
        disableDates: [],
        events: [
          {
            highlight: {
              backgroundColor: '#ff8080',
              },
            dates: [
            ],
          }
        ],
        
        mainVideo: 'https://www.youtube.com/embed/',
        videos: [],

        modal: {
          show: false
        },
        dates: {
          range: ""
        },
        request_info:{
          request_nickname: '',
          response_nickname: '',
          start_date: '',
          end_date: '',
          video_origin_length: '',
          video_result_length: '',
          video_type: '',
          request_description: '',
          // rid: 0,
          tag_list: '',
          video_skill: '',
          video_style: '',
          done_flag: 0,
        },

      }
    },
    created() {
      // 잘못된 url 접근시 이동
      if(!this.$route.query.no || this.$route.query.no == '' || !this.checkNumberFormat(this.$route.query.no)){
        this.$router.push("/");
        return;
      }

      // store.dispatch(
      //   "getRequestitems1",
      //   "/request/res/" + this.portfolio.nickname + "/1"
      // );

      this.uid = this.$route.query.no;
      this.request_info.request_nickname = this.$session.get('nickname');
      let URL = '/portfolio';
      //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
      //포트폴리오 정보
      this.getPortfolio(URL);

      //포트폴리오 리뷰
      this.getReviewInfo(URL);

      // 포트폴리오 영상
      this.getVideoInfo(URL);

      // 포트폴리오 스케쥴
      this.getScheduleInfo(URL);

      // 현재 진행중인 작업 스케줄
      // this.getInprogressDate();

      // 포트폴리오 태그
      this.getTagInfo(URL);

      // 북마크 정보 가져와서 북마크 한 인원수 보여주기
      this.getBookmarkCount();
    },
    // computed: {
    //   ...mapGetters(["requestitems1"]),
    // },
    methods: {
      checkRequestForm(){
                let valid = true;
                let message = '';
                // 필수 입력이 되었는지 확인
                valid && !this.request_info.request_nickname && ((valid = false), (message = '의뢰인의 닉네임이 없습니다.'))
                valid && !this.request_info.response_nickname && ((valid = false), (message = '편집자의 닉네임이 없습니다.'))
                valid && !this.dates.range && ((valid = false), (message = '기간을 입력해주세요'))
                valid && !this.request_info.video_origin_length && ((valid = false), (message = '원본 영상 길이를 입력해주세요'))
                valid && !this.request_info.video_origin_length && ((valid = false), (message = '원본 영상 길이를 입력해주세요'))

                //숫자인지 체크
                valid && !this.checkNumberFormat(this.request_info.video_origin_length) && ((valid = false), (message = '숫자만 입력해주세요'))
                valid && !this.checkNumberFormat(this.request_info.video_result_length) && ((valid = false), (message = '숫자만 입력해주세요'))
                valid && !this.request_info.video_type && ((valid = false), (message = '원하는 영상 종류를 입력해주세요'))
                
                if (valid) this.requestForm();
                else {
                    alertify.error(message, 3);
                    return;
                }
        },
        checkNumberFormat(number){
                let exptext = /^[0-9]/;
                //숫자로만 이루어져 있는지 확인        
                if (exptext.test(number) == false) {
                    return false;
                }
                return true;
        },
      requestForm(){
        // 요청 양식에 따라 가공
        // this.dates.range (YYYY-MM-DD || YYYY-MM-DD to YYYY-MM-DD)
        let dates = this.dates.range.split('to')
        //dates의 length가 1이라면 term=0
        //dates의 length가 2라면 term은 두 날의 차이이다.
        this.request_info.start_date = dates[0];
        if(dates.length == 1){
          this.request_info.end_date = dates[0];
        }
        else if(dates.length == 2){
          this.request_info.end_date = dates[1]
        }

        http
            .post('/request',this.request_info)
            .then(({data}) => {
                if (data == 'success') {
                  console.log("요청사항 완료")
                  this.initModalRequest();
                  alertify.notify('작업 요청 완료', 'success', 3)
                  return;
                } else {
                  console.log("요청사항 실패")
                  // fail 
                    return;
                }
            })
            .catch(error => {
                console.log(error);
                return;
            })
      },
      initModalRequest(){
        this.request_info={
          request_nickname: '',
          response_nickname: '',
          start_date: '',
          end_date: '',
          video_origin_length: '',
          video_result_length: '',
          video_type: '',
          request_description: '',
          tag_list: '',
          video_skill: '',
          video_style: '',
          done_flag: 0,
        }
        this.modal.show = false;
      },
      getInprogressDate(){
        http
        .get("/request/res/"+this.portfolio.nickname+"/1")
        .then(({data}) => {
          // console.log(data);
          this.events.dates = this.makeInprogressDateArray(data);
          console.log(this.events.dates);
          return;
        })
        .catch(error => {
          console.log(error);
          return;
        })
      },
      getScheduleInfo(URL){
        http
        .get(URL+'/schedule/'+this.uid)
        .then(({data}) => {
          if(data.data == 'success'){
            // scheduleType=0 기본
            let result = data.object.filter(schedule => schedule.scheduleType == 0);
            this.disableDates = this.makeScheduleArray(result);
            // console.log(result);
            return;
          } else {
            return;
          }
        })
        .catch(error => {
          console.log(error);
          return;
        })
      },
      getTagInfo(URL){
        http
            .get(URL+'/tag/'+this.uid)
            .then(({data}) => {
                //성공시 평균 계산 필요 추출 필요
                if (data.data == 'success') {
                  this.tags = [];
                  data.object.forEach(obj => {
                    this.tags.push(obj.tagName);
                  });
                  return;
                } else {
                  // fail 
                    return;
                }
            })
            .catch(error => {
                console.log(error);
                return;
            })
      },
      getReviewInfo(URL){
        http
            .get(URL+'/review/'+this.uid)
            .then(({data}) => {
                //성공시 평균 계산 필요 추출 필요
                if (data.data == 'success') {
                  this.reviews = data.object;
                  // console.log(data.object);
                  //평균계산
                  let videoAvg=0, kindnessAvg=0, finishAvg=0;
                  data.object.forEach(obj => {
                    videoAvg += obj.videoScore;
                    kindnessAvg += obj.kindnessScore;
                    finishAvg += obj.finishScore;
                  });
                  let length = data.object.length;
                  this.videoAvg = videoAvg/length;
                  this.kindnessAvg = kindnessAvg/length;
                  this.finishAvg = finishAvg/length;

            return;
          } else {
            // fail
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
            .get(URL+'/video/'+this.uid)
            .then(({data}) => {
                //성공시 video 추출 필요
                if (data.data == 'success') {
                  //main이 아닌것들만 추출
                  let result = data.object.filter(video => video.mainFlag == 0);
                  this.videos = this.makeVideosArray(result);
                  
                  result = data.object.filter(video => video.mainFlag == 1);
                  this.mainVideo = result[0].url;
                    return;
                } else {
                  // fail 
                    return;
                }
            })
            .catch(error => {
                console.log(error);
                return;
            })
      },
      getPortfolio(URL){
      //1은 session uid
        http
            .get(URL+'/portfolio/'+this.uid)
            .then(({data}) => {
                //성공시 video 추출 필요
                if (data.data == 'success') {
                  this.portfolio.nickname = data.object.nickname;
                  // console.log(data.object);
                  this.request_info.response_nickname = data.object.nickname;
                  this.portfolio.description = data.object.description;
                  this.portfolio.payMin = data.object.payMin;
                  this.portfolio.skill = data.object.skill;
                  this.getInprogressDate();
                  return;
                } else {
                  // fail 
                    return;
                }
            })
            .catch(error => {
                console.log(error);
                return;
            })
      },
      getBookmarkCount(){
        http
        .get('/bookmark/cnt/'+this.uid)
        .then(({data}) => {
          if(data.data == 'success'){
            this.portfolio.markCnt = data.object.length;
            data.object.forEach(obj => {
              if(obj.userInfoUid == this.$session.get('uid')){
                this.togleBookmark = true;
                return;
              } else {
                this.togleBookmark = false;
              }
            })
            return;
          } else {
            return;
          }
        })
        .catch(error => {
          console.log(error);
          return;
        })
      },
      addBookmark(){
        if(!this.togleBookmark){
          http
          .post('/bookmark', {
            uid: this.$session.get('uid'),
            muid: this.uid
          })
          .then(({data}) => {
            if(data.data == "success"){
              this.togleBookmark = true;
              this.getBookmarkCount();
              return;
            } else {
              return;
            }
          })
        } else {
          http
          .post('/bookmark/delete', {
            uid: this.$session.get('uid'),
            muid: this.uid
          })
          .then(({data}) => {
            if(data.data == "success"){
              this.togleBookmark = false;
              this.getBookmarkCount();
              return;
            } else {
              return;
            }
          })
        }
      },
      //성공했을 경우 각 객체 돌면서 비디오 추출 필요
      makeVideosArray(result){
        let obj;
        let res = [];
        result.forEach(element => {
          obj = new Object();
          obj.url = element.url;
          obj.previewImageSize = 'maxresdefault';
          res.push(obj);
        });
        return res;
      },
      makeScheduleArray(result){
        let res = [];
        result.forEach(element => {
          res.push(getFormatDate(element.startDate));
        })
        return res;
      },
      makeInprogressDateArray(result){
        let obj;
        let dates = [];
        result.forEach(element => {
          obj = new Object();
          obj.start = element.start_date;
          obj.end = element.end_date;
          dates.push(obj);
        })
        return dates;
      },
      goToday() {
        this.$refs.calendar.goToday();
      },
      onOpenItem() {
        // Item opened
      },
      onCloseItem() {
        // Item closed
      },
  },
};
</script>
<style lang="scss">

</style>
