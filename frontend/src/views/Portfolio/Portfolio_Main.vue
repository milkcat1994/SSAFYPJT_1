<template>
  <div>
    <base-header class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;">
      <span class="mask bg-gradient-success opacity-8"></span>
      <!-- 에디터 본인 일 경우에만 활성화 되어야한다. -->
      <router-link v-if="uid == $session.get('uid') " :to="'/portfolio/edit?no='+this.uid">
        <base-button v-if="!haveTags && !haveVideo" type="info" class="btn btn-info float-right"> 포트폴리오 등록하기</base-button>
        <base-button v-if="haveTags || haveVideo" size="sm" type="info" class="btn btn-info float-right" icon="ni ni-settings">Edit profile</base-button>
      </router-link>
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-7 order-xl-2 mb-5 mb-xl-0">
          <div class="card card-profile shadow">
            <div class="card-body pt-0 pt-md-4" style="height: 303px;">
              <div class="row">
                <div class="col">
                  <div class="text">
                    <h1>
                      {{portfolio.nickname}}
                      <base-button v-if="uid != $session.get('uid')" outline type="danger" icon="ni ni-favourite-28" @click="addBookmark()">
                      {{portfolio.markCnt}}
                      </base-button>
                      <base-button v-if="!isLogin" size="sm" type="default float-right" @click="alertModal.show=true"> 작업 요청하기 </base-button>
                      <base-button v-if="$session.get('auth') == 'noneditor'" size="sm" type="default float-right" @click="modal.show=true"> 작업 요청하기 </base-button>
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
              <hr>
              <div class="row" style="margin-top: 30px">
                <!-- <div class="col"> -->
                  <calendar
                    :eventCategories="eventCategories"
                    :events="events"
                    :offDays="offdays"
                    ref="calendar"
                    style="width: 100%; height: 100%"
                  />
                  <!-- {{events}} -->
                <!-- </div> -->
              </div>
              <hr>
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
                <div class="col">
                  <h3>한줄평</h3>
                  <badger-accordion>
                    <badger-accordion-item v-for="(review, index) in reviewsMain" :key="index">
                      <template slot="header" >{{review.comment}}</template>    
                      <template slot="content" >
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
                  <base-button size="sm" type="float-right" @click="reviewModal.show = true" icon="ni ni-fat-add">더보기</base-button>
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
          <h3>편집 목적</h3>
        </div>
        <div class="row">
          <base-checkbox class="mb-3" name="video_type" value="personal" v-model="video_type.personal"> 개인용 </base-checkbox>
          <base-checkbox class="mb-3" name="video_type" value="commercial" v-model="video_type.commercial">상업용</base-checkbox>
        </div>
        <div class="row">
          <h3>영상 종류</h3>
        </div>
        <div class="row">
          <base-checkbox class="mb-3" name="video_style" value="kids" v-model="video_style.personal"> 
            키즈
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="game" v-model="video_style.game"> 
            게임
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="music" v-model="video_style.music"> 
            음악/댄스
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="food" v-model="video_style.food"> 
            푸드/쿠킹
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="vlog" v-model="video_style.vlog"> 
            V-log
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="movie" v-model="video_style.movie"> 
            영화/애니메이션
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="animal" v-model="video_style.animal"> 
            동물
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="beauty" v-model="video_style.beauty"> 
            뷰티/패션
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="sports" v-model="video_style.sports"> 
            스포츠
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_style" value="etc" v-model="video_style.etc"> 
            기타
          </base-checkbox>
          <base-input
            alternative
            placeholder="기타 선택 시 영상 종류를 입력해주세요"
            input-classes="form-control-alternative"
            v-model="request_info.video_style"
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
          <h3>원하는 영상 편집 기술</h3>
        </div>
        <div class="row">
          <base-checkbox class="mb-3" name="video_skill" value="color" v-model="video_skill.color">
            색, 밝기 조정
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="audio" v-model="video_skill.audio">
            음향(오디오, 음악)
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="motion" v-model="video_skill.motion">
            모션그래픽
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="caption" v-model="video_skill.caption">
            자막
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="intro" v-model="video_skill.intro">
            인트로
          </base-checkbox>
          <base-checkbox class="mb-3" name="video_skill" value="outro" v-model="video_skill.outro">
            아웃트로
          </base-checkbox>
        </div>
          <h3>기타 요구사항</h3>
          <textarea class="form-control form-control-alternative" id="description" v-model="request_info.request_description" rows="3" placeholder="기타 요구사항을 작성해주세요."></textarea>
        </div>
     <template slot="footer">
         <base-button type="secondary" @click="modal.show = false">Close</base-button>
         <base-button type="primary" @click="checkRequestForm()">요청하기</base-button>
     </template>
    </modal>

    <modal :show.sync="reviewModal.show">
      <h1 slot="header" class="modal-title" id="modal-title-default">한줄평</h1>
        <badger-accordion>
          <badger-accordion-item v-for="(review, index) in reviews" :key="index">
            <template slot="header" >{{review.comment}}</template>    
              <template slot="content" >
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
        <template slot="footer">
          <base-button type="link" class="ml-auto" @click="reviewModal.show = false">
            Close
          </base-button>
        </template>
    </modal>

    <modal :show.sync="alertModal.show"
      gradient="danger"
      modal-classes="modal-danger modal-dialog-centered">
      <div class="py-3 text-center">
        <i class="ni ni-bell-55 ni-3x"></i>
        <h4 class="heading mt-4">로그인 후 이용이 가능한 서비스입니다.</h4>
      </div>

      <template slot="footer">
        <router-link to="/login">
          <base-button type="white float-right" @click="alertModal.show = false">로그인</base-button>
        </router-link>
        <hr>
        아직 회원이 아니십니까?
        <router-link to="/user">
          <base-button type="white float-right" @click="alertModal.show = false">회원가입</base-button>
        </router-link>
        <base-button type="link"
          text-color="white"
          class="ml-auto"
          @click="alertModal.show = false">
          Close
        </base-button>
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

