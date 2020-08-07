<template>
  <div>
    <base-header class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;">
      <span class="mask bg-gradient-success opacity-8"></span>
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-7 order-xl-2 mb-5 mb-xl-0">
          <div class="card card-profile shadow">
            <div class="card-body pt-0 pt-md-4">
              <div class="row">
                <div class="col">
                  <div class="text">
                    <h1>
                      {{portfolio.nickname}}
                      <base-button size="sm" type="default float-right" @click="updatePortfolio()"> 수정하기 </base-button>
                    </h1>
                    <base-input
                      alternative
                      label="자신을 어필해주세요."
                      placeholder="깔끔한 편집에 자신있습니다!"
                      input-classes="form-control-alternative"
                      v-model="portfolio.description"
                    />
                    <base-input
                      alternative
                      label="자신의 강점을 적어주세요."
                      placeholder="인트로 전문 제작"
                      input-classes="form-control-alternative"
                      v-model="portfolio.skills"
                    />
                    <base-input
                      alternative
                      label="분당 페이가격을 적어주세요."
                      placeholder="1,000원"
                      input-classes="form-control-alternative"
                      v-model="portfolio.payMin"
                    />
                    <div class="tags-input">
                      <base-button size="sm" type="default float-right" @click="updateTags()"> 수정하기 </base-button>
                      <input-tag placeholder="Add Tag" v-model="tags" :limit="limit"></input-tag>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-xl-5 order-xl-1">
          <card shadow type="secondary">
            <base-input
              alternative
              label="대표영상 URL"
              placeholder="https://"
              input-classes="form-control-alternative"
              v-model="portfolio.HeadURL[0]"
            />
            <base-button size="sm" type="default float-right" @click="uploadHeadVideo()"> 수정하기 </base-button>
          </card>
        </div>
      </div>
    </div>

    <div class="container-fluid" style="margin-top: 20px">
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-body">
              <base-button size="sm" type="default float-right" @click="uploadVideos()"> 수정하기 </base-button>
              <div class="row">
                <div class="col">
                <base-input
                  alternative
                  label="영상1 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[0]"
                />
                <base-input
                  alternative
                  label="영상2 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[1]"
                />
                <base-input
                  alternative
                  label="영상3 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[2]"
                />
                </div>
              </div>

              <div class="row" style="margin-top: 30px">
                <div class="col">
                  <base-button size="sm" type="default float-right" @click="updateSchedule()"> 수정하기 </base-button>
                  <label for="schedule"> 근무가 불가능한 날짜를 골라주세요. </label>
                  <vc-date-picker
                    mode='multiple'
                    v-model='disableDates'>
                  </vc-date-picker>
                  <!-- <vc-calendar
                    title-position="left"
                    v-model='disableDates'
                    :disabled-dates='disableDates'
                  />
                  {{disableDates}} -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import InputTag from 'vue-input-tag';
