<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="param"
        >
          <a-form-item>
            <a-input-search
                v-model:value="param.name"
                placeholder="输入名字查询"
                enter-button
                @search="handleQuery({page: 1, size: pagination.pageSize})"
            />
          </a-form-item>
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
          :data-source="memList"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" height="80" width="50"/>
        </template>
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <router-link to="/admin/doc">
            <a-button type="primary">
              主页设置
            </a-button>
            </router-link>
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
        <template #category1="{ text, record }">
          {{getCategoryName(record.category1Id)}}
        </template>
        <template #tags="{ text: category2Id }">
          <a-tag
              :color="getCategoryName(category2Id) === '教师' ? 'pink'
              :getCategoryName(category2Id) === '博士生'? 'blue'
              :getCategoryName(category2Id) === '硕士生'? 'green' :'geekblue'"
          >
            {{ getCategoryName(category2Id) }}
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
    <a-form :model="memberItem">
      <a-form-item label="封面">
        <a-input v-model:value="memberItem.cover" />
      </a-form-item>
      <a-form-item label="姓名">
        <a-input v-model:value="memberItem.name" />
      </a-form-item>
      <a-form-item label="姓名">
        <a-input v-model:value="memberItem.name" />
      </a-form-item>
      <a-form-item label="分类">
          <a-cascader
              v-model:value="homeCategoryIds"
              :options="level1"
              :field-names="{label:'name', value:'id', children:'children'}"
              placeholder="Please select" />
      </a-form-item>
      <a-form-item label="邮箱">
        <a-input v-model:value="memberItem.email" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="memberItem.description" />
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
        title: '封面',
        dataIndex: 'cover',
        key: 'cover',
        slots: {customRender: 'cover' },
      },
      {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '邮箱',
        key: 'email',
        dataIndex: 'email',
      },
      {
        title: '分类',
        dataIndex: 'category1Id',
        slots: {customRender: 'category1' },
      },
      {
        title: '人员类别',
        key: 'category2Id',
        dataIndex: 'category2Id',
        slots: { customRender: 'tags' },
      },
      {
        title: '点击量',
        dataIndex: 'viewCount',
        key: 'viewCount',
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

    const homeCategoryIds = ref();
    const param = ref();
    param.value = {};

    const memberItem = ref();
    const memList = ref();
    memList.value = [];
    memberItem.value = {};
    
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0
    });


    let homeCategoryList: any;

    /**
     * 分类数据查询
     **/
    const handleCategoryQuery = () => {
      loading.value = true;
      axios.get("/homeCategory/all").then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          homeCategoryList = data.content;
          level1.value = Tool.array2Tree(homeCategoryList, 0);

          //解决axios异步问题，加载完分类后再加载页面，否则getCategoryName会出现空指针错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else{
          message.error(data.message);
        }

      });
    }

    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/memberinfo/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          memList.value = data.content.list;

          //重置分页
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else{
          message.error(data.message);
        }

      });
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };


    // ------表单-------
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      memberItem.value.category1Id = homeCategoryIds.value[0];
      memberItem.value.category2Id = homeCategoryIds.value[1];
      axios.post("/memberinfo/save", memberItem.value).then((response)=>{
        modalLoading.value = false;
        const data = response.data;
        if(data.success) {
          modalVisible.value = false;

          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
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
      memberItem.value = Tool.copy(record);
      homeCategoryIds.value = [memberItem.value.category1Id, memberItem.value.category2Id];
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      memberItem.value = {};

    }

    const handleDelete = (id: any) => {
      axios.delete("/memberinfo/delete/"+id).then((response)=>{
        const data = response.data;
        if(data.success) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };

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




    onMounted(()=>{
      handleCategoryQuery();
    });

    return {
      memList,
      param,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,

      modalLoading,
      modalVisible,
      handleModalOk,
      memberItem,

      handleDelete,
      homeCategoryIds,
      level1,
      getCategoryName,

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