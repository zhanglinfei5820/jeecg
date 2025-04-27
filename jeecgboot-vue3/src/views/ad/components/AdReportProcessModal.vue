<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="处理广告上报" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { reportProcess } from '/@/views/ad/AdReport.api';
  import { useMessage } from '/@/hooks/web/useMessage';

  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  const record = ref<Record<string, any>>({});

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: [
      {
        field: 'status',
        label: '处理状态',
        component: 'JDictSelectTag',
        componentProps: {
          dictCode: 'ad_report_process_status',
          placeholder: '请选择处理状态',
          stringToNumber: true
        },
        required: true,
      },
      {
        field: 'processResult',
        label: '处理结果',
        component: 'InputTextArea',
        componentProps: {
          placeholder: '请输入处理结果',
          rows: 4
        },
        required: true,
      },
    ],
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    record.value = data?.record || {};
  });

  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      
      // 构建请求参数
      const params = {
        driverId: record.value.driverId,
        reportId: record.value.id,
        publishDetailId: record.value.publishDetailId,
        transactionType: 1, // 默认1
        status: values.status,
        processResult: values.processResult
      };
      
      // 调用处理接口
      const result = await reportProcess(params); 
    } catch (error) {
      console.error('处理上报失败:', error);
      createMessage.error('处理失败，请重试');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script> 