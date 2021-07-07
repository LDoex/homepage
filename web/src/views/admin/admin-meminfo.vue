<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-button type="primary" @click="add" size="large">
        新增
      </a-button>
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
  >
    <a-form :model="memberItem" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="封面">
        <a-input v-model:value="memberItem.cover" />
      </a-form-item>
      <a-form-item label="姓名">
        <a-input v-model:value="memberItem.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-select v-model:value="memberItem.category1Id" placeholder="please select your zone">
          <a-select-option value="1">1</a-select-option>
          <a-select-option value="2">2</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="人员类别">
        <a-select v-model:value="memberItem.category2Id" placeholder="please select your zone">
          <a-select-option value="3">3</a-select-option>
          <a-select-option value="4">4</a-select-option>
        </a-select>
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

    const memberItem = ref({});
    const memList = ref();
    memList.value = [];
    
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0
    });

    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/memberinfo/list", {
        params: {
          page: params.page,
          size: params.size
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

    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    // ------表单-------
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      axios.post("/memberinfo/save", memberItem.value).then((response)=>{
        loading.value = false;
        const data = response.data;
        if(data.success) {
          modalVisible.value = false;
          modalLoading.value = false;

          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    }

    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      memberItem.value = record;
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




    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    })

    return {
      memList,
      pagination,
      actions,
      columns,
      loading,
      handleTableChange,

      edit,
      add,

      modalLoading,
      modalVisible,
      handleModalOk,
      memberItem,

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