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
                  @click="handleTitleClick(item.name, c.id)">
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
                <a :href="item.href">{{ item.name }}</a>
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
    const pagination = {
      pageSize: 3,
    };

    const isHtml = ref(true);
    const list = ref();

    const level1 = ref();
    level1.value = [];
    let homeCategoryList: any;

    /**
     * 分类数据查询
     **/
    const handleCategoryQuery = () => {
      axios.get("/homeCategory/all").then((response)=>{
        const data = response.data;
        if(data.success){
          homeCategoryList = data.content;
          level1.value = Tool.array2Tree(homeCategoryList, 0);
          console.log("树形",level1.value);

        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 处理html和团队成员列表互斥显示
     */
    const handleTitleClick = (titleName: any, id: any) => {
      if(titleName === "成员"){
        isHtml.value = false;
      } else {
        isHtml.value = true;
      }
    };



    onMounted(()=>{
      handleCategoryQuery();
      axios.get("/memberinfo/list", {
        params: {
          page: 1,
          size: 500
        }
        }).then((response)=>{
        const data = response.data;
        if(data.success){
          list.value = data.content.list;
        } else{
          list.value = data.message;
        }
      });
    })
    return {
      list,
      pagination,
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