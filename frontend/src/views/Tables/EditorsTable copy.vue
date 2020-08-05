<template>
  <div class="card shadow"
       :class="type === 'dark' ? 'bg-default': ''">
    <div class="card-header border-0"
         :class="type === 'dark' ? 'bg-transparent': ''">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white': ''">
            {{title}}
          </h3>
        </div>
        <div class="col text-right">
          <base-button type="primary" size="sm">See all</base-button>
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <base-table class="table align-items-center table-flush"
                  :class="type === 'dark' ? 'table-dark': ''"
                  :thead-classes="type === 'dark' ? 'thead-dark': 'thead-light'"
                  tbody-classes="list"
                  :data="editorsData">
        <template slot="columns">
          <th>편집자</th>
          <th>별점</th>
          <th>상태</th>
          <th>선호 태그</th>
          <th>작업 진행 상황</th>
          <th></th>
        </template>

        <template slot-scope="{row}">
          <th scope="row">
            <div class="media align-items-center">
              <a href="#" class="avatar rounded-circle mr-3">
                <!-- 편집자 대표 이미지 -->
                <img alt="Image placeholder" :src="row.img">
              </a>
              <div class="media-body">
                <span class="name mb-0 text-sm">{{row.title}}</span>
              </div>
            </div>
          </th>
          <td class="stars">
            <badge type="secondary">{{row.stars}}</badge>
          </td>
          <td>
            <badge class="badge-dot mr-4" :type="row.statusType">
              <i :class="`bg-${row.statusType}`"></i>
              <span class="status">{{row.status}}</span>
            </badge>
          </td>
          <td>
            <div class="avatar-group">
              <a href="#" class="avatar avatar-sm rounded-circle" data-toggle="tooltip" data-original-title="Ryan Tompson">
                <img alt="Image placeholder" src="img/theme/team-1-800x800.jpg">
              </a>
              <a href="#" class="avatar avatar-sm rounded-circle" data-toggle="tooltip" data-original-title="Romina Hadid">
                <img alt="Image placeholder" src="img/theme/team-2-800x800.jpg">
              </a>
              <a href="#" class="avatar avatar-sm rounded-circle" data-toggle="tooltip" data-original-title="Alexander Smith">
                <img alt="Image placeholder" src="img/theme/team-3-800x800.jpg">
              </a>
              <a href="#" class="avatar avatar-sm rounded-circle" data-toggle="tooltip" data-original-title="Jessica Doe">
                <img alt="Image placeholder" src="img/theme/team-4-800x800.jpg">
              </a>
            </div>
          </td>

          <td>
            <div class="d-flex align-items-center">
              <span class="completion mr-2">{{row.completion}}%</span>
              <div>
                <base-progress :type="row.statusType"
                               :show-percentage="false"
                               class="pt-0"
                               :value="row.completion"/>
              </div>
            </div>
          </td>

          <td class="text-right">
            <router-link to="/portfolio?no=1">
              <base-button class="btn btn-secondary">DETAIL</base-button>
            </router-link>
            <!-- <base-dropdown class="dropdown"
                           position="right">
              <a slot="title" class="btn btn-sm btn-icon-only text-light" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-ellipsis-v"></i>
              </a>

              <template>
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <a class="dropdown-item" href="#">Something else here</a>
              </template>
            </base-dropdown> -->
          </td>

        </template>

      </base-table>
    </div>

    <div class="card-footer d-flex justify-content-end"
         :class="type === 'dark' ? 'bg-transparent': ''">
      <base-pagination total="30"></base-pagination>
    </div>

  </div>
</template>
<script>

export default {
  name: 'editors-table',
  props: {
    type: {
      type: String
    },
    title: String
  },
  data() {
    return {
      //태그들
      tags:[],
      tag: '',
      editorsData: [
        {
          img: 'img/theme/ryan.jpg',
          title: '라이언',
          stars: 5,
          status: 'pending',
          statusType: 'warning',
          completion: 60
        },
        {
          img: 'img/theme/tube.png',
          title: '튜브',
          stars: 2,
          status: 'completed',
          statusType: 'success',
          completion: 100
        },
        {
          img: 'img/theme/apeach.jpg',
          title: '어피치',
          stars: 5,
          status: 'delayed',
          statusType: 'danger',
          completion: 72
        },
        {
          img: 'img/theme/frodo.png',
          title: '프로도',
          stars: 3,
          status: 'on schedule',
          statusType: 'info',
          completion: 90
        },
        {
          img: 'img/theme/muzi.png',
          title: '무지',
          stars: 4,
          status: 'completed',
          statusType: 'success',
          completion: 100
        }
      ]
    }
  }
}
</script>
<style>
</style>
