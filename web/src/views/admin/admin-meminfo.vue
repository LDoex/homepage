<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="memList"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #name="{ text: name }">
          <a>{{ name }}</a>
        </template>
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" height="80" width="50"/>
        </template>
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
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
</template>

<script lang="ts">
import { defineComponent,ref,onMounted } from 'vue';
import axios from 'axios';



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
        title: '阅读数',
        dataIndex: 'viewCount',
        key: 'viewCount',
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];

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
        } else{
          memList.value = data.message;
        }
        //重置分页
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
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
      handleTableChange,

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