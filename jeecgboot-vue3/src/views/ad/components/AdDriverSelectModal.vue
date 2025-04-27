<template>
    <div>
      <!-- 这里可以添加司机查询表单 -->
      <BasicForm @register="registerForm"></BasicForm>
      <!-- 这里可以添加司机列表 -->
      <BasicTable @register="registerTable"></BasicTable>
      <div style="text-align: right">
        <a-button @click="onSelectOk">确认</a-button>
        <a-button @click="onCancel">取消</a-button>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicTable, useTable } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  
  const [registerForm, { getFieldsValue }] = useForm({
    // 配置查询表单
  });
  
  const [registerTable, { getSelectedRows, getSelectedRowKeys }] = useTable({
    // 配置司机列表
  });
  
  const [, { closeModal }] = useModal();
  
  // 确认选择事件
  function onSelectOk() {
    const selectRows = getSelectedRows();
    const selectKeys = getSelectedRowKeys();
    // 触发自定义事件，将选中的司机信息传递出去
    const event = new CustomEvent('getSelectResult', {
      detail: { selectRows, selectKeys },
    });
    window.dispatchEvent(event);
    closeModal();
  }
  
  // 取消选择事件
  function onCancel() {
    closeModal();
  }
  </script>