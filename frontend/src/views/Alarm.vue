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
    <!-- <h2 class="text-center">알림창</h2> -->
    <br />

    <div class="container-fluid mt--7 mb-5">
      <!-- for calendar -->
      <br />
      <br />
      <div style="display: table; width:100%">
        <div style="display: table-cell; text-align:center;">
          <calendar
            :eventCategories="eventCategories"
            :events="events=scheduledate"
            ref="calendar"
          />
          <i class="fas fa-circle" style="color: #f29661; margin: 15px;">선택된 작업</i>
          <i class="fas fa-circle" style="color: #6699ff; margin: 15px">진행중 작업</i>
          <i
            class="fas fa-circle"
            style="color: #ff0066; margin: 15px"
            v-if="$session.get('auth') == 'editor'"
          >개인 일정</i>
        </div>
      </div>

      <tabs fill class="flex-column flex-md-row">
        <card shadow>
          <tab-pane>
            <span slot="title">
              <div v-if="$session.get('auth') == 'editor'">요청된 작업</div>
              <div v-if="$session.get('auth') == 'noneditor'">요청한 작업</div>
            </span>
            <div role="tablist">
              <div v-for="(requestitem0, index) in requestitems0" :key="index + '_requestitems0'">
                <b-card no-body class="m-1">
                  <b-card-header header-tag="header" class="p-1" role="tab">
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem0.uid"
                      variant="info"
                      @click="
                        getDetail(requestitem0.rid);
                        setRequestDate(
                          requestitem0.start_date,
                          requestitem0.end_date,
                          requestitem0.rid
                        );
                      "
                      v-if="$session.get('auth') == 'editor'"
                    >
                      {{ requestitem0.request_nickname }}님이 작업을
                      요청했습니다.
                    </b-button>
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem0.uid"
                      variant="info"
                      @click="
                        getDetail(requestitem0.rid);
                        setRequestDate(
                          requestitem0.start_date,
                          requestitem0.end_date,
                          requestitem0.rid
                        );
                      "
                      v-if="$session.get('auth') == 'noneditor'"
                    >
                      <span v-if="$session.get('auth') == 'editor'">
                        {{ requestitem0.request_nickname }}님이 작업이
                        요청했습니다.
                      </span>
                      <span v-if="$session.get('auth') == 'noneditor'">
                        {{ requestitem0.response_nickname }}님께 작업을
                        요청했습니다.
                      </span>
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem0.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover">
                          <tbody>
                            <tr v-if="$session.get('auth') == 'editor'">
                              <th>요청자</th>
                              <td>{{ requestitem0.request_nickname }}</td>
                            </tr>
                            <tr v-if="$session.get('auth') == 'noneditor'">
                              <th>편집자</th>
                              <td>{{ requestitem0.response_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem0.video_type }}</td>
                            </tr>
                            <tr>
                              <th>영상 스킬</th>
                              <td>{{ requestitem0.video_skill }}</td>
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
                                {{ requestitem0.start_date.substring(0, 10) }} ~
                                {{ requestitem0.end_date.substring(0, 10) }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem0.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </b-card-text>
                      <div id="editorBtn" v-if="$session.get('auth') == 'editor'">
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
                      </div>
                      <div id="noneditorBtn" v-if="$session.get('auth') == 'noneditor'">
                        <b-button
                          class="statusBtn"
                          style="background-color: #aaaaaa"
                          @click="denyRequest(requestitem0.rid)"
                        >요청 취소</b-button>
                      </div>
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
                      @click="
                        getDetail(requestitem1.rid);
                        setRequestDate(
                          requestitem1.start_date,
                          requestitem1.end_date,
                          requestitem1.rid
                        );
                      "
                    >
                      <span v-if="$session.get('auth') == 'editor'">
                        {{ requestitem1.request_nickname }}님과의 작업이
                        진행중입니다.
                      </span>
                      <span v-if="$session.get('auth') == 'noneditor'">
                        {{ requestitem1.response_nickname }}님과의 작업이
                        진행중입니다.
                      </span>
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem1.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover">
                          <tbody>
                            <tr v-if="$session.get('auth') == 'editor'">
                              <th>요청자</th>
                              <td>{{ requestitem1.request_nickname }}</td>
                            </tr>
                            <tr v-if="$session.get('auth') == 'noneditor'">
                              <th>편집자</th>
                              <td>{{ requestitem1.response_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem1.video_type }}</td>
                            </tr>
                            <tr>
                              <th>영상 스킬</th>
                              <td>{{ requestitem1.video_skill }}</td>
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
                                {{ requestitem1.start_date.substring(0, 10) }} ~
                                {{ requestitem1.end_date.substring(0, 10) }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem1.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </b-card-text>
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
                      @click="
                        getDetail(requestitem2.rid);
                        setRequestDate(
                          requestitem2.start_date,
                          requestitem2.end_date,
                          requestitem2.rid
                        );
                      "
                    >
                      <span v-if="$session.get('auth') == 'editor'">
                        {{ requestitem2.request_nickname }}님과의 작업이
                        완료되었습니다.
                      </span>
                      <span v-if="$session.get('auth') == 'noneditor'">
                        {{ requestitem2.response_nickname }}님과의 작업이
                        완료되었습니다.
                      </span>
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem2.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover">
                          <tbody>
                            <tr v-if="$session.get('auth') == 'editor'">
                              <th>요청자</th>
                              <td>{{ requestitem2.request_nickname }}</td>
                            </tr>
                            <tr v-if="$session.get('auth') == 'noneditor'">
                              <th>편집자</th>
                              <td>{{ requestitem2.response_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem2.video_type }}</td>
                            </tr>
                            <tr>
                              <th>영상 스킬</th>
                              <td>{{ requestitem2.video_skill }}</td>
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
                                {{ requestitem2.start_date.substring(0, 10) }} ~
                                {{ requestitem2.end_date.substring(0, 10) }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem2.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </b-card-text>
                      <b-button
                        class="statusBtn"
                        style="background-color: #0099ff"
                        v-if="$session.get('auth') == 'noneditor'"
                        @click="$bvModal.show('review-'+requestitem2.rid)"
                      >후기 남기기</b-button>
                      <b-modal :id="'review-'+requestitem2.rid" hide-footer>
                        <template v-slot:modal-title>후기 작성</template>
                        <div class="d-block text-center">
                          *편집된 영상에 만족하셨나요?
                          <div class="d-flex justify-content-center">
                            <star-rating v-model="videoScore"></star-rating>
                          </div>
                          <hr />*편집자가 친절하게 소통했나요?
                          <div class="d-flex justify-content-center">
                            <star-rating v-model="kindnessScore"></star-rating>
                          </div>
                          <hr />*편집자가 마감 기한을 잘 지켰나요?
                          <div class="d-flex justify-content-center">
                            <star-rating v-model="finishScore"></star-rating>
                          </div>
                          <hr />*솔직한 한 줄 평을 남겨주세요.
                          <div class="d-flex justify-content-center">
                            <textarea class="form-control" v-model="comment"></textarea>
                          </div>
                        </div>
                        <div class="d-flex justify-content-center mt-3">
                          <b-button
                            @click="
                              writeReview(
                                requestitem2.response_nickname,
                                requestitem2.rid
                              )
                            "
                          >작성 완료</b-button>
                          <b-button @click="$bvModal.hide('review-'+requestitem2.rid)">창닫기</b-button>
                        </div>
                      </b-modal>
                    </b-card-body>
                  </b-collapse>
                </b-card>
              </div>
              <div v-for="(requestitem3, index) in requestitems3" :key="index + '_requestitems3'">
                <b-card no-body class="m-1">
                  <b-card-header header-tag="header" class="p-1" role="tab">
                    <b-button
                      block
                      v-b-toggle="'accordion-' + requestitem3.uid"
                      variant="primary"
                      @click="
                        getDetail(requestitem3.rid);
                        setRequestDate(
                          requestitem3.start_date,
                          requestitem3.end_date,
                          requestitem3.rid
                        );
                      "
                    >
                      <span v-if="$session.get('auth') == 'editor'">
                        {{ requestitem3.request_nickname }}님과의 작업이
                        완료되었습니다.
                      </span>
                      <span v-if="$session.get('auth') == 'noneditor'">
                        {{ requestitem3.response_nickname }}님과의 작업이
                        완료되었습니다.
                      </span>
                    </b-button>
                  </b-card-header>
                  <b-collapse
                    :id="'accordion-' + requestitem3.uid"
                    accordion="my-accordion"
                    role="tabpanel"
                  >
                    <b-card-body>
                      <b-card-text>
                        <table class="table table-hover" style="float:left; width: 100%">
                          <tbody>
                            <tr v-if="$session.get('auth') == 'editor'">
                              <th>요청자</th>
                              <td>{{ requestitem3.request_nickname }}</td>
                            </tr>
                            <tr v-if="$session.get('auth') == 'noneditor'">
                              <th>편집자</th>
                              <td>{{ requestitem3.response_nickname }}</td>
                            </tr>
                            <tr>
                              <th>영상 타입</th>
                              <td>{{ requestitem3.video_type }}</td>
                            </tr>
                            <tr>
                              <th>영상 스킬</th>
                              <td>{{ requestitem3.video_skill }}</td>
                            </tr>
                            <tr>
                              <th>원본 동영상 길이</th>
                              <td>{{ requestitem3.video_origin_length }}</td>
                            </tr>
                            <tr>
                              <th>원하는 동영상 길이</th>
                              <td>{{ requestitem3.video_result_length }}</td>
                            </tr>
                            <tr>
                              <th>동영상 스타일</th>
                              <td>
                                {{ requestitem3.video_style }}
                                <div style="color: blue; float: right">{{ requestitem.tag_list }}</div>
                              </td>
                            </tr>
                            <tr>
                              <th>진행 날짜</th>
                              <td>
                                {{ requestitem3.start_date.substring(0, 10) }} ~
                                {{ requestitem3.end_date.substring(0, 10) }}
                              </td>
                            </tr>
                            <tr>
                              <th>기타 요청사항</th>
                              <td>{{ requestitem3.request_description }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </b-card-text>
                      <b-button
                        class="statusBtn"
                        style="background-color: #0099ff"
                        v-if="$session.get('auth') == 'noneditor'"
                        @click="getReview(requestitem3.rid)"
                      >후기 보기</b-button>
                      <b-modal id="donereview" hide-footer>
                        <template v-slot:modal-title>내가 쓴 후기</template>
                        <div class="d-block text-center">
                          영상만족도
                          <div class="d-flex justify-content-center">
                            <star-rating :rating="EvideoScore" :read-only="true"></star-rating>
                          </div>
                          <hr />친절도
                          <div class="d-flex justify-content-center">
                            <star-rating :rating="EkindnessScore" :read-only="true"></star-rating>
                          </div>
                          <hr />마감속도
                          <div class="d-flex justify-content-center">
                            <star-rating :rating="EfinishScore" :read-only="true"></star-rating>
                          </div>
                          <hr />한줄평
                          <div class="d-flex justify-content-center">
                            <textarea class="form-control" v-model="Ecomment"></textarea>
                          </div>
                        </div>
                        <div class="d-flex justify-content-center mt-3">
                          <b-button variant="danger" @click="deleteReview(requestitem3.rid)">삭제하기</b-button>
                          <b-button @click="$bvModal.hide('donereview')">창닫기</b-button>
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
      ],

      ridDetail: "",

      events: [],
      videoScore: 0,
      kindnessScore: 0,
      finishScore: 0,
      comment: "",
      uid: 0,
      nickname: "",

      EvideoScore: 0,
      EkindnessScore: 0,
      EfinishScore: 0,
      Ecomment: "",
      complete: false,
    };
  },
  created() {
    //생성 시 로그인 상태 확인
    if (this.$session.exists()) {
      store
        .dispatch("auth/getUserInfo", this.$session.get("uid"))
        .then((response) => {
          this.uid = this.$session.get("uid");
          this.setUserInfo(response.data.object);
        })
        .catch(() => {
          this.$router.push("/");
        });
      if (this.$session.get("auth") == "editor") {
        store.dispatch(
          "getProgressdate",
          "/request/date/res/" + this.$session.get("nickname")
        );
        store.dispatch(
          "getHolidaydate",
          "/schedule/holiday/" + this.$session.get("uid")
        );
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
      } else if (this.$session.get("auth") == "noneditor") {
        store.dispatch(
          "getProgressdate",
          "/request/date/req/" + this.$session.get("nickname")
        );
        store.dispatch(
          "getHolidaydate",
          "/schedule/holiday/" + this.$session.get("uid")
        );
        store.dispatch(
          "getRequestitems0",
          "/request/req/" + this.$session.get("nickname") + "/0"
        );
        store.dispatch(
          "getRequestitems1",
          "/request/req/" + this.$session.get("nickname") + "/1"
        );
        store.dispatch(
          "getRequestitems2",
          "/request/req/" + this.$session.get("nickname") + "/2"
        );
        store.dispatch(
          "getRequestitems3",
          "/request/req/" + this.$session.get("nickname") + "/3"
        );
      }
      this.readNotify();
    } 
  },
  computed: {
    ...mapGetters(["requestitems0"]),
    ...mapGetters(["requestitems1"]),
    ...mapGetters(["requestitems2"]),
    ...mapGetters(["requestitems3"]),
    ...mapGetters(["requestitem"]),
    ...mapGetters(["progressdate"]),
    ...mapGetters(["holidaydate"]),
    ...mapGetters(["scheduledate"]),
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
          this.setProgressDate();
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
          if (this.$session.get("auth") == "editor")
            store.dispatch(
              "getRequestitems0",
              "/request/res/" + this.$session.get("nickname") + "/0"
            );
          else if (this.$session.get("auth") == "noneditor")
            store.dispatch(
              "getRequestitems0",
              "/request/req/" + this.$session.get("nickname") + "/0"
            );
          this.setDateClean();
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
          if (this.$session.get("auth") == "editor") {
            store.dispatch(
              "getRequestitems1",
              "/request/res/" + this.$session.get("nickname") + "/1"
            );
            store.dispatch(
              "getRequestitems2",
              "/request/res/" + this.$session.get("nickname") + "/2"
            );
          } else if (this.$session.get("auth") == "noneditor") {
            store.dispatch(
              "getRequestitems1",
              "/request/req/" + this.$session.get("nickname") + "/1"
            );
            store.dispatch(
              "getRequestitems2",
              "/request/req/" + this.$session.get("nickname") + "/2"
            );
          }
          this.setProgressDate();
        });
    },
    // 후기 완료
    doneReview(rid) {
      http
        .put("/request/doneReview/" + rid)
        .then(({ data }) => {
          if (data == "success") {
            // alertify.notify("후기 작성이 완료되었습니다.", "success", 3);
          }
        })
        .catch(() => {
          // alert('요청 거절중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          if (this.$session.get("auth") == "noneditor") {
            store.dispatch(
              "getRequestitems2",
              "/request/req/" + this.$session.get("nickname") + "/2"
            );
            store.dispatch(
              "getRequestitems3",
              "/request/req/" + this.$session.get("nickname") + "/3"
            );
          }
        });
    },

    setUserInfo(data) {
      this.nickname = data.nickname;
    },
    getReview(rid) {
      http
        .get("request/review/" + rid)
        .then(({ data }) => {
          (this.EvideoScore = data.videoScore),
            (this.EkindnessScore = data.kindnessScore),
            (this.EfinishScore = data.finishScore),
            (this.Ecomment = data.comment);
          this.$bvModal.show("donereview");
        })
        .catch((err) => {
          console.log(err);
        });
    },
    writeReview(editor_nickname, rid) {
      let msg = "리뷰 작성에 실패하였습니다.";
      // console.log(editor_nickname, rid);
      http
        .post("/request/review", {
          requestFormRid: rid,
          nickname: editor_nickname,
          videoScore: this.videoScore,
          kindnessScore: this.kindnessScore,
          finishScore: this.finishScore,
          comment: this.comment,
        })
        .then(({ data }) => {
          // console.log(this.videoScore);
          if (data == 1) {
            msg = "리뷰 작성이 완료되었습니다.";
            alertify.notify(msg, "success", 3);
            this.$bvModal.hide("review-" + rid);
            this.doneReview(rid);
            return;
          } else {
            alertify.error(msg, 3);
            return;
          }
        })
        .catch((err) => {
          console.log(err);
          msg = "리뷰 서버 통신 실패";
          alertify.error(msg, 3);
          return;
        });
    },
    deleteReview(rid) {
      let msg = "리뷰 삭제 실패했습니다.";
      alertify.confirm(
        "리뷰 삭제",
        "삭제 하시겠습니까?",
        function () {
          http
            .delete("request/review/" + rid)
            .then(({ data }) => {
              if (data == 1) {
                msg = "삭제가 완료되었습니다.";
                alertify.notify(msg, "success", 3);
                return;
              } else {
                msg = "삭제에 실패하였습니다.";
                alertify.error(msg, 3);
                return;
              }
            })
            .catch(() => {
              msg = "서버 통신 실패";
              alertify.error(msg, 3);
              return;
            });
        },
        function () {
          alertify.error("취소되었습니다.");
        }
      );
    },
    setRequestDate(start, end, rid) {
      if (this.ridDetail == rid) {  // 상세보기가 열려잇으면 닫으면서 날짜표시 지움
        this.setDateClean();
        this.ridDetail = "";
        return;
      }
      this.ridDetail = rid;
      this.events[this.events.length - 1].start = start.substring(0, 10);
      this.events[this.events.length - 1].end = end.substring(0, 10);
    },
    setDateClean() {
      this.events[this.events.length - 1].start = "";
      this.events[this.events.length - 1].end = "";
    },
    setProgressDate() {
      if (this.$session.get("auth") == "editor")
        store.dispatch(
          "getProgressdate",
          "/request/date/res/" + this.$session.get("nickname")
        );
      else if (this.$session.get("auth") == "noneditor")
        store.dispatch(
          "getProgressdate",
          "/request/date/req/" + this.$session.get("nickname")
        );
      store.dispatch(
        "getHolidaydate",
        "/schedule/holiday/" + this.$session.get("uid")
      );
      this.events = this.scheduledate;
    },

    // 알림 읽음 처리
    readNotify(){
      http
        .put('/request/notify/read/' + this.$session.get('nickname'))
        .then(({ data }) => {
          if (data == 'success'){
            // alert('알람 읽음 완료');
            }
        })
        .catch(() => {
          // alert('요청 거절중 에러가 발생했습니다.');
        })
        .finally(() => {
          // 목록 새로고침
          store.dispatch('getNotifyitems', '/request/notify/' + this.$session.get('nickname'));
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