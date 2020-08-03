<template>
  <card class="card-search" :show-footer-line="true">
    <div class="row">

      <div class="col align-items-">
        <slot>
          <h3 class="card-title text-uppercase text-muted mb-0" v-if="title">{{title}}</h3>
          <h1 class="font-weight-bold mb-0" v-if="subTitle">{{subTitle}}</h1>
        </slot>
      </div>

      <div class="col-auto" v-if="$slots.icon || icon">
        <slot name="icon">
          <div class="icon icon-shape text-white rounded-circle shadow"
               :class="[`bg-${type}`, iconClasses]">
            <i :class="icon"></i>
          </div>
        </slot>
      </div>
    </div>

    <p class="mt-0 mb-0 text-sm">
      <slot name="footer">

      </slot>
    </p>
  </card>
</template>
<script>
  import Card from './Card.vue';

  export default {
    name: 'search-card',
    components: {
      Card
    },
    props: {
      type: {
        type: String,
        default: 'primary'
      },
      icon: String,
      title: String,
      subTitle: String,
      iconClasses: [String, Array]
    }
  };
</script>
<style></style>