//axios 초기 설정파일
import http from "@/util/http-common";
import alertify from "alertifyjs";
import { getFormatDate } from "@/util/day-common";

  export default {
    name: 'portfolio_edit',
    components: {
      InputTag
    },
    data() {
      return {
        uid:'',
        haveSchedule: false,
        isFirstHeadVideo: false,
        isFirstVideos: false,
        portfolio: {
          nickname: '',
          skills: "",
          payMin: "",
          HeadURL: [],
          URLs: [],
          schedule: "",
          description: ""
        },
        //태그들
        tags:[],
        tag: '',
        //checkedDays - 요일들
        disableDates: [],
        checkboxes: {
          weekday: false,
          weekend: false,
          mon: false,
          tue: false,
          wed: false,
          thur: false,
          fri: false,
          sat: false,
          sun: false
        }
      }
    },
    created() {
      this.uid = this.$route.query.no;
      this.portfolio.nickname = this.$session.get('nickname');
      let URL = '/portfolio';

      //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
      //포트폴리오 정보
      this.getPortfolio(URL);

      //포트폴리오 영상
      this.getVideoInfo(URL);

      // //포트폴리오 스케쥴
      this.getScheduleInfo(URL);
      
      // //포트폴리오 태그
      this.getTagInfo(URL);
    },
    methods: {
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
      getVideoInfo(URL){
        http
            .get(URL+'/video/'+this.uid)
            .then(({data}) => {
              if (data.data == 'success') {
                let result = data.object.filter(video => video.mainFlag == 0);
                this.portfolio.URLs = this.makeVideosArray(result);
                if(this.portfolio.URLs.length == 0){
                  this.isFirstVideos = true;
                } else {
                  this.isFirstVideos = false;
                }

                result = data.object.filter(video => video.mainFlag == 1);
                this.portfolio.HeadURL = this.makeVideosArray(result);

                if(this.portfolio.HeadURL.length == 0){
                  this.isFirstHeadVideo = true;
                } else {
                  this.isFirstHeadVideo = false;
                }
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
      getScheduleInfo(URL){
        http
        .get(URL+'/schedule/'+this.uid)
        .then(({data}) => {
          if(data.data == 'success'){
            this.haveSchedule = true;
            let result = data.object.filter(schedule => schedule.scheduleType == 0);
            this.disableDates = this.makeScheduleArray(result);
            console.log(this.disableDates);
            return;
          } else {
            this.haveSchedule = false;
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
                if (data.data == 'success') {
                  this.portfolio.description = data.object.description;
                  this.portfolio.payMin = data.object.payMin;
                  this.portfolio.skills = data.object.skill;
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
      uploadHeadVideo(){
        // 대표영상_최초등록일 경우
        if(this.isFirstHeadVideo){
          http
          .post('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: this.portfolio.HeadURL,
            mainFlag: 1
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("저장이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        } else {
          http
          .put('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: this.portfolio.HeadURL,
            mainFlag: 1
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        }
      },
      uploadVideos(){
        // 그외영상_최초등록일 경우
        if(this.isFirstVideos){
          http
          .post('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: this.portfolio.URLs,
            mainFlag: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("저장이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        } else {
          http
          .put('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: this.portfolio.URLs,
            mainFlag: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        }
      },
      updateSchedule(){
        this.disableDates = this.disableDates.map(x => getFormatDate(x));
        // 스케줄이 없는 경우 최초등록
        // 스케줄 타입 0: 기본, 1: 개인일정(휴가 등등), 2: 다른 작업중
        if(!this.haveSchedule){
          http
          .post('/portfolio/schedule/'+this.uid, {
            portfolioUid: this.uid,
            startDate: this.disableDates,
            endDate: this.disableDates,
            scheduleType: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("저장이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        } else { // 스케줄이 있는 경우 수정하기
          http
          .put('/portfolio/schedule/'+this.uid, {
            portfolioUid: this.uid,
            startDate: this.disableDates,
            endDate: this.disableDates,
            scheduleType: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        }
      },
      updatePortfolio(){
        http
        .put('/portfolio/portfolio/'+this.uid, {
          uid: this.uid,
          skill: this.portfolio.skills,
          payMin: this.portfolio.payMin,
          description: this.portfolio.description
        })
        .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
        })
      },
      updateTags(){
        http
        .put('/portfolio/tag/'+this.uid, {
          portfolioUid: this.uid,
          tagName: this.tags
        })
        .then(({ data }) => {
            if(data.data == "success"){
              alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
        })
      },
      makeVideosArray(result){
        let res = [];
        result.forEach(element => {
          res.push(element.url);
        });
        return res;
      },
      makeScheduleArray(result){
        let res = [];
        result.forEach(element => {
          // getFormatDate(element.startDate);
          res.push(element.startDate);
        })
        return res;
      },
    },
  };
</script>
<style lang="scss">
</style>
