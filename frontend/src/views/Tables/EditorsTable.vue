<template>
  <div style="max-height: 700px;">
    <div class="card p-4">
      <ul class="list-unstyled mt-4">
        <li class="mb-4" v-for="editor in editorsData" :key="editor.id">
          <router-link :to="`/portfolio?no=${editor.id}`">
          <div class="container">
            <div class="row">
              <div class="col-2">
                <img :src="editor.imgURL" :alt="editor.nickname" class="mr-3" style="max-width: 180px;">
              </div>
              <div class="col-10">
                <div class="d-flex align-items-stretch mt-4">
                  <div class="d-inline-flex ml-2">
                    <!-- nickname and bookmarks -->
                    <span class="display-1 mt-0 mb-1 text-default">{{editor.nickname}}</span>
                  </div>
                  <div class="d-inline-flex flex-column ml-3 mt-3">
                    <div><i class="display-4 ni ni-like-2"></i></div>
                    <div>{{editor.bookmarks}}</div>
                  </div>
                </div>
                <div class="ml-2 mt-4">
                  <!-- tags and estimated price -->
                  <div class="row">
                    <div class="col-6">{{editor.tags}}</div>
                    <div class="col-6 text-danger">예상 견적: 분당 {{editor.pay}}원</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          </router-link>
        </li>
      </ul>
      <scroll-loader :loader-method="fetchEditors">
        <div>불러오는 중...</div>
      </scroll-loader>
    </div>
  </div>
</template>
<script>
import ScrollLoader from "vue-scroll-loader"

export default {
  name: 'editors-table',
  props: {
    type: {
      type: String
    },
    title: String,
  },
  components:{
    ScrollLoader
  },
  data() {
    return {
      // 태그들
      tags:[],
      tag: '',

      // 편집자 리스트
      loadMore: true,
      editorsData: [
        // {
        //   id: portfolio.uid,
        //   nickname: portfolio.nickname,
        //   skills: portfolio.skills,
        //   pay: portfolio.pay_min
        // },
        {
          id: 1,
          imgURL: 'img/theme/ryan.jpg',
          nickname: '라이언',
          bookmarks: 555,
          status: 'pending',
          tags: '#화려한조명이감싸는#내가제일잘나가',
          pay: 60000
        },
        {
          id: 2,
          imgURL: 'img/theme/tube.png',
          nickname: '튜브',
          bookmarks: 222,
          status: 'completed',
          tags: '#흙수저#대충그까이꺼뭐#일단재생#가성비',
          pay: 10000
        },
        {
          id: 3,
          imgURL: 'img/theme/apeach.jpg',
          nickname: '어피치',
          bookmarks: 531,
          status: 'delayed',
          tags: '#고객중심#원하는대로#심플',
          pay: 72000
        },
        {
          id: 4,
          imgURL: 'img/theme/frodo.png',
          nickname: '프로도',
          bookmarks: 333,
          status: 'on schedule',
          tags: '#그냥믿고맡기는#베테랑',
          pay: 90000
        },
        {
          id: 5,
          imgURL: 'img/theme/muzi.png',
          nickname: '무지',
          bookmarks: 444,
          status: 'completed',
          tags: '#알지#믿지#예술의경지',
          pay: 100000
        }
      ]
    }
  },
  methods: {
    fetchEditors() {
      this.editorsData.concat([
        {
          id: this.editorsData.length + 1,
          imgURL: 'img/theme/ryan.jpg',
          nickname: '라이언',
          bookmarks: 555,
          status: 'pending',
          tags: '#화려한조명이감싸는#내가제일잘나가',
          pay: 60000
        },
        {
          id: this.editorsData.length + 2,
          imgURL: 'img/theme/tube.png',
          nickname: '튜브',
          bookmarks: 222,
          status: 'completed',
          tags: '#흙수저#대충그까이꺼뭐#일단재생#가성비',
          pay: 10000
        },
        {
          id: this.editorsData.length + 3,
          imgURL: 'img/theme/apeach.jpg',
          nickname: '어피치',
          bookmarks: 531,
          status: 'delayed',
          tags: '#고객중심#원하는대로#심플',
          pay: 72000
        },
        {
          id: this.editorsData.length + 4,
          imgURL: 'img/theme/frodo.png',
          nickname: '프로도',
          bookmarks: 333,
          status: 'on schedule',
          tags: '#그냥믿고맡기는#베테랑',
          pay: 90000
        },
        {
          id: this.editorsData.length + 5,
          imgURL: 'img/theme/muzi.png',
          nickname: '무지',
          bookmarks: 444,
          status: 'completed',
          tags: '#알지#믿지#예술의경지',
          pay: 100000
        }
      ])
    }
  }
}
</script>
<style>
</style>
