<template>
  <div style="height:100%">
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
    <!-- <h2 class="text-center">알림창</h2> -->
    <br />
    <div class="container mb-4">
      <tabs fill class="flex-column flex-md-row">
        <card shadow>
          <tab-pane>
            <span slot="title">
              <div>요청된 작업</div>
            </span>
            <div role="tablist">
              <div v-for="(requestitem0, index) in requestitems0" :key="index + '_requestitems0'">
                <b-card no-body class="m-1">
                  <b-card-header header-tag="header" class="p-1" role="tab">
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem0.uid"
                      variant="info"
                      @click="getDetail(requestitem0.rid)"
                    >
                      {{ requestitem0.request_nickname }}님이 작업을
                      요청했습니다.
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem0.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover" style="float:left; width: 60%">
                          <tbody>
                            <tr>
                              <th>요청자</th>
                              <td>{{ requestitem0.request_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem0.video_type }}</td>
                            </tr>
                            <tr>
                              <th>원본 동영상 길이</th>
                              <td>{{ requestitem0.video_origin_length }}</td>
                            </tr>
                            <tr>
                              <th>원하는 동영상 길이</th>
                              <td>{{ requestitem0.video_result_length }}</td>
                            </tr>
                            <tr>
                              <th>동영상 스타일</th>
                              <td>
                                {{ requestitem0.video_style }}
                                <div style="color: blue; float: right">{{ requestitem.tag_list }}</div>
                              </td>
                            </tr>
                            <tr>
                              <th>진행 날짜</th>
                              <td>
                                {{ requestitem0.start_date }} ~
                                {{ requestitem0.end_date }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem0.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>

                        <!-- for calendar -->
                        <calendar
                          :eventCategories="eventCategories"
                          :events="events"
                          ref="calendar"
                          style="float:left; width: 40%; height: 100%"
                        />
                      </b-card-text>
                      <b-button
                        class="statusBtn"
                        style="background-color: #0099ff"
                        @click="acceptRequest(requestitem0.rid)"
                      >요청 수락</b-button>
                      <b-button
                        class="statusBtn"
                        style="background-color: #aaaaaa"
                        @click="denyRequest(requestitem0.rid)"
                      >요청 거절</b-button>
                    </b-card-body>
                  </b-collapse>
                </b-card>
              </div>
            </div>
          </tab-pane>

          <tab-pane title="Profile">
            <span slot="title">
              <div>진행중 작업</div>
            </span>
            <div role="tablist">
              <div v-for="(requestitem1, index) in requestitems1" :key="index + '_requestitems1'">
                <b-card no-body class="m-1">
                  <b-card-header header-tag="header" class="p-1" role="tab">
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem1.uid"
                      variant="info"
                      @click="getDetail(requestitem1.rid)"
                    >
                      {{ requestitem1.request_nickname }}님과의 작업이
                      진행중입니다.
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem1.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover" style="float:left; width: 60%">
                          <tbody>
                            <tr>
                              <th>요청자</th>
                              <td>{{ requestitem1.request_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem1.video_type }}</td>
                            </tr>
                            <tr>
                              <th>원본 동영상 길이</th>
                              <td>{{ requestitem1.video_origin_length }}</td>
                            </tr>
                            <tr>
                              <th>원하는 동영상 길이</th>
                              <td>{{ requestitem1.video_result_length }}</td>
                            </tr>
                            <tr>
                              <th>동영상 스타일</th>
                              <td>
                                {{ requestitem1.video_style }}
                                <div style="color: blue; float: right">{{ requestitem.tag_list }}</div>
                              </td>
                            </tr>
                            <tr>
                              <th>진행 날짜</th>
                              <td>
                                {{ requestitem1.start_date }} ~
                                {{ requestitem1.end_date }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem1.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>

                        <!-- for calendar -->
                        <calendar
                          :eventCategories="eventCategories"
                          :events="events"
                          ref="calendar"
                          style="float:left; width: 40%; height: 100%"
                        />
                      </b-card-text>
                      <!-- 비편집자일 때만 보이게 해야함 -->
                      <b-button
                        class="statusBtn"
                        style="background-color: #0099ff"
                        @click="doneRequest(requestitem1.rid)"
                      >요청 완료</b-button>
                    </b-card-body>
                  </b-collapse>
                </b-card>
              </div>
            </div>
          </tab-pane>

          <tab-pane>
            <span slot="title">
              <div>완료된 작업</div>
            </span>
            <div role="tablist">
              <div v-for="(requestitem2, index) in requestitems2" :key="index + '_requestitems2'">
                <b-card no-body class="m-1">
                  <b-card-header header-tag="header" class="p-1" role="tab">
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem2.uid"
                      variant="info"
                      @click="getDetail(requestitem2.rid)"
                    >
                      {{ requestitem2.request_nickname }}님과의 작업이
                      완료되었습니다.
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem2.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover" style="float:left; width: 60%">
                          <tbody>
                            <tr>
                              <th>요청자</th>
                              <td>{{ requestitem2.request_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem2.video_type }}</td>
                            </tr>
                            <tr>
                              <th>원본 동영상 길이</th>
                              <td>{{ requestitem2.video_origin_length }}</td>
                            </tr>
                            <tr>
                              <th>원하는 동영상 길이</th>
                              <td>{{ requestitem2.video_result_length }}</td>
                            </tr>
                            <tr>
                              <th>동영상 스타일</th>
                              <td>
                                {{ requestitem2.video_style }}
                                <div style="color: blue; float: right">{{ requestitem.tag_list }}</div>
                              </td>
                            </tr>
                            <tr>
                              <th>진행 날짜</th>
                              <td>
                                {{ requestitem2.start_date }} ~
                                {{ requestitem2.end_date }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem2.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>

                        <!-- for calendar -->
                        <calendar
                          :eventCategories="eventCategories"
                          :events="events"
                          ref="calendar"
                          style="float:left; width: 40%; height: 100%"
                        />
                      </b-card-text>
                      <b-button
                        class="statusBtn"
                        style="background-color: #0099ff"
                        @click="$bvModal.show('review')"
                      >후기 남기기</b-button>
                      <b-modal id="review" hide-footer>
                        <template v-slot:modal-title>후기 작성</template>
                        <div class="d-block text-center">
                          *편집된 영상에 만족하셨나요?
                          <div class="d-flex justify-content-center">
                            <star-rating :increment="0.5" v-model="videoScore"></star-rating>
                          </div>
                          <hr />*편집자가 친절하게 소통했나요?
                          <div class="d-flex justify-content-center">
                            <star-rating :increment="0.5" v-model="kindnessScore"></star-rating>
                          </div>
                          <hr />*편집자가 마감 기한을 잘 지켰나요?
                          <div class="d-flex justify-content-center">
                            <star-rating :increment="0.5" v-model="finishScore"></star-rating>
                          </div>
                          <hr />*솔직한 한 줄 평을 남겨주세요.
                          <div class="d-flex justify-content-center">
                            <textarea class="form-control" v-model="comment"></textarea>
                          </div>
                        </div>
                        <div class="d-flex justify-content-center mt-3">
                          <b-button @click="writeReview(requestitem2.uid)">작성 완료</b-button>
                          <b-button @click="$bvModal.hide('review')">창닫기</b-button>
                        </div>
                      </b-modal>
                    </b-card-body>
                  </b-collapse>
                </b-card>
              </div>
            </div>
          </tab-pane>
        </card>
      </tabs>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

import { mapGetters } from "vuex";
import store from "@/store/store.js";
import http from "@/util/http-common";
import alertify from "alertifyjs";

// for calenggar
import { Calendar } from "vue-sweet-calendar";
import "vue-sweet-calendar/dist/SweetCalendar.css";

// Install BootstrapVue
Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);

export default {
  name: "alarm",
  components: {
    Calendar,
  },
  data() {
    return {
      eventCategories: [
        {
          id: 1,
          title: "Personal",
          textColor: "white",
          backgroundColor: "Blue",
        },
        {
          id: 2,
          title: "Company-wide",
          textColor: "white",
          backgroundColor: "red",
        },
      ],
      events: [
        {
          title: "Event 1",
          start: "2020-08-10",
          end: "2020-08-15",
          repeat: "monthly",
          categoryId: 1,
        },
      ],
      videoScore: 0,
      kindnessScore: 0,
      finishScore: 0,
      comment: "",
      uid: "",
      nickname: "",
      account: "",
    };
  },
  created() {
    //생성 시 로그인 상태 확인
    if (this.$session.exists()) {
      store.dispatch(
        "getRequestitems0",
        "/request/res/" + this.$session.get("nickname") + "/0"
      );
      store.dispatch(
        "getRequestitems1",
        "/request/res/" + this.$session.get("nickname") + "/1"
      );
      store.dispatch(
        "getRequestitems2",
        "/request/res/" + this.$session.get("nickname") + "/2"
      );
      store
        .dispatch("auth/getUserInfo", this.$session.get("uid"))
        .then((response) => {
          this.uid = this.$session.get("uid");
          this.setUserInfo(response.data.object);
        });
    } else {
      this.$router.push("/");
      alert("로그인이 필요한페이지 입니다.");
    }
  },
  computed: {
    ...mapGetters(["requestitems0"]),
    ...mapGetters(["requestitems1"]),
    ...mapGetters(["requestitems2"]),
    ...mapGetters(["requestitem"]),
  },
  methods: {
    getDetail(rid) {
      store.dispatch("getRequestitem", "/request/" + rid);
    },
    // 요청 수락
    acceptRequest(rid) {
      http
        .put("/request/accept/" + rid)
        .then(({ data }) => {
          if (data == "success") {
            alertify.notify("요청을 수락하였습니다.", "success", 3);
          }
        })
        .catch(() => {
          // alert('요청 수락중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          store.dispatch(
            "getRequestitems0",
            "/request/res/" + this.$session.get("nickname") + "/0"
          );
          store.dispatch(
            "getRequestitems1",
            "/request/res/" + this.$session.get("nickname") + "/1"
          );
        });
    },
    // 요청 거절
    denyRequest(rid) {
      http
        .put("/request/deny/" + rid)
        .then(({ data }) => {
          if (data == "success") {
            alertify.notify("요청을 거절하였습니다.", "success", 3);
          }
        })
        .catch(() => {
          // alert('요청 거절중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          store.dispatch(
            "getRequestitems0",
            "/request/res/" + this.$session.get("nickname") + "/0"
          );
        });
    },
    // 요청 완료
    doneRequest(rid) {
      http
        .put("/request/done/" + rid)
        .then(({ data }) => {
          if (data == "success") {
            alertify.notify("요청이 완료되었습니다.", "success", 3);
          }
        })
        .catch(() => {
          // alert('요청 거절중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          store.dispatch(
            "getRequestitems1",
            "/request/res/" + this.$session.get("nickname") + "/1"
          );
          store.dispatch(
            "getRequestitems2",
            "/request/res/" + this.$session.get("nickname") + "/2"
          );
        });
    },

    // 캘린더 날짜 셋팅
    setCalendarDate(start, end) {
      this.events.start = start.substring(0, 10);
      this.events.end = end.substring(0, 10);
    },
    setUserInfo(data) {
      this.nickname = data.nickname;
    },
    writeReview(rid) {
      let msg = "리뷰 작성에 실패하였습니다.";
      http
        .post("/portfolio/review/" + this.uid, {
          portfolioUid: rid,
          nickname: this.nickname,
          videoScore: this.videoScore,
          kindnessScore: this.kindnessScore,
          finishScore: this.finishScore,
          comment: this.comment,
        })
        .then(({ data }) => {
          if (data.data == "success") {
            msg = "리뷰 작성이 완료되었습니다.";
            alertify.notify(msg, "success", 3);
            return;
          } else {
            alertify.error(msg, 3);
            return;
          }
        })
        .catch(() => {
          msg = "리뷰 서버 통신 실패";
          alertify.error(msg, 3);
          return;
        });
    },
  },
};
</script>

<style>
.statusBtn {
  border: none;
  margin: 10px;
  float: right;
}
</style>
