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
      <a class="login-menu" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        title="登录"
        @ok="login">
      <a-form :model="loginUser" :label-col="{span:6}" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName">
            <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
          </a-input>
        </a-form-item>

        <a-form-item label="密码">
          <a-input-password v-model:value="loginUser.password">
            <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
          </a-input-password>
        </a-form-item>

      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import validate = WebAssembly.validate;
import _default from "ant-design-vue/es/color-picker";
import axios from "axios";
import {message} from "ant-design-vue";

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'the-header',
  setup(){
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    const showLoginModal = () => {
      loginModalVisible.value = true;
      loginUser.value.loginName = "";
      loginUser.value.password = "";
    };

    //登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if(data.success){
          loginModalVisible.value = false;
          message.success('登录成功');
        } else{
          message.error(data.message);
        }
      });
    };

    // const handleLoginModalOk = () => {
    //   loginModalLoading.value = true;
    //   axios.post("/login", loginUser.value).then((response) => {
    //     loginModalLoading.value = false;
    //     const data = response.data;
    //     if(data.success){
    //       loginModalVisible.value = false;
    //       message.success('登录成功');
    //     } else{
    //       message.error(data.message);
    //     }
    //   });
    // };

    return{
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      login,
      loginUser,
    }
  }
});
</script>

<style scoped>
.login-menu{
  color: white;
  position: fixed;
  right: 2rem;
}
</style>