import { Calendar } from "vue-sweet-calendar";
import "vue-sweet-calendar/dist/SweetCalendar.css";

import alertify from "alertifyjs"

// axios 초기 설정파일
import http from "@/util/http-common";

// import { mapGetters } from "vuex";
// import store from "@/store/store.js";
// 날짜 계산 파일
// import { getFormatDate } from "@/util/day-common";
// import { getEndDate, getFormatDate } from "@/util/day-common";

  export default {
    name: 'user-portfolio',
    components: {
      LazyYoutubeVideo,
      Rate,
      BadgerAccordion,
      BadgerAccordionItem,
      flatPicker,
      InputTag,
      Calendar
    },
    data() {
      return {
        uid:'',
        isLogin: false,
        userType: '',
        haveVideo: true,
        haveTags: true,

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
        reviewsMain: [],

        //태그들
        tags:[],
        tag: '',

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
          }
        ],
        events: [],
        offdays: [],
        
        mainVideo: 'https://www.youtube.com/embed/',
        videos: [],

        modal: {
          show: false
        },
        alertModal: {
          show: false
        },
        reviewModal: {
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
        video_skill: {
          color: false,
          audio: false,
          motion: false,
          caption: false,
          intro: false,
          outro: false
        },
        video_type: {
          personal: false,
          commercial: false
        },
        video_style: {
          kids: false,
          game: false,
          music: false,
          food: false,
          vlog: false,
          movie: false,
          animal: false,
          beauty: false,
          sports: false,
          etc: false
        }
      }
    },
    created() {
      // 잘못된 url 접근시 이동
      if(!this.$route.query.no || this.$route.query.no == '' || !this.checkNumberFormat(this.$route.query.no)){
        this.$router.push("/");
        return;
      }

      if(this.$session.exists()){
        this.isLogin = true;
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

      // 북마크 정보 가져와서 북마크 한 인원수 보여주기
      this.getBookmarkCount();

      // console.log(this.events);
    },
    // computed: {
    //   ...mapGetters(["requests"])
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
                // valid && !this.request_info.video_type && ((valid = false), (message = '원하는 영상 종류를 입력해주세요'))
                
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

        if(this.video_type.personal){
          this.request_info.video_type = "개인용";
        }
        if(this.video_type.commercial){
          this.request_info.video_type = "상업용";
        }

        if(this.video_style.kids){
          this.request_info.video_style = "키즈";
        }
        if(this.video_style.game){
          this.request_info.video_style = "게임";
        }
        if(this.video_style.music){
          this.request_info.video_style = "음악/댄스";
        }
        if(this.video_style.food){
          this.request_info.video_style = "푸드/쿠킹";
        }
        if(this.video_style.vlog){
          this.request_info.video_style = "V-log";
        }
        if(this.video_style.movie){
          this.request_info.video_style = "영화/애니메이션";
        }
        if(this.video_style.animal){
          this.request_info.video_style = "동물";
        }
        if(this.video_style.beauty){
          this.request_info.video_style = "뷰티/패션";
        }
        if(this.video_style.sports){
          this.request_info.video_style = "스포츠";
        }

        if(this.video_skill.color){
          this.request_info.video_skill += ",색 보정";
        }
        if(this.video_skill.audio){
          this.request_info.video_skill += ",음향";
        }
        if(this.video_skill.motion){
          this.request_info.video_skill += ",모션그래픽";
        }
        if(this.video_skill.caption){
          this.request_info.video_skill += ",자막";
        }
        if(this.video_skill.intro){
          this.request_info.video_skill += ",인트로";
        }
        if(this.video_skill.outro){
          this.request_info.video_skill += ",아웃트로";
        }

        http
            .post('/request',this.request_info)
            .then(({data}) => {
                if (data == 'success') {
                  // console.log("요청사항 완료")
                  this.initModalRequest();
                  alertify.notify('작업 요청 완료', 'success', 3)
                  return;
                } else {
                  // console.log("요청사항 실패")
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
          if(data.length > 0)
            this.makeInprogressDateArray(data);
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
            // let result = data.object.filter(schedule => schedule.scheduleType == 0);
            this.makeScheduleArray(data.object);
            // console.log(data.object);
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
                  this.haveTags = true;
                  this.tags = [];
                  data.object.forEach(obj => {
                    this.tags.push(obj.tagName);
                  });
                  return;
                } else {
                  this.haveTags = false;
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

            this.reviewsMain = this.reviews.reverse();
            if(this.reviews.length > 5){
              this.reviewsMain = this.reviews.slice(0,5);
            }
            
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
                  this.haveVideo = true;
                  //main이 아닌것들만 추출
                  let result = data.object.filter(video => video.mainFlag == 0);
                  this.videos = this.makeVideosArray(result);
                  
                  result = data.object.filter(video => video.mainFlag == 1);
                  this.mainVideo = result[0].url;
                  return;
                } else {
                  this.haveVideo = false;
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
        let date;
        result.forEach(element => {
          date = new Object();
          date.start = new Date(element.startDate);
          date.end = new Date(element.endDate);
          date.categoryId = element.scheduleType;
          date.repeat = "never";
          this.events.push(date);
        })
      },
      makeInprogressDateArray(result){
        let obj;
        result.forEach(element => {
          obj = new Object();
          obj.start = new Date(element.start_date);
          obj.end = new Date(element.end_date);
          obj.categoryId = 3;
          obj.repeat = "never";
          this.events.push(obj);
        })
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
