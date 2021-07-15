<template>
  <a-layout>
    <a-layout-sider
        width="280"
        style="background: #fff"
        collapsed-width="0"
        breakpoint='xl'
        :trigger="null"
        @breakpoint="onBreakpoint"
    >
      <a-anchor
          v-show="!drawHandleVisible"
          :bounds="1000"
          >
        <a-anchor-link v-for="c in level1" :key="c.id" :href="'#' + c.id" :title="c.name">
          <a-divider />
        </a-anchor-link>
      </a-anchor>
      <div v-if="!drawHandleVisible" :innerHTML="footer" style="
      margin-bottom: 30%;
      float: left;
      width: 10em;
"></div>
    </a-layout-sider>

    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-drawer
          :placement="placement"
          :closable="false"
          :visible="visible"
          :width="150"
          @close="onClose"
      >
        <template v-slot:handle>
          <div v-show="drawHandleVisible" class="handle" @click="visible = !visible">
            <a-icon :type="visible ? 'close' : 'setting'"></a-icon>
          </div>
        </template>
        <div style="margin-left: -65px" id="myanchor">
          <a-anchor
              :bounds="10"
              :affix="false"
              :show-ink-in-fixed="false"
              >
            <a-anchor-link v-for="c in level1" :key="c.id" :href="'#' + c.id" :title="c.name" />
          </a-anchor>
        </div>
        <div v-if="drawHandleVisible" :innerHTML="footer" style="
      width: 10em;
      margin-left: -50%;
"></div>
      </a-drawer>

    <a-row type="flex" :gutter="24" align="middle">
      <a-col
          :xs="{ span: 2 }" :lg="{ span: 4, offset: 4}"
      >
        <div class="avaterContent">
          <a-avatar  shape="square" :size="{ xs: 80, sm: 80, md: 80, lg: 160, xl: 160, xxl: 160 }" :src="imageurl"/>
        </div>
      </a-col>

      <a-col
          :xs="{ span: 18, offset: 4}" :lg="{ span: 10, offset: 0}"
      >
        <div :innerHTML="profile" id="Home"></div>
      </a-col>
    </a-row>
      <br/>
    <a-row>
      <a-form
          layout="vertical"
          :model="contents"
      >
        <a-form-item v-for="c in contents" :key="c.id" :id="c.id">
          <div :key="c.id" :id="c.id" style="font-size: x-large">{{c.name}}</div>
          <a-divider/>
          <div class="wangeditor" :key="c.id" :innerHTML="c.content"></div>
        </a-form-item>
      </a-form>
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
          handleQueryImage();
          handleQueryContent();

        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 内容查询
     **/
    let i: any = 0;
    const ids: Array<string> = [];
    const handleQueryContent = () => {


      axios.get("/doc/find-contents/"+ids.join(',')).then((response) => {
        const data = response.data;
        if(data.success){
          contents.value = data.content;

          i = 0;
          for (let contentsKey in contents.value) {
            contents.value[contentsKey].name = docList.value[i].name;
            i+=1;
          }
        } else{
          message.error(data.message);
        }
      });
    };

    const visible = ref<boolean>(false);
    const drawHandleVisible = ref(false);
    const placement = ref<string>('left');

    const showDrawer = () => {
      visible.value = true;
    };

    const onClose = () => {
      visible.value = false;
    };


    const onBreakpoint = (broken: any) => {
      drawHandleVisible.value = broken;
      console.log(drawHandleVisible);
    };

    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const imageurl = ref("");
    const handleQueryImage = () => {
      axios.get("/memberinfo/list", {
        params: {
          page: 1,
          size: 500,
          id: docList.value[0].outcateId,
        }
      }).then((response)=>{
        const data = response.data;
        if(data.success){
          imageurl.value = data.content.list[0].cover;
          console.log(imageurl.value);
        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 查询other内容
     **/
    const profile = ref();
    const footer = ref();
    const handleQueryOtherContent = () => {
      axios.get("/others/find-content/"+route.query.meminfoId).then((response)=>{
        const data = response.data;
        if(data.success){
          profile.value = data.content[0].content;
          footer.value = data.content[0].footer;
        } else{
          message.error(data.message);
        }

      });
    };
    onMounted(()=>{
      handleQuery();
      handleQueryOtherContent();
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
      placement,

      imageurl,

      profile,
      footer,
    };
  },
});
</script>

<style>
 .ant-anchor-wrapper {
   margin-left: -25px!important;
   padding-left: 26px!important;
   overflow: auto;
   /*background-color: transparent;*/
   background-color: white!important;
 }
.ant-anchor{
  margin: 15px!important;
  padding: 15px!important;
  color: rgba(0, 0, 0, 0.85);
  font-family: "Times New Roman";
  font-size: 18px!important;
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
   left: 145px;
   width: 40px;
   height: 40px;
   background-color: #5e5e5e;
   color: white;
   font-size: 20px;
   text-align: center;
   line-height: 40px;
   border-radius: 5px;
 }
 .handle:hover {
   cursor: pointer;
 }

 /* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
 /* table 样式 */
 .wangeditor table {
   border-top: 1px solid #ccc;
   border-left: 1px solid #ccc;
 }
 .wangeditor table td,
 .wangeditor table th {
   border-bottom: 1px solid #ccc;
   border-right: 1px solid #ccc;
   padding: 3px 5px;
 }
 .wangeditor table th {
   border-bottom: 2px solid #ccc;
   text-align: center;
 }

 /* blockquote 样式 */
 .wangeditor blockquote {
   display: block;
   border-left: 8px solid #d0e5f2;
   padding: 5px 10px;
   margin: 10px 0;
   line-height: 1.4;
   font-size: 100%;
   background-color: #f1f1f1;
 }

 /* code 样式 */
 .wangeditor code {
   display: inline-block;
   *display: inline;
   *zoom: 1;
   background-color: #f1f1f1;
   border-radius: 3px;
   padding: 3px 5px;
   margin: 0 3px;
 }
 .wangeditor pre code {
   display: block;
 }

 /* ul ol 样式 */
 .wangeditor ul, ol {
   margin: 10px 0 10px 20px;
 }

 /* 和antdv p,h1,h2,h3冲突，覆盖掉 */
 .wangeditor blockquote p, h1, h2, h3 {
   margin: 20px 20px !important;
   font-weight:600;
 }

 /* 点赞 */
 .vote-div {
   padding: 15px;
   text-align: center;
 }

 /* 图片自适应 */
 .wangeditor img {
   max-width: 100%;
   height: auto;
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

