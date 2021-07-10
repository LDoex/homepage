<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
          <a-form
              layout="inline"
          >
            <a-form-item>
              <a-button type="primary" @click="handleQuery()">
                刷新列表
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handleProfileView()">
                简介编辑
              </a-button>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="add()" >
                新增类别
              </a-button>
            </a-form-item>
          </a-form>

        </p>
          <a-table
              v-if="level1.length>0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{record.name}}
            </template>
            <template v-slot:action="{text, record}">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
          <br/>
          <a-table
              :columns="otherColumns"
              :row-key="record => record.id"
              :data-source="otherList"
              :loading="otherLoading"
              :pagination="false"
              size="small"
          >
            <template #otherName="{ text, record }">
              简介及页脚信息
            </template>
            <template v-slot:otherAction="{text, record}">
              <a-space size="small">
                <a-button type="primary" @click="otherEdit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="16">
          <a-form
              layout="vertical"
              v-show="isProfile"
          >
            <a-form-item>
              <a-button type="primary" >
                编辑新增
              </a-button>
            </a-form-item>
            <a-form-item label="个人简介">
              <div id="profileEditor"></div>
            </a-form-item>
            <a-form-item label="页脚信息">
              <div id="footerEditor"></div>
            </a-form-item>
          </a-form>
          <p>
            <a-form
                layout="inline"
                v-show="!isProfile"
            >
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="docItem" layout="vertical" v-show="!isProfile">
            <a-form-item>
              <a-input v-model:value="docItem.name" placeholder="请输入名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="docItem.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择分类"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="docItem.sort" placeholder="请输入顺序编号"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary">
                <EyeOutlined /> 预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="contentEditor"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import {createVNode, defineComponent,ref,onMounted } from 'vue';
import axios from 'axios';
import {message, Modal} from 'ant-design-vue';
import {Tool} from '@/util/tool';
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import E from "wangeditor";


