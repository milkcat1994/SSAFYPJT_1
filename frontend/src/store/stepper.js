export const stepper = {
  namespaced: true,
  state: {
    videoType: [
      {
        name: '개인용',
        value: 'pers',
        status: false,
        img: 'img/icons/search/personal.svg',
        description: '일상의 즐거움을 담은 vlog, 결혼식이나 돌잔치 영상 등 개인 영상을 편집하시는 경우 선택해주세요'
      },
      {
        name: '상업용',
        value: 'comm',
        status: false,
        img: 'img/icons/search/commercial.svg',
        description: '기업에서 맡기시는 경우 선택해주세요'
      }
    ],
    videoStyle: [
      // 유튜브 카테고리를 차용
      {
        name: "키즈",
        value: 'kids',
        icon: "fas fa-baby",
        status: false,
      },
      {
        name: "게임",
        value: 'game',
        icon: "fas fa-gamepad",
        status: false,
      },
      {
        name: "음악/댄스",
        value: 'musi',
        icon: "fas fa-music",
        status: false,
      },
      {
        name: "푸드/쿠킹",
        value: 'food',
        icon: "fas fa-utensils",
        status: false,
      },
      {
        name: "V-log",
        value: 'vlog',
        icon: "fas fa-user",
        status: false,
      },
      {
        name: "영화/애니메이션",
        value: 'movi',
        icon: "fas fa-film",
        status: false,
      },
      {
        name: "동물",
        value: 'anim',
        icon: "fas fa-paw",
        status: false,
      },
      {
        name: "뷰티/패션",
        value: 'beau',
        icon: "fas fa-spray-can",
        status: false,
      },
      {
        name: "스포츠",
        value: 'spor',
        icon: "fas fa-dumbbell",
        status: false,
      },
      {
        name: "기타",
        value: 'etcs',
        icon: "fas fa-question",
        status: false,
      },
    ],
    videoSkills: [
      {
        name: '색/밝기 조정',
        value: 'colr',
        img: 'img/icons/search/rgb.svg',
        status: false,
        description: '색, 밝기, 대비 등 영상 색상 관련 보정을 말합니다.',
      },
      {
        name: '오디오/음악',
        value: 'audi',
        img: 'img/icons/search/audio.svg',
        status: false,
        description: '음향 효과를 삽입/제거하거나 조정하는 작업을 말합니다.',
      },
      {
        name: '모션그래픽',
        value: 'moti',
        img: 'img/icons/search/motion_graphic.svg',
        status: false,
        description: '비디오 영상 혹은 애니메이션 기술을 이용하여 영상 속에 다양한 움직임이나 회전의 환영을 만들어내는 그래픽을 말합니다.',
      },
      {
        name: '자막',
        value: 'capt',
        img: 'img/icons/search/caption.svg',
        status: false,
        description: '영상 관련 자막 편집을 말합니다.',
      },
      {
        name: '인트로',
        value: 'intr',
        img: 'img/icons/search/hello.svg',
        status: false,
        description: '본 영상에 앞서 시작을 알리거나 소개하는 영상을 말합니다.',
      },
      {
        name: '아웃트로',
        value: 'outr',
        img: 'img/icons/search/goodbye.svg',
        status: false,
        description: '본 영상이 끝나고 결론을 보여주거나 크레딧을 표시하는 영상을 말합니다.',
      }
    ],
    originLength: [
      {
        name: '5분 이내',
        status: false,
        value: '5min'
      },
      {
        name: '15분 이내',
        status: false,
        value: '15min'
      },
      {
        name: '30분 이내',
        status: false,
        value: '30min'
      },
      {
        name: '1시간 이내',
        status: false,
        value: '1hr'
      },
      {
        name: '1시간 이상',
        status: false,
        value: 'over1hr'
      },
    ],
    finalLength: [
      {
        name: '1분 이내',
        status: false,
        value: '1min'
      },
      {
        name: '5분 이내',
        status: false,
        value: '5min'
      },
      {
        name: '10분 이내',
        status: false,
        value: '10min'
      },
      {
        name: '30분 이내',
        status: false,
        value: '30min'
      },
      {
        name: '30분 이상',
        status: false,
        value: 'over30min'
      },
    ],
    deadline: [],
  },
  getters: {
    // Stepper 세부사항 불러오기
    getSelectedVideoType(state) {
      for (const item of state.videoType) {
        if (item.status == true) {
          return item
        }
      }
      return;
    },
    getSelectedVideoStyle(state) {
      for (const item of state.videoStyle) {
        if (item.status == true) {
          return item
        }
      }
      return;
    },
    getSelectedVideoSkills(state) {
      let res = new Array();
      for (const item of state.videoSkills) {
        if (item.status == true) {
          res.push(item.value)
        }
      }
      return res;
    },
    getSelectedVideoSkillsName(state) {
      let res = new Array();
      for (const item of state.videoSkills) {
        if (item.status == true) {
          res.push(item.name)
        }
      }
      return res;
    },
    getOriginLength(state) {
      for (const item of state.originLength) {
        if (item.status == true) {
          return item
        }
      }
      return;
    },
    getFinalLength(state) {
      for (const item of state.finalLength) {
        if (item.status == true) {
          return item
        }
      }
      return;
    },
    getDeadline(state) {
      return state.deadline
    },
    // 해당 Step에 값이 있는지 검증
    isChecked(state, getters) {
      return step => {
        switch (step) {
          case 0:
            if (!getters.getSelectedVideoType) {
              return false
            } else {
              return true
            }
          case 1:
            if (!getters.getSelectedVideoStyle) {
              return false
            } else {
              return true
            }
          case 2:
            if (getters.getSelectedVideoSkills.length === 0) {
              return false
            } else {
              return true
            }
          case 3:
            if (!getters.getOriginLength) {
              return false
            } else {
              return true
            }
          case 4:
            if (!getters.getFinalLength) {
              return false
            } else {
              return true
            }
          case 5:
            if (getters.getDeadline.length < 2) {
              return false
            } else {
              return true
            }
        }
      }
    }
  },
  mutations: {
    // 초기화
    clearStepperStatus(state) {
      state.videoType.forEach(e => {
        e.status = false
      })
      state.videoStyle.forEach(e => {
        e.status = false
      })
      state.videoSkills.forEach(e => {
        e.status = false
      })
      state.originLength.forEach(e => {
        e.status = false
      })
      state.finalLength.forEach(e => {
        e.status = false
      })
      state.deadline = [];
    },
    // 항목별 초기화(검색 filter-finder에서 쓸 용도)
    clearFilterFinderStatus(state) {
      state.videoType.forEach(e => {
        e.status = false
      })
      state.videoStyle.forEach(e => {
        e.status = false
      })
      state.videoSkills.forEach(e => {
        e.status = false
      })
    },
    // Stepper 항목별 갱신
    selectVideoType(state, payload) {
      state.videoType.forEach(e => {
        if (e.value == payload) {
          e.status = true
        } else {
          e.status = false
        }
      })
    },
    selectVideoStyle(state, payload) {
      state.videoStyle.forEach(e => {
        if (e.value == payload) {
          e.status = true
        } else {
          e.status = false
        }
      })
    },
    selectVideoSkills(state, payload) {
      state.videoSkills.forEach(e => {
        if(e.value == payload) {
          e.status = !e.status
        }
      })
    },
    setOriginLength(state, payload) {
      state.originLength.forEach(e => {
        if (e.value == payload) {
          e.status = true
        } else {
          e.status = false
        }
      })
    },
    setFinalLength(state, payload) {
      state.finalLength.forEach(e => {
        if (e.value == payload) {
          e.status = true
        } else {
          e.status = false
        }
      })
    },
    
    setDeadline(state, payload) {
      // payload => "2020-08-01 to 2020-08-21"
      state.deadline = payload.split(" to ");
      // deadline => ["2020-08-01", "2020-08-21"]
    },
  }
}