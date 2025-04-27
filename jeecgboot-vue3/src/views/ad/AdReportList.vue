<template>
  <div>
    <!--引用表格-->
   <BasicTable @register="registerTable" :rowSelection="rowSelection">
     <!--插槽:table标题-->
      <template #tableTitle>
          <a-button type="primary" v-auth="'ad:ad_report:add'" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
          <a-button  type="primary" v-auth="'ad:ad_report:exportXls'" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
          <j-upload-button type="primary" v-auth="'ad:ad_report:importExcel'" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
              <template #overlay>
                <a-menu>
                  <a-menu-item key="1" @click="batchHandleDelete">
                    <Icon icon="ant-design:delete-outlined"></Icon>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
              <a-button v-auth="'ad:ad_report:deleteBatch'">批量操作
                <Icon icon="mdi:chevron-down"></Icon>
              </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
      </template>
       <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)"/>
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell>
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <AdReportModal @register="registerModal" @success="handleSuccess"></AdReportModal>
    <!-- 处理弹窗 -->
    <AdReportProcessModal @register="registerProcessModal" @success="handleSuccess"></AdReportProcessModal>
  </div>
</template>

<script lang="ts" name="ad-adReport" setup>
  import {ref, reactive, computed, unref} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import {useModal} from '/@/components/Modal';
  import { useListPage } from '/@/hooks/system/useListPage'
  import AdReportModal from './components/AdReportModal.vue'
  import AdReportProcessModal from './components/AdReportProcessModal.vue'
  import {columns, searchFormSchema, superQuerySchema} from './AdReport.data';
  import {list, deleteOne, batchDelete, getImportUrl,getExportUrl} from './AdReport.api';
  import { downloadFile } from '/@/utils/common/renderUtils';
  import { useUserStore } from '/@/store/modules/user';
  const queryParam = reactive<any>({});
  const checkedKeys = ref<Array<string | number>>([]);
  const userStore = useUserStore();
  //注册model
  const [registerModal, {openModal}] = useModal();
  //注册处理model
  const [registerProcessModal, {openModal: openProcessModal}] = useModal();
  
  // 表格操作列配置
  const actionColumn = {
    width: 120,
    title: '操作',
    dataIndex: 'action',
    fixed: 'right' as const,
    slots: { customTitle: 'action' },
    actions: [
      {
        label: '处理',
        onClick: handleProcess,
        auth: 'ad:ad_transaction:reportProcess',
        show: (record) => {
          console.log('记录状态:', record.status);
          return record.status === 0 || record.status === '0';
        },
      },
    ],
  };
  
  //注册table数据
  const { prefixCls,tableContext,onExportXls,onImportXls } = useListPage({
      tableProps:{
           title: '广告上报记录表',
           api: list,
           columns,
           canResize:false,
           formConfig: {
              //labelWidth: 120,
              schemas: searchFormSchema,
              autoSubmitOnEnter:true,
              showAdvancedButton:true,
              fieldMapToNumber: [
              ],
              fieldMapToTime: [
              ],
            },
           actionColumn: {
             width: 120,
             title: '操作',
             dataIndex: 'action',
             fixed: 'right' as const,
             slots: { customTitle: 'action' },
           },
            beforeFetch: (params) => {
              return Object.assign(params, queryParam);
            },
      },
       exportConfig: {
            name:"广告上报记录表",
            url: getExportUrl,
            params: queryParam,
          },
          importConfig: {
            url: getImportUrl,
            success: handleSuccess
          },
  })

  const [registerTable, {reload},{ rowSelection, selectedRowKeys }] = tableContext

  // 高级查询配置
  const superQueryConfig = reactive(superQuerySchema);

  /**
   * 高级查询事件
   */
  function handleSuperQuery(params) {
    Object.keys(params).map((k) => {
      queryParam[k] = params[k];
    });
    reload();
  }
   /**
    * 新增事件
    */
  function handleAdd() {
     openModal(true, {
       isUpdate: false,
       showFooter: true,
     });
  }
   /**
    * 编辑事件
    */
  function handleEdit(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: true,
     });
   }
   /**
    * 详情
   */
  function handleDetail(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: false,
     });
   }
   /**
    * 处理事件
    */
  function handleProcess(record: Recordable) {
    openProcessModal(true, {
      record,
      isUpdate: true,
    });
  }
   /**
    * 删除事件
    */
  async function handleDelete(record) {
     await deleteOne({id: record.id}, handleSuccess);
   }
   /**
    * 批量删除事件
    */
  async function batchHandleDelete() {
     await batchDelete({ids: selectedRowKeys.value}, handleSuccess);
   }
   /**
    * 成功回调
    */
  function handleSuccess() {
      (selectedRowKeys.value = []) && reload();
   }
   /**
      * 操作栏
      */
  function getTableAction(record){
       return [
         {
           label: '编辑',
           onClick: handleEdit.bind(null, record),
           auth: 'ad:ad_report:edit'
         }
       ]
   }
     /**
        * 下拉操作栏
        */
  function getDropDownAction(record){
       return [
         {
           label: '详情',
           onClick: handleDetail.bind(null, record),
         },
         {
           label: '处理',
           onClick: handleProcess.bind(null, record),
           auth: 'ad:ad_transaction:reportProcess',
           show: () => {
             console.log('记录状态:', record.status);
             return record.status === 0 || record.status === '0';
           }
         }
         ,{
           label: '删除',
           popConfirm: {
             title: '是否确认删除',
             confirm: handleDelete.bind(null, record),
             placement: 'topLeft',
           },
           auth: 'ad:ad_report:delete'
         }
       ]
   }

</script>

<style lang="less" scoped>
  :deep(.ant-picker),:deep(.ant-input-number){
    width: 100%;
  }
</style>