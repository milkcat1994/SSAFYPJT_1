<template>
  <div class="container mx-auto my-4 py-4" style="width: 80%">
    
    <h1 class="display-3 mb-2 text-center">추가하실 편집 기술이 있으신가요?</h1>

    <div class="container">
      <div class="row d-flex justify-content-center">
        <div class="card col-4 col-md-3 m-2 p-2 text-center"
            v-for="skill in skills"
            :key="skill.name"
            :class="{selected: !!skill.status}"
            @click.prevent="selectSkill(skill.name)">
          <div class="card-body">
            <img class="icon" :src="skill.img" :alt="skill.name">
            <h2 class="card-text mt-2">{{skill.description}}</h2>
            <!-- <span v-if="skill.name === 'motion'"
                  :data-tooltip-text="tooltip"> -->
              <!-- <i class="far fa-question-circle fa-md"></i>
            </span> -->
          </div>
          <!-- <i v-if="!!skill.status" class="fas fa-check-circle fa-sm"></i> -->
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";

export default {
  name: "edit-skill",
  props: {
    type: {
      type: String,
    },
    title: String,
    clickedNext: {
      type: Boolean,
    },
    currentStep: {
      type: Object,
    },
  },
  mixins: [validationMixin],
  data() {
    return {
      skills: [
        {
          name: 'color',
          img: 'img/icons/search/rgb.svg',
          status: false,
          description: '색 보정',
        },
        {
          name: 'audio',
          img: 'img/icons/search/audio.svg',
          status: false,
          description: '음향',
        },
        {
          name: 'motion',
          img: 'img/icons/search/motion_graphic.svg',
          status: false,
          description: '모션그래픽',
        },
        {
          name: 'caption',
          img: 'img/icons/search/caption.svg',
          status: false,
          description: '자막',
        },
        {
          name: 'intro',
          img: 'img/icons/search/hello.svg',
          status: false,
          description: '인트로',
        },
        {
          name: 'outro',
          img: 'img/icons/search/goodbye.svg',
          status: false,
          description: '아웃트로',
        }
      ],
    };
  },
  methods: {
    selectSkill(name) {
      this.skills.forEach(e => {
        if (e.name === name) {
          e.status = !e.status
        }
      })
    },
  },
  validations() {
    return {
      skills: {
        required
      }
    }
  },
  watch: {
    $v: {
      handler: function (val) {
        if (!val.$invalid) {
          this.$emit("can-continue", { value: true });
        } else {
          this.$emit("can-continue", { value: false });
        }
      },
      deep: true,
    },
    clickedNext(val) {
      if (val === true) {
        this.$v.skills.$touch();
        this.setSkills()
      }
    },
    skills: {
      deep: true,
      handler(val) {
        this.$store.commit('setSkills', {value: val})
      }
    },
  },
  mounted() {
    if (!this.$v.$invalid) {
      this.$emit("can-continue", { value: true });
    } else {
      this.$emit("can-continue", { value: false });
    }
  },
};
</script>
<style scoped>
.card {
}
.icon {
  /* display: inline-block; */
  width: 100px;
  height: 100px;
}
.selected {
  border: solid thick darkblue;
}
/* [data-tooltip-text]:hover {
  position: relative;
}
[data-tooltip-text]:hover:after {
	content: attr(data-tooltip-text);

  position: absolute;
	top: 100%;
	left: 0;

  min-width: 200px;
  
  background-color: rgba(0, 0, 0, 0.8);
	color: #FFFFFF;
  white-space: pre;
	font-size: 12px;

	z-index: 1;
} */
</style>
