<template>
  <div style="max-height: 1200px;">
    <div class="card p-4">
      <ul class="list-unstyled mt-4">
        <li class="mb-4" v-for="editor in currentEditors" :key="editor.id">
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
      <div class="card-footer d-flex justify-content-end"
         :class="type === 'dark' ? 'bg-transparent': ''">
        <base-pagination
          :total="editorsData.length"
          :perPage="editorsPerPage"
          :value="currentPage"
          @input="fetchPage"
        ></base-pagination>
      </div>
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
    title: String,
  },
  components:{
  },
  computed: {
    currentEditors() {
      let start = (this.currentPage-1) * this.editorsPerPage
      let end = this.currentPage * this.editorsPerPage
      return this.editorsData.slice(start, end)
    },
  },
  data() {
    return {
      // 태그들
      tags:[],
      tag: '',
      editorsPerPage: 3,
      currentPage: 1,
      // 편집자 리스트
      editorsData: [
        // {
        //   id: portfolio.uid,
        //   nickname: portfolio.nickname,
        //   skills: portfolio.skills,
        //   tags: portfolio.tags,
        //   pay: portfolio.pay_min
        // },
        {
          id: 1,
          imgURL: 'img/theme/kakaofriends/ryan.jpg',
          nickname: '라이언',
          bookmarks: 555,
          tags: '#화려한조명이감싸는#내가제일잘나가',
          pay: 60000
        },
        {
          id: 2,
          imgURL: 'img/theme/kakaofriends/tube.png',
          nickname: '튜브',
          bookmarks: 222,
          tags: '#흙수저#대충그까이꺼뭐#일단재생#가성비',
          pay: 10000
        },
        {
          id: 3,
          imgURL: 'img/theme/kakaofriends/apeach.jpg',
          nickname: '어피치',
          bookmarks: 531,
          tags: '#고객중심#원하는대로#심플',
          pay: 72000
        },
        {
          id: 4,
          imgURL: 'img/theme/kakaofriends/frodo.png',
          nickname: '프로도',
          bookmarks: 333,
          tags: '#그냥믿고맡기는#베테랑',
          pay: 90000
        },
        {
          id: 5,
          imgURL: 'img/theme/kakaofriends/muzi.png',
          nickname: '무지',
          bookmarks: 444,
          tags: '#알지#믿지#예술의경지',
          pay: 100000
        },
        {
          id: 6,
          imgURL: 'img/theme/kakaofriends/jay-g.jpg',
          nickname: '제이지',
          bookmarks: 321,
          tags: '#아재감성#하지만실력은진짜',
          pay: 80000
        },
        {
          id: 7,
          imgURL: 'img/theme/kakaofriends/neo.jfif',
          nickname: '네오',
          bookmarks: 777,
          tags: '#네#넹#넵#네ㅠ#넴#넼#네!',
          pay: 30000
        },
        {
          id: 7,
          imgURL: 'img/theme/kakaofriends/concon.jfif',
          nickname: '콘',
          bookmarks: 456,
          tags: '#무지챙기는것처럼#편집도세심하게',
          pay: 40000
        },
      ]
    }
  },
  methods: {
    fetchEditors() {
      // BE API를 불러오는 방식으로 변경해야 함
    },
    fetchPage(val) {
      this.currentPage = val
    }
  }
}
</script>
<style>
</style>
