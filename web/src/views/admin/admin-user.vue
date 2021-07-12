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
          :data-source="userList"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              重置
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
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="重置"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
      okText="确认"
      cancelText="取消"
  >
    <a-form :model="userItem" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="userItem.loginName" :disabled="!!userItem.id"/>
      </a-form-item>
      <a-form-item label="姓名">
        <a-input v-model:value="userItem.name" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input-password v-model:value="userItem.password" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent,ref,onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from '@/util/tool';

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  setup() {

    const columns = [
      {
        title: '登陆名',
        dataIndex: 'loginName'
      },
      {
        title: '姓名',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' }
      }
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
    
    const param = ref();
    param.value = {};

    const userItem = ref();
    const userList = ref();
    userList.value = [];
    userItem.value = {};
    
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });




    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success){
          userList.value = data.content.list;

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
      userItem.value.password = hexMd5(userItem.value.password + KEY);
      axios.post("/user/save", userItem.value).then((response)=>{
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
      userItem.value = Tool.copy(record);
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      userItem.value = {};

    }

    const handleDelete = (id: any) => {
      axios.delete("/user/delete/"+id).then((response)=>{
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





    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });

    return {
      userList,
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
      userItem,

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