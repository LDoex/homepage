<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
        >
          <a-form-item>
            <a-button type="primary" @click="add()" >
              新增
            </a-button>
          </a-form-item>
        </a-form>

      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" height="80" width="50"/>
        </template>
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
        <template #tags="{ text: category2Id }">
          <a-tag
              :color="category2Id === 'loser' ? 'volcano' : 'green'"
          >
            {{ category2Id }}
          </a-tag>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="编辑"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
      okText="确认"
      cancelText="取消"
  >
    <a-form :model="homeCategoryItem">
      <a-form-item label="姓名">
        <a-input v-model:value="homeCategoryItem.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select v-model:value="homeCategoryItem.parent" placeholder="please select your zone">
          <a-select-option value="0">无</a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="homeCategoryItem.id === c.id">{{c.name}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="homeCategoryItem.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent,ref,onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from '@/util/tool';


export default defineComponent({
  setup() {

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        key: 'sort',
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];

    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id:"",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     **/
    const level1 = ref();
    level1.value = [];
    const levelList = ref();
    levelList.value = [];



    const homeCategoryItem = ref({});
    const memList = ref();
    memList.value = [];
    
    const loading = ref(false);


    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const handleQuery = () => {
      loading.value = true;
      //如果不清空现有数据，则编辑保存重新加载数据后，再次编辑，则列表显示的还是编辑前的数据
      memList.value = [];
      axios.get("/homeCategory/all").then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          memList.value = data.content;
          console.log("原始数组", memList.value);
          level1.value = Tool.array2Tree(memList.value, 0);
          console.log("树形数组", level1.value);
          levelList.value = Tool.copy(level1);

        } else{
          message.error(data.message);
        }

      });
    }


    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    // ------表单-------
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      axios.post("/homeCategory/save", homeCategoryItem.value).then((response)=>{
        modalLoading.value = false;
        const data = response.data;
        if(data.success) {
          modalVisible.value = false;

          //重新加载列表
          handleQuery();
        } else{
          message.error(data.message);
        }
      });
    }

    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      homeCategoryItem.value = Tool.copy(record);
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      homeCategoryItem.value = {};

    }

    const handleDelete = (id: any) => {
      axios.delete("/homeCategory/delete/"+id).then((response)=>{
        const data = response.data;
        if(data.success) {
          //重新加载列表
          handleQuery();
        }
      });
    };




    onMounted(()=>{
      handleQuery();
    })

    return {
      level1,
      actions,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      modalLoading,
      modalVisible,
      handleModalOk,
      homeCategoryItem,

      handleDelete,

    };
  },
});
</script>

<style scoped>
.ant-avatar{
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>