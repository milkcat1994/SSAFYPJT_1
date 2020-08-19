<template>
  <div>
    <base-header class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;">
      <span class="mask bg-gradient-success opacity-8"></span>
      <!-- 최초등록 시에만 활성화되는 버튼 -->
      <base-button v-if="isFirstHeadVideo && isFirstVideos && !haveSchedule" type="info" class="btn btn-info float-right"
        @click="uploadOnce()">
        등록하기
      </base-button>

      <router-link :to="'/portfolio?no='+this.uid" style="margin-right: 1%;">
        <base-button type="info" class="btn btn-info float-right" style="margin-right: 1%;">
          취소하기
        </base-button>
      </router-link>
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
                      <base-button v-if="!isFirstHeadVideo || !isFirstVideos || haveSchedule" size="sm" type="default float-right" @click="updatePortfolio()"> 수정하기 </base-button>
                    </h1>
                    <base-input
                      alternative
                      label="자신을 어필해주세요."
                      placeholder="깔끔한 편집에 자신있습니다!"
                      input-classes="form-control-alternative"
                      v-model="portfolio.description"
                    />
                    <h4>편집할 수 있는 기술을 선택해주세요</h4>
                    <base-checkbox class="mb-3" name="video_skill" value="color" v-model="checkboxes.color">
                      색, 밝기 조정
                    </base-checkbox>
                    <base-checkbox class="mb-3" name="video_skill" value="audio" v-model="checkboxes.audio">
                      음향(오디오, 음악)
                    </base-checkbox>
                    <base-checkbox class="mb-3" name="video_skill" value="motion" v-model="checkboxes.motion">
                      모션그래픽
                    </base-checkbox>
                    <base-checkbox class="mb-3" name="video_skill" value="caption" v-model="checkboxes.caption">
                      자막
                    </base-checkbox>
                    <base-checkbox class="mb-3" name="video_skill" value="intro" v-model="checkboxes.intro">
                      인트로
                    </base-checkbox>
                    <base-checkbox class="mb-3" name="video_skill" value="outro" v-model="checkboxes.outro">
                      아웃트로
                    </base-checkbox>
                    <base-input
                      alternative
                      label="분당 페이가격을 적어주세요."
                      placeholder="1,000원"
                      input-classes="form-control-alternative"
                      v-model="portfolio.payMin"
                    />
                    <div class="tags-input">
                      <base-button v-if="!isFirstHeadVideo || !isFirstVideos || haveSchedule" size="sm" type="default float-right" @click="updateTags()"> 수정하기 </base-button>
                      <input-tag placeholder="Add Tag" v-model="tags" :limit="20"></input-tag>
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
              v-model="portfolio.HeadURL[0].url"
            />
            <base-button v-if="!isFirstHeadVideo || !isFirstVideos || haveSchedule" size="sm" type="default float-right" @click="uploadHeadVideo()"> 수정하기 </base-button>
          </card>
        </div>
      </div>
    </div>

    <div class="container-fluid" style="margin-top: 20px;  margin-bottom: 50px;">
      <div class="row">
        <div class="col">
          <div class="card shadow">
            <div class="card-body">
              <div class="col" style="margin-bottom:30px;">
              <base-button v-if="!isFirstHeadVideo || !isFirstVideos || haveSchedule" size="sm" type="default float-right" @click="uploadVideos()"> 
                수정하기
              </base-button>
              </div>
              <div class="row" v-for="(video, index) in portfolio.URLs" :key="index">
                <div class="col-md-11">
                  <!-- {{index}} -->
                  <base-input
                  alternative
                  label="영상 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="video.url"
                  />
                </div>
                <div class="col-md-1">
                  <base-button style="margin-top:32px;" type="float-center" icon="fa fa-minus-circle" @click="deleteURL(index)"></base-button>
                </div>
              </div>
              <div class="addButton">
                <base-button type="float-center" icon="fa fa-plus-circle" @click="addURL()"></base-button>
              </div>

              <div class="col">
                <base-button v-if="!isFirstHeadVideo || !isFirstVideos || haveSchedule" size="sm" type="default float-right" @click="updateSchedule()"> 수정하기 </base-button>
                <h4> 근무가 불가능한 날짜를 골라주세요. </h4>
                  <!-- <div class="row"> -->
                    <!-- <base-checkbox class="mb-3" v-model="offDays.weekday">평일</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.weekend">주말</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.mon">월</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.tue">화</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.wed">수</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.thur">목</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.fri">금</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.sat">토</base-checkbox>
                    <base-checkbox class="mb-3" v-model="offDays.sun">일</base-checkbox> -->
                  <!-- </div> -->
                <vc-date-picker
                  mode='multiple'
                  v-model='disableDates'>
                </vc-date-picker>
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
      InputTag,
      // alertify
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
          HeadURL: [{url: "", mainFlag: 1}],
          URLs: [{url: "", mainFlag: 0},
                  {url: "", mainFlag: 0},
                  {url: "", mainFlag: 0}],
          schedule: "",
          description: ""
        },
        //태그들
        tags:[],
        tag: '',
        //checkedDays - 요일들
        disableDates: [],
        checkboxes: {
          color: false,
          audio: false,
          motion: false,
          caption: false,
          intro: false,
          outro: false
        },
        offDays: {
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
                  // console.log("URL 없음");
                  this.isFirstVideos = true;
                } else {
                  this.isFirstVideos = false;
                }

                result = data.object.filter(video => video.mainFlag == 1);
                this.portfolio.HeadURL = this.makeVideosArray(result);

                if(this.portfolio.HeadURL.length == 0){
                  // console.log("HEAD URL 없음");
                  this.isFirstHeadVideo = true;
                } else {
                  this.isFirstHeadVideo = false;
                }
                return;
              } else {
                this.isFirstVideos = true;
                this.isFirstHeadVideo = true;
                // console.log("URL전부 없음");
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
            let result = data.object.filter(schedule => schedule.scheduleType == 4);
            this.disableDates = this.makeScheduleArray(result);
            return;
          } else {
            // console.log("스케줄 없음");
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
                  let skills = data.object.skill.split(',');
                  skills.forEach(skill => {
                    if(skill === "색/밝기 조정")
                      this.checkboxes.color = true;
                    else if(skill === "음향")
                      this.checkboxes.audio = true;
                    else if(skill === "모션그래픽")
                      this.checkboxes.motion = true;
                    else if(skill === "자막")
                      this.checkboxes.caption = true;
                    else if(skill === "인트로")
                      this.checkboxes.intro = true;
                    else if(skill === "아웃트로")
                      this.checkboxes.outro = true;
                  })
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
        let urlList = [];
        this.portfolio.HeadURL.forEach(element => {
          urlList.push(element.url);
        })
        // 대표영상_최초등록일 경우
        if(this.isFirstHeadVideo){
          http
          .post('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: urlList,
            mainFlag: 1
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("저장이 완료되었습니다.","success",3);
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
            url: urlList,
            mainFlag: 1
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        }
      },
      uploadVideos(){
        let urlList = [];
        this.portfolio.URLs.forEach(element => {
          urlList.push(element.url);
        })
        // 그외영상_최초등록일 경우
        if(this.isFirstVideos){
          http
          .post('/portfolio/video/'+this.uid, {
            portfolioUid: this.uid,
            url: urlList,
            mainFlag: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("저장이 완료되었습니다.","success",3);
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
            url: urlList,
            mainFlag: 0
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("수정이 완료되었습니다.","success",3);
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
        // 스케줄 타입 4: 기본, 1: 개인일정(휴가 등등), 2: 다른 작업중
        if(!this.haveSchedule){
          http
          .post('/portfolio/schedule/'+this.uid, {
            portfolioUid: this.uid,
            startDate: this.disableDates,
            endDate: this.disableDates,
            scheduleType: 4
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("저장이 완료되었습니다.","success",3);
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
            scheduleType: 4
          })
          .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
          })
        }
      },
      updatePortfolio(){
        // Object.keys(this.checkboxes).forEach(key => {
        //   if(this.checkboxes[key])
        //     // console.log(Object.values(this.checkboxes[key]));
        //     this.portfolio.skills += this.checkboxes[key] + ",";
        // })
        if(this.checkboxes.color){
          this.portfolio.skills += ",color";
        }
        if(this.checkboxes.audio){
          this.portfolio.skills += ",audio";
        }
        if(this.checkboxes.motion){
          this.portfolio.skills += ",motion";
        }
        if(this.checkboxes.caption){
          this.portfolio.skills += ",caption";
        }
        if(this.checkboxes.intro){
          this.portfolio.skills += ",intro";
        }
        if(this.checkboxes.outro){
          this.portfolio.skills += ",outro";
        }
        // console.log(this.portfolio.skills);
        http
        .put('/portfolio/portfolio/'+this.uid, {
          uid: this.uid,
          skill: this.portfolio.skills,
          payMin: this.portfolio.payMin,
          description: this.portfolio.description
        })
        .then(({ data }) => {
            if(data.data == "success"){
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("수정이 완료되었습니다.","success",3);
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
              this.$router.push("/portfolio?no="+this.uid);
              // alertify.notfiy("수정이 완료되었습니다.","success",3);
              return;
            } else {
              alertify.error("오류가 발생하였습니다.",3);
              return;
            }
        })
      },
      uploadOnce(){
        this.uploadHeadVideo();
        this.uploadVideos();
        this.updateSchedule();
        this.updatePortfolio();
        this.updateTags();
        this.$router.push("/portfolio?no="+this.uid);
      },
      makeVideosArray(result){
        let res = [];
        let obj;
        result.forEach(element => {
          obj = new Object;
          obj.url = element.url;
          obj.mainFlag = element.mainFlag;
          res.push(obj);
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
      addURL(){
        this.portfolio.URLs.push({
          url: '',
          mainFlag: 0
        })
      },
      deleteURL(index){
        // console.log(index);
        // this.portfolio.URLs[index].url = "";
        // delete this.portfolio.URLs[index];
        this.portfolio.URLs.splice(index,1);
      }
    },
  };
</script>
<style lang="scss">
.addButton {
  text-align: center;
}
</style>
