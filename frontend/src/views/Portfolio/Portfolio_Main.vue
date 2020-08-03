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
                      {{nickname}}
                      <!-- 에디터 본인 일 경우에만 비활성화 되어야한다. -->
                      <base-button size="sm" type="default" @click="modal.show=true"> 작업 요청하기 </base-button>
                    </h1>
                    <!-- 스크롤 및 높이 지정 필요 -->
                    <h3>
                      Skills: 배경음악, 로고삽입, 색 보정, 인트로 제작, 아웃트로 제작
                    </h3>
                    <h3>
                      Pay/Wages: 00,000원
                    </h3>
                    <h3>
                      {{description}}
                    </h3>
                    <div class="tags-input">
                      <vue-tags-input
                        v-model="tag"
                        :allow-edit-tags = "false"
                        :tags="tags"
                        @tags-changed="newTags => tags = newTags"
                      />
                    </div>
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
                  <calendar
                    :eventCategories="eventCategories"
                    :events="events"
                    ref="calendar"
                  />
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
          <textarea class="form-control form-control-alternative" id="description" v-model="description" rows="3" placeholder="기타 요구사항을 작성해주세요."></textarea>
        </div>
     <template slot="footer">
         <base-button type="secondary" @click="modal.show = false">Close</base-button>
         <base-button type="primary" @click="checkRequestForm()">요청하기</base-button>
     </template>
   </modal>
  </div>
</template>
<script>
import LazyYoutubeVideo from "vue-lazy-youtube-video";
import { Rate } from "vue-rate";
import { Calendar } from "vue-sweet-calendar";
import { BadgerAccordion, BadgerAccordionItem } from "vue-badger-accordion";
import flatPicker from "vue-flatpickr-component";
import VueTagsInput from '@johmun/vue-tags-input';
import "flatpickr/dist/flatpickr.css";
import "vue-rate/dist/vue-rate.css";
import "vue-sweet-calendar/dist/SweetCalendar.css";
import alertify from "alertifyjs"

// axios 초기 설정파일
import http from "@/util/http-common";
// 날짜 계산 파일
import { getEndDate, getFormatDate } from "@/util/day-common";
  export default {
    name: 'user-portfolio',
    components: {
      LazyYoutubeVideo,
      Calendar,
      Rate,
      BadgerAccordion,
      BadgerAccordionItem,
      flatPicker,
      VueTagsInput
    },
    data() {
      return {
        uid:'',
        //닉네임, 소개
        nickname:'',
        description:'',

        // 각 평점
        videoAvg: 0,
        kindnessAvg: 0,
        finishAvg: 0,

        //리뷰들
        reviews: [],

        //태그들
        tags:[],
        tag: '',

        eventCategories: [
          {
            id: 1,
            title: 'Request',
            textColor: 'white',
            backgroundColor: 'Blue'
          },
          {
            id: 2,
            title: 'Personal',
            textColor: 'white',
            backgroundColor: 'red'
          }
        ],
        events: [
        ],
        mainVideo: 'https://www.youtube.com/embed/',
        dates: {
          range: ""
        },

        videos: []
        ,modal: {
          show: false
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
      
      // 포트폴리오 태그
      this.getTagInfo(URL);
    },
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
      getScheduleInfo(URL){
        http
            .get(URL+'/schedule/'+this.uid)
            .then(({data}) => {
                // private int scheduleNo;
                // private int portfolioUid;
                // private Date startDate;
                // private int term;
                // private String scheduleType;
                //성공시 평균 계산 필요 추출 필요
                if (data.data == 'success') {
                  let obj;
                  this.events = [];
                  console.log(data.object)
                  data.object.forEach(element => {
                    obj = new Object();
                    obj.title = element.scheduleNo;
                    obj.start = getFormatDate(element.startDate);
                    obj.end = getEndDate(element.startDate, element.term);
                    obj.repeat = 'never';
                    obj.categoryId = Number(element.scheduleType);
                  console.log(obj);
                    this.events.push(obj);
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
      // private int reviewNo;
      
      // reviewNo를 쓸 필요가 있을까?
      // private String nickname;
      // private Date createdDate;

      //1은 session uid
        http
            .get(URL+'/review/'+this.uid)
            .then(({data}) => {
                //성공시 평균 계산 필요 추출 필요
                if (data.data == 'success') {
                  this.reviews = data.object;
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
                  this.nickname = data.object.nickname;
                  console.log(data.object);
                  this.request_info.response_nickname = data.object.nickname;
                  this.description = data.object.description;
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
@import url(//fonts.googleapis.com/earlyaccess/nanumpenscript.css);

.tags-input-tag{
  font-family: 'Nanum Pen Script', cursive;
  font-size: 20px;
}

.mb-30 {
  margin-bottom:30px;
}
</style>
