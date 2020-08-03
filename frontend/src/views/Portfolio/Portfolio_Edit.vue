<template>
  <div>
    <base-header class="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"
      style="min-height: 200px; background-size: cover; background-position: center top;">
      <span class="mask bg-gradient-success opacity-8"></span>
    </base-header>

    <div class="container-fluid mt--7">
      <div class="card">
        <form @submit.prevent>
          <h6 class="heading-small text-muted mb-4">User Portfolio Information</h6>
          <div class="pl-lg-4">
            <div class="row">
              <!-- <div class="col-lg-12">
                <base-input
                  alternative
                  label="사용기술"
                  placeholder="에팩, 프리미어"
                  input-classes="form-control-alternative"
                  v-model="portfolio.skills"
                />
              </div> -->
              <div class="col-lg-12">
                <base-input
                  alternative
                  label="Pay/Wages"
                  placeholder="00,000"
                  input-classes="form-control-alternative"
                  v-model="portfolio.payMin"
                />
              </div>
              <div class="col-lg-12">
                <base-input
                  alternative
                  label="대표영상 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[0]"
                />
              </div>
              <div class="col-lg-12">
                <base-input
                  alternative
                  label="영상1 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[1]"
                />
              </div>
              <div class="col-lg-12">
                <base-input
                  alternative
                  label="영상2 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[2]"
                />
              </div>
              <div class="col-lg-12">
                <base-input
                  alternative
                  label="영상3 URL"
                  placeholder="https://"
                  input-classes="form-control-alternative"
                  v-model="portfolio.URLs[3]"
                />
              </div>
              <div class="col-lg-12">
                <!-- 아직 더 생각하고 작업해봐야할듯 -->
                <!-- <label for="schedule"> 근무 가능 요일을 골라주세요 </label> -->
                <!-- <div class="row"> -->
                  <!-- <base-checkbox class="mb-3" id="1,7" v-model="checkboxes.weekday">평일</base-checkbox>
                  <base-checkbox class="mb-3" id="2,3,4,5,6" v-model="checkboxes.weekend">주말</base-checkbox>
                  <base-checkbox class="mb-3" id="2" v-model="checkboxes.mon">월</base-checkbox>
                  <base-checkbox class="mb-3" id="3" v-model="checkboxes.tue">화</base-checkbox>
                  <base-checkbox class="mb-3" id="4" v-model="checkboxes.wed">수</base-checkbox>
                  <base-checkbox class="mb-3" id="5" v-model="checkboxes.thur">목</base-checkbox>
                  <base-checkbox class="mb-3" id="6" v-model="checkboxes.fri">금</base-checkbox>
                  <base-checkbox class="mb-3" id="7" v-model="checkboxes.sat">토</base-checkbox>
                  <base-checkbox class="mb-3" id="1" v-model="checkboxes.sun">일</base-checkbox>
                <span>{{ disableDates }} </span>
                <span>{{ checkboxes }} </span> -->
                <!-- </div> -->
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

              <div class="col-lg-12 tags-input">
                <vue-tags-input
                  v-model="tag"
                  :allow-edit-tags = "false"
                  :tags="tags"
                  @tags-changed="newTags => tags = newTags"
                />
              </div>
            </div>
          </div>
          <base-button
            type="default"
            class="mb-3 float-right"
            style="white-space: nowrap;"
            @click="updatePortfolio()">
            Update
          </base-button>
        </form>
      </div>
    </div>
  </div>
</template>
<script>
import VueTagsInput from '@johmun/vue-tags-input';
//axios 초기 설정파일
import http from "@/util/http-common";

  export default {
    name: 'user-portfolio-edit',
    components: {
      VueTagsInput,
    },
    data() {
      return {
        selectedValue: new Date(),
        portfolio: {
          skills: "",
          payMin: "",
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
      let URL = 'http://i3a509.p.ssafy.io:8080/portfolio'
      //포트폴리오 정보, 영상, 리뷰, 스케쥴, 태그 가져오기
      //포트폴리오 정보
      this.getPortfolio(URL);

      //포트폴리오 영상
      this.getVideoInfo(URL);

      // //포트폴리오 스케쥴
      // this.getScheduleInfo(URL);
      
      // //포트폴리오 태그
      this.getTagInfo(URL);
    },
    methods: {
      getTagInfo(URL){
        http
            .get(URL+'/tag/'+1)
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
      //1은 session uid
        http
            .get(URL+'/video/'+1)
            .then(({data}) => {
                //성공시 video 추출 필요
                if (data.data == 'success') {
                  this.portfolio.URLs = [];
                  data.object.forEach(obj => {
                    this.portfolio.URLs.push(obj.url);
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
      getPortfolio(URL){
      //1은 session uid
        http
            .get(URL+'/portfolio/'+1)
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
    },
  };
</script>
<style lang="scss">
</style>
