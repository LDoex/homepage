<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/memberinfo">
        <router-link to="/admin/memberinfo">团队人员管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/homeCategory">
        <router-link to="/admin/homeCategory">主页分类管理</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'the-header'
});
</script>