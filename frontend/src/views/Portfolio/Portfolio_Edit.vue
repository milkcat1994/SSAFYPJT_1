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
            <div class="card-body pt-0 pt-md-4" style="height: 295px;">
              <div class="row">
                <div class="col">
                  <div class="text">
                    <h1>
                      {{portfolio.nickname}}
                      <!-- 에디터 본인 일 경우에만 비활성화 되어야한다. -->
                      <base-button size="sm" type="default float-right"> 수정하기 </base-button>
                    </h1>
                    <base-input
                      alternative
                      label="자신을 어필해주세요."
                      placeholder="깔끔한 편집에 자신있습니다!"
                      input-classes="form-control-alternative"
                      v-model="portfolio.description"
                    />
                    <h3>
                      {{portfolio.description}}
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
            <!-- <h3 class="mb-0" style="text-align:center">대표영상</h3> -->
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
            <!-- <div class="card-header bg-transparent">
              <h3 class="mb-0">그 외 영상들</h3>
            </div> -->

            <div class="card-body">
              <base-button size="sm" type="default float-right" @click="uploadVideos()"> 수정하기 </base-button>
              <div class="row">
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

              <div class="row" style="margin-top: 30px">
                <div class="col-xl-4 col-lg-6">
                  <base-button size="sm" type="default float-right"> 수정하기 </base-button>
                  <label for="schedule"> 근무가 불가능한 날짜를 골라주세요. </label>
                  <vc-date-picker
                    mode='multiple'
                    v-model='selectedValue'>
                  </vc-date-picker>
                  <vc-calendar
                    title-position="left"
                    v-model='selectedValue'
                    :disabled-dates='selectedValue'
                  />

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
import VueTagsInput from '@johmun/vue-tags-input';
//axios 초기 설정파일
import http from "@/util/http-common";
import alertify from "alertifyjs";

  export default {
    name: 'portfolio_edit',
    components: {
      VueTagsInput,
    },
    data() {
      return {
        uid:'',
        selectedValue: new Date(),
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
      // console.log(this.uid);
      //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
      //포트폴리오 정보
      this.getPortfolio(URL);

      //포트폴리오 영상
      this.getVideoInfo(URL);

      // //포트폴리오 스케쥴
      // this.getScheduleInfo(URL);
      
      // //포트폴리오 태그
      // this.getTagInfo(URL);
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
                  console.log("그외 동영상 없음");
                } else {
                  this.isFirstVideos = false;
                  console.log("그외 동영상 있음");
                  // console.log(this.portfolio.URLs.length);
                }

                result = data.object.filter(video => video.mainFlag == 1);
                this.portfolio.HeadURL = this.makeVideosArray(result);
                // console.log(this.portfolio.HeadURL.length);
                if(this.portfolio.HeadURL.length == 0){
                  this.isFirstHeadVideo = true;
                  // console.log("대표영상 없음");
                } else {
                  this.isFirstHeadVideo = false;
                }
                return;
              } else {
                // this.isFirstVideo = true;
                // console.log("URL 없음" + this.isFirstVideo);
                return;
              }
            })
            .catch(error => {
              // this.isFirstVideo = true;
              // console.log("URL 없음");
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
                  console.log(data.object);
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
            } else {
              alertify.error("오류가 발생하였습니다.",3);
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
            } else {
              alertify.error("오류가 발생하였습니다.",3);
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
            } else {
              alertify.error("오류가 발생하였습니다.",3);
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
            } else {
              alertify.error("오류가 발생하였습니다.",3);
            }
          })
        }
      },
      makeVideosArray(result){
        let res = [];
        result.forEach(element => {
          res.push(element.url);
        });
        return res;
      },
    },
  };
</script>
<style lang="scss">
</style>