export default defineComponent({
  setup() {

    const isProfile = ref(false);
    const editor = new E("#contentEditor");
    const profileEditor = new E("#profileEditor");
    const footerEditor = new E("#footerEditor");

    //文本框置底，允许上传图片
    editor.config.zIndex = 0;
    editor.config.uploadImgShowBase64 = true;

    //处理简介编辑显示
    const handleProfileView = () => {
      isProfile.value = true;
    }
    //处理简介编辑显示
    const handleProfileSave = () => {
      isProfile.value = false;
    }

    const route = useRoute();
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        slots: { customRender: 'name' },
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'action' },
      },
    ];
    const otherColumns = [
      {
        title: '名称',
        dataIndex: 'id',
        key: 'id',
        slots: { customRender: 'otherName' },
      },
      {
        title: '操作',
        key: 'action',
        slots: { customRender: 'otherAction' },
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



    const docItem = ref();
    docItem.value = {};
    const docList = ref();
    docList.value = [];

    const otherList = ref();
    otherList.value = [];
    
    const loading = ref(false);
    const otherLoading = ref(false);


    /**
     * 查询other分类
     **/
    const handleOtherQuery = () => {
      otherLoading.value = true;
      //如果不清空现有数据，则编辑保存重新加载数据后，再次编辑，则列表显示的还是编辑前的数据
      otherList.value = [];
      axios.get("/others/all/"+route.query.outCateId).then((response)=>{
        otherLoading.value = false;
        const data = response.data;
        if(data.success){
          otherList.value = data.content;
          console.log("otherList",otherList.value);
        } else{
          message.error(data.message);
        }

      });
    };
    /**
     * 查询other内容
     **/
    const handleQueryOtherContent = () => {
      otherLoading.value = true;
      axios.get("/others/find-content/"+route.query.outCateId).then((response)=>{
        otherLoading.value = false;
        const data = response.data;
        if(data.success){
          profileEditor.txt.html(data.content[0].content);
          footerEditor.txt.html(data.content[0].footer);
        } else{
          message.error(data.message);
        }

      });
    };

    /**
     * 数据查询 handleQuery相当于一个对象实例
     **/
    const handleQuery = () => {
      loading.value = true;
      //如果不清空现有数据，则编辑保存重新加载数据后，再次编辑，则列表显示的还是编辑前的数据
      docList.value = [];
      axios.get("/doc/all/"+route.query.outCateId).then((response)=>{
        loading.value = false;
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


    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    // ------表单-------
    const modalLoading = ref(false);
    const handleSave = () => {
      modalLoading.value = true;
      docItem.value.content = editor.txt.html();
      axios.post("/doc/save", docItem.value).then((response)=>{
        modalLoading.value = false;
        const data = response.data;
        if(data.success) {

          message.success("保存成功");
          //清空
          editor.txt.html("");
          docItem.value = {}
          //重新加载列表
          handleQuery();
        } else{
          message.error(data.message);
        }
      });
    }

    /**
     * 递归将某节点及其子孙节点全部置为disabled
     * */
    const treeSelectData = ref();
    treeSelectData.value = [];
    const setDisable = (treeSelectData: any, id: any) => {
      //console.log("treeSelecData结构：",treeSelectData, id);
      for(let i = 0; i<treeSelectData.length; i++){
        const node = treeSelectData[i];
        if(node.id === id){
          //如果当前节点就是目标节点
          //将目标节点设置为disabled
          node.disabled = true;

          //遍历所有子节点，将所有子节点全部加上disabled
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0; j<children.length; j++){
              setDisable(children, children[j].id);
            }
          }
        }
        else{
          //如果当前节点不是目标节点，则到其子节点再找
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            setDisable(children, id);
          }
        }
      }
    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/"+docItem.value.id).then((response) => {
        const data = response.data;

        if(data.success){
          editor.txt.html(data.content);
        } else{
          message.error(data.message);
        }
      });
    };


    /**
     * 编辑
     * @param record
     */
    const edit = (record: any) => {
      isProfile.value = false;
      //清空富文本框
      editor.txt.html("");
      docItem.value = Tool.copy(record);
      handleQueryContent();

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      //为树选择添加一个”无“
      treeSelectData.value.unshift({id: 0, name: '无'});
    }

    /**
     * 新增
     */
    const add = () => {
      isProfile.value = false;
      //清空富文本框
      editor.txt.html("");
      docItem.value = {
        outcateId: route.query.outCateId,
      };

      treeSelectData.value = Tool.copy(level1.value);
      //为树选择添加一个”无“
      treeSelectData.value.unshift({id: 0, name: '无'});

    }

    /**
     * 递归将某节点及其子孙节点全部删除
     * */
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      //console.log("treeSelecData结构：",treeSelectData, id);
      for(let i = 0; i<treeSelectData.length; i++){
        const node = treeSelectData[i];
        if(node.id === id){
          //如果当前节点就是目标节点
          //将目标节点加入ids
          ids.push(node.id);

          //遍历所有子节点，将所有子节点全部push进ids
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            for(let j=0; j<children.length; j++){
              getDeleteIds(children, children[j].id);
            }
          }
        }
        else{
          //如果当前节点不是目标节点，则到其子节点再找
          const children = node.children;
          if(Tool.isNotEmpty(children)){
            getDeleteIds(children, id);
          }
        }
      }
    };


    const handleDelete = (id: any) => {
      // 清空数组，否则多次删除时，数组会一直增加
      ids.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        okText: "确认",
        cancelText: "取消",
        title: '确定删除?',
        icon: createVNode(ExclamationCircleOutlined),
        content: createVNode('div', { style: 'color:red;' }, '确定删除该节点下所有内容？'),
        onOk() {
          axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
            const data = response.data;
            if (data.success) {
              //重新加载列表
              handleQuery();
            }
          });
        },
        onCancel() {
          console.log('Cancel');
        },
        class: 'test',
      });
      };

    /**
     * 编辑简介
     * @param record
     */
    const otherEdit = (record: any) => {
      isProfile.value = true;
      //清空富文本框
      profileEditor.txt.html("");
      footerEditor.txt.html("");
      docItem.value = Tool.copy(record);
      handleQueryOtherContent();

    };

    /**
     * 新增简介
     */
    const otherAdd = () => {
      isProfile.value = false;
      //清空富文本框
      editor.txt.html("");
      docItem.value = {
        outcateId: route.query.outCateId,
      };
    }





    onMounted(()=>{
      handleQuery();
      handleOtherQuery();
      editor.create();
      profileEditor.create();
      footerEditor.create();
      setTimeout(()=>{
        add()
      }, 100);
    });

    return {
      level1,
      actions,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      modalLoading,
      handleSave,
      docItem,

      handleDelete,
      treeSelectData,

      handleProfileView,
      isProfile,

      otherLoading,
      otherColumns,
      otherList,
      otherEdit,


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