<template>
  <a-layout-header class="header" style="height: 6rem">
    <a-row type="flex" justify="space-between">
      <a-col :xs="{span:20}" :sm="4" :md="6" :lg="8" :xl="6">
        <div class="college">
          <router-link to="/" style="color: #FFFFFF">
            <a-avatar  shape="square" :size="{ xs: 280, sm: 80, md: 80, lg: 460, xl: 460, xxl: 460 }" src="/image/footer-logo.png"/>
          </router-link>
<!--          <img src="/image/footer-logo.png" width="120">-->
        </div>
      </a-col>
      <a-col :xs="0" :sm="4" :md="6" :lg="8" :xl="5">
        <a-menu
            theme="dark"
            mode="horizontal"
            :style="{ lineHeight: '6rem' }"
        >
          <a-menu-item key="/admin/user" :style="user.id?{}:{display:'none'}">
            <router-link to="/admin/user">用户管理</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/memberinfo" :style="user.id?{}:{display:'none'}">
            <router-link to="/admin/memberinfo" :style="user.id?{}:{display:'none'}">团队人员管理</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/homeCategory" :style="user.id?{}:{display:'none'}">
            <router-link to="/admin/homeCategory">主页分类管理</router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :xs="{span: 0}" :sm="4" :md="6" :lg="8" :xl="6">
        <a-popconfirm
            title="确认退出登录?"
            ok-text="是"
            cancel-text="否"
            @confirm="logout()"
        >
          <a class="logout-menu" v-show="user.id">
            <span>退出登录</span>
          </a>
        </a-popconfirm>

        <a class="welcome-menu" v-show="user.id">
          <span>欢迎管理员{{user.name}}</span>
        </a>

        <a class="login-menu" @click="showLoginModal" v-show="!user.id">
          <span>登录</span>
        </a>
<!--        <a class="return-menu" v-show="!user.id">-->
<!--          <router-link to="/" class="return-menu">返回首页</router-link>-->
<!--        </a>-->

      </a-col>
    </a-row>


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
import {computed, defineComponent, ref} from 'vue';
import validate = WebAssembly.validate;
import _default from "ant-design-vue/es/color-picker";
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'the-header',
  setup(){
    //登录后保存
    const user = computed(() => {return store.state.user;});

    //用来登录
    const loginUser = ref({
      loginName: "ts",
      password: "test123"
    });

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    //登录
    const login = () => {
      // console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if(data.success){
          loginModalVisible.value = false;
          message.success('登录成功');
          store.commit("setUser", data.content);
        } else{
          message.error(data.message);
        }
      });
    };

    //退出登录
    const logout = () => {
      // console.log("退出登录开始");
      axios.get("/user/logout/"+user.value.token).then((response) => {
        const data = response.data;
        if(data.success){
          message.success('退出登录成功');
          // user.value = data.content;
          store.commit("setUser", {});
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
      user,
      logout,
    }
  }
});
</script>

<style scoped>
.login-menu{
  color: white;
  float: right;
  padding-left: 0.5rem;
}
.return-menu{
  color: white;
  float: right;
}
.logout-menu{
  color: white;
  float: right;
  padding-left: 0.5rem!important;
}
.welcome-menu{
  color: white;
  float: right;
}
.college{
  color: #ffffff;
}
</style>

<style scoped>
.ant-avatar{
  width: 50px;
  height: 100%!important;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
  vertical-align: middle!important;
}
</style>
