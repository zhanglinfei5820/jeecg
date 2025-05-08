<template>
    <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose title="分发" :width="500" @ok="handleSubmit">
        <BasicForm @register="registerForm" name="AdPublishDetailDistributeForm" />
    </BasicModal>
  </template>
  
  <script lang="ts" setup>
      import {ref, computed, unref} from 'vue';
      import {BasicModal, useModalInner} from '/@/components/Modal';
      import {BasicForm, useForm} from '/@/components/Form/index';
      import {distribute} from '../AdPublishDetail.api';
      import { JDictSelectTag } from '/@/components/Form';
      // Emits声明
      const emit = defineEmits(['register','success']);
      
      //表单配置
      const [registerForm, { setProps,resetFields, setFieldsValue, validate, scrollToField }] = useForm({
          labelWidth: 100,
          schemas: [
              {
                  label: '抽成金额',
                  field: 'percentage',
                  component: 'InputNumber',
                  componentProps: {
                      min: 0,
                      max: 100,
                      precision: 2,
                      placeholder: '请输入抽成金额'
                  },
                  dynamicRules: ({model,schema}) => {
                      return [
                          { required: true, message: '请输入抽成金额!'},
                      ];
                  },
              },
              {
                  label: '审核状态',
                  field: 'status',
                  component: 'JDictSelectTag',
                  componentProps: {
                      dictCode: "ad_publish_detail_status",
                      placeholder: '请选择审核状态',
                      stringToNumber: true
                  },
                  dynamicRules: ({model,schema}) => {
                      return [
                          { required: true, message: '请选择审核状态!'},
                      ];
                  },
              },
              {
                  label: '录用司机数',
                  field: 'actualDrivers',
                  component: 'InputNumber',
                  componentProps: {
                      min: 0,
                      precision: 0,
                      placeholder: '请输入录用司机数'
                  },
                  dynamicRules: ({model,schema}) => {
                      return [
                          { required: true, message: '请输入录用司机数!'},
                      ];
                  },
              }
          ],
          showActionButtonGroup: false,
          baseColProps: {span: 24}
      });
  
      //表单赋值
      const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
          //重置表单
          await resetFields();
          setModalProps({confirmLoading: false});
          if (data?.record) {
              //表单赋值
              await setFieldsValue({
                  ...data.record,
              });
          }
          //保存原始记录数据
          recordData.value = data?.record;
      });
  
      //保存原始记录数据
      const recordData = ref<any>(null);
  
      //表单提交事件
      async function handleSubmit(v) {
          try {
              let values = await validate();
              setModalProps({confirmLoading: true});
              //合并原始记录数据和抽成比例
              const submitData = {
                  ...recordData.value,
                  percentage: values.percentage,
                  status: values.status,
                  actualDrivers:values.actualDrivers,
                  driverCount: values.driverCount
              };
              //提交表单
              await distribute(submitData);
              //关闭弹窗
              closeModal();
              //刷新列表
              emit('success');
          } catch (error: any) {
             if (error?.errorFields) {
               const firstField = error.errorFields[0];
               if (firstField) {
                 scrollToField(firstField.name, { behavior: 'smooth' });
               }
             }
             return Promise.reject(error);
          } finally {
              setModalProps({confirmLoading: false});
          }
      }
  </script>
  
  <style lang="less" scoped>
      /** 时间和数字输入框样式 */
    :deep(.ant-input-number) {
      width: 100%;
    }
  
    :deep(.ant-calendar-picker) {
      width: 100%;
    }
  </style>