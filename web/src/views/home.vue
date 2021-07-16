<template>
  <a-layout>
    <a-layout-sider
        width="200"
        style="background: #fff"
        collapsed-width="0"
        breakpoint='xl'>
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleTitleClick"
      >
        <template v-for="item in level1" :key="item.id">
          <template v-if="item.children">
            <a-sub-menu
                :key="item.id"
            >
              <template #title>
              <span>
                <user-outlined />
                {{item.name}}
              </span>
              </template>
              <a-menu-item
                  v-for="c in item.children"
                  :key="c.id"
              >
                <template #icon>
                  <PieChartOutlined />
                </template>
                {{ c.name }}
              </a-menu-item>
            </a-sub-menu>
          </template>
          <template v-else>
            <a-menu-item :key="item.id">
              <template #icon>
                <PieChartOutlined />
              </template>
              {{ item.name }}
            </a-menu-item>
          </template>
        </template>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="htmlLayout" v-show="isHtml">
        <h1>html</h1>
      </div>
      <a-list v-show="!isHtml" item-layout="vertical" size="large" :data-source="list" :grid="{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 3, xxl: 3}">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span class="email">
            <component v-bind:is="'MailOutlined'" style="margin-right: 8px" />
            {{ item.email }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?meminfoId=' + item.id">
                {{item.name}}
                </router-link>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent,ref,onMounted } from 'vue';
import axios from 'axios';
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";


export default defineComponent({
  setup() {

    const isHtml = ref(true);
    const list = ref();

    const level1 = ref();
    level1.value = [];
    let homeCategoryList: any;

    /**
     * 根据id查分类名称
     **/
    const getCategoryName = (cid: any) => {
      //console.log(cid)
      let result = "";
      homeCategoryList.forEach((item:any)=>{
        if(item.id === cid){
          //return item.name; //直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    /**
     * 根据子id查父id
     **/
    const getParentId = (cid: any) => {
      // console.log(cid)
      let result = "";
      homeCategoryList.forEach((item:any)=>{
        if(item.id === cid){
          //return item.name; //直接return不起作用
          result = item.parent;
        }
      });
      return result;
    };

    /**
     * 根据子id查父分类名称
     **/
    const getParentName = (cid: any) => {
      //console.log(cid)
      let result = "";
      // console.log("pid", getParentId(cid));
      result = getCategoryName(getParentId(cid));
      return result;
    };

    /**
     * 列表数据查询
     **/
    const handleQuery = () => {
      axios.get("/memberinfo/list", {
        params: {
          page: 1,
          size: 500,
          category2Id: category2Id,
        }
      }).then((response)=>{
        const data = response.data;
        if(data.success){
          list.value = data.content.list;
        } else{
          list.value = data.message;
        }
      });
    };

    /**
     * 分类数据查询
     **/
    const handleCategoryQuery = () => {
      axios.get("/homeCategory/all").then((response)=>{
        const data = response.data;
        if(data.success){
          homeCategoryList = data.content;
          level1.value = Tool.array2Tree(homeCategoryList, 0);
          // console.log("树形",level1.value);

          //加载完分类数据才加载列表数据
          handleQuery();

        } else{
          message.error(data.message);
        }

      });
    };

    let category2Id = 0;

    /**
     * 处理html和团队成员列表互斥显示
     */
    const handleTitleClick = (value: any) => {
      if(getParentName(value.key) === "成员"){
        isHtml.value = false;
        category2Id = value.key;
        handleQuery();
      } else {
        isHtml.value = true;
      }
    };



    onMounted(()=>{
      handleCategoryQuery();
    });

    return {
      list,
      level1,
      isHtml,
      handleTitleClick,
    };
  },
});
</script>

<style scoped>
.ant-avatar{
  width: 50px;
  height: 80px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}

.email{
  margin-left: 70px;
}
</style>