<template>
  <a-layout-footer style="text-align: center;">
    Address: No. 24, 1st South Section, Yihuan Road, Wuhou District, Chengdu, Sichuan Province, P.R. China 610065 {{user.name}}
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);

    return{
      user,
    }
  }
});
</script>