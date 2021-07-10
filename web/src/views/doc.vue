<template>
  <a-layout>
    <a-layout-sider
        width="200"
        style="background: #fff"
        collapsed-width="0"
        breakpoint='xl'
        @breakpoint="onBreakpoint"
    >
      <a-anchor
          :bounds="1000"
          :wrapper-style="{marginLeft:14}"
          @change="onChange">
        <a-anchor-link v-for="c in level1" :key="c.id" :href="'#' + c.id" :title="c.name" />
      </a-anchor>
    </a-layout-sider>
    <a-drawer
        title="Basic Drawer"
        :placement="placement"
        :closable="false"
        :visible="visible"
        @close="onClose"
    >
      <template v-slot:handle>
        <div v-show="drawHandleVisible" class="handle" @click="visible = !visible">
          <a-icon :type="visible ? 'close' : 'setting'"></a-icon>
        </div>
      </template>
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>
    </a-drawer>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
    <a-row :gutter="24">

      <a-col :span="20">
        <div class="wangeditor" >
          <div v-for="c in contents" :key="c.id" :id="c.id" :class="c.id" :innerHTML="c.content"></div>
        </div>
      </a-col>
    </a-row>
      <a-back-top />
    </a-layout-content>
  </a-layout>

</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import axios from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";

export default defineComponent({
  setup() {
    const onChange = (link: string) => {
      console.log('Anchor:OnChange', link);
    };
    const route = useRoute();
    const level1 = ref();
    level1.value = [];
    const docList = ref();
    docList.value = [];
    const contents = ref();
    contents.value = {};

    /**
     * 文档查询
     **/
    const handleQuery = () => {
      //如果不清空现有数据，则编辑保存重新加载数据后，再次编辑，则列表显示的还是编辑前的数据
      docList.value = [];
      axios.get("/doc/all/"+route.query.meminfoId).then((response)=>{
        const data = response.data;
        if(data.success){
          docList.value = data.content;
          console.log("原始数组", docList.value);
          level1.value = Tool.array2Tree(docList.value, 0);
          console.log("树形数组", level1.value);

          //从docList.value拿到key值，再用key作为索引查id
          for (let docListKey in docList.value) {
            // console.log("id:",docList.value);
            // console.log("id:",docListKey);
            ids.push(docList.value[docListKey].id);
          }

        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 内容查询
     **/
    const ids: Array<string> = [];
    const handleQueryContent = () => {


      axios.get("/doc/find-contents/"+ids.join(',')).then((response) => {
        const data = response.data;
        if(data.success){
          contents.value = data.content;
        } else{
          message.error(data.message);
        }
      });
    };

    const visible = ref<boolean>(false);
    const drawHandleVisible = ref(false);

    const showDrawer = () => {
      visible.value = true;
    };

    const onClose = () => {
      visible.value = false;
    };


    const onBreakpoint = (broken: any) => {
      drawHandleVisible.value = broken;
    };

    onMounted(()=>{
      handleQuery();
      setTimeout(() => {
        handleQueryContent();
      }, 100);
    });

    return {
      onChange,
      level1,
      handleQueryContent,
      contents,

      visible,
      showDrawer,
      onClose,
      onBreakpoint,
      drawHandleVisible,
    };
  },
});
</script>

<style>
 .ant-anchor-wrapper {
   margin-left: 14px!important;
   padding-left: 4px;
   overflow: auto;
   background-color: transparent;
 }
.ant-anchor{
  margin: 0;
  padding: 0;
  color: rgba(0, 0, 0, 0.85);
  font-size: 28px;
  font-variant: tabular-nums;
  line-height: 1.5715;
  list-style: none;
  font-feature-settings: 'tnum';
  position: relative;
  padding-left: 2px;
}

 .handle {
   position: absolute;
   top: 250px;
   right: 310px;
   width: 40px;
   height: 40px;
   background-color: #0000ff;
   color: white;
   font-size: 20px;
   text-align: center;
   line-height: 40px;
   border-radius: 5px;
 }
 .handle:hover {
   cursor: pointer;
 }
</style>

