<template>
  <a-row :gutter="24">
    <a-col :span="8">
      <a-anchor
          :affix="false"
          :bounds="100"
          :wrapper-class="anchor"
          @change="onChange">
        <a-anchor-link v-for="c in level1" :key="c.id" :href="'#' + c.id" :title="c.name" />
      </a-anchor>
    </a-col>
    <a-col :span="16">
      <div class="anchor">sdf</div>
      <a-button type="primary" @click="handleQueryContent" >
        查找
      </a-button>
    </a-col>
  </a-row>

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

        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 内容查询
     **/

    let id: any=68609776132689920;
    const handleQueryContent = () => {
      for (let docListItem in docList) {
        console.log(docListItem);
      }
      axios.get("/doc/find-contents/68607729803071488,68609776132689920").then((response) => {
        const data = response.data;
        if(data.success){
          message.success("chenggong");
        } else{
          message.error(data.message);
        }
      });
    };

    onMounted(()=>{
      handleQuery();
    });

    return {
      onChange,
      level1,
      handleQueryContent,
    };
  },
});
</script>

