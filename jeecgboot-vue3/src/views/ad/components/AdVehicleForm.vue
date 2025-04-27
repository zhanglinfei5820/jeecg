<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol" name="AdVehicleForm">
          <a-row>
            <a-col :span="24">
              <a-form-item label="所属司机" v-bind="validateInfos.driverName" id="AdVehicleForm-driverName" name="driverName">
                <j-search-select
                  v-model:value="formData.driverName"
                  dict="ad_driver,name,id,status=1"
                  placeholder="请选择司机"
                  @change="handleDriverChange"
                />
              </a-form-item>
            </a-col>
            <a-col :span="24" style="display: none;">
              <a-form-item label="司机id" v-bind="validateInfos.driverId" id="AdVehicleForm-driverId" name="driverId">
                <a-input v-model:value="formData.driverId" placeholder="请输入司机id" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="公司名称" v-bind="validateInfos.companyName" id="AdVehicleForm-companyName" name="companyName">
                <j-search-select
                  v-model:value="formData.companyName"
                  dict="ad_company,name,id,status=1"
                  placeholder="请选择公司"
                  @change="handleCompanyChange"
                />
              </a-form-item>
            </a-col>
            <a-col :span="24" style="display: none;">
              <a-form-item label="公司id" v-bind="validateInfos.companyId" id="AdVehicleForm-companyId" name="companyId">
                <a-input v-model:value="formData.companyId" placeholder="请输入公司id" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="广告名称" v-bind="validateInfos.adName" id="AdVehicleForm-adName" name="adName">
                <j-search-select
                  v-model:value="formData.adName"
                  dict="ad_publish,title,id,status=1"
                  placeholder="请选择广告"
                  @change="handleAdPublishChange"
                />
              </a-form-item>
            </a-col>
            <a-col :span="24" style="display: none;">
              <a-form-item label="广告id" v-bind="validateInfos.adId" id="AdVehicleForm-adId" name="adId">
                <a-input v-model:value="formData.adId" placeholder="广告id" disabled></a-input>
              </a-form-item>
            </a-col>
						<a-col :span="24">
							<a-form-item label="车牌号码" v-bind="validateInfos.plateNumber" id="AdVehicleForm-plateNumber" name="plateNumber">
								<a-input v-model:value="formData.plateNumber" placeholder="请输入车牌号码"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="车辆类型" v-bind="validateInfos.vehicleType" id="AdVehicleForm-vehicleType" name="vehicleType">
								<j-dict-select-tag v-model:value="formData.vehicleType" placeholder="请输入车辆类型" dictCode="ad_vehicle_type"  allow-clear />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="品牌" v-bind="validateInfos.brand" id="AdVehicleForm-brand" name="brand">
								<a-input v-model:value="formData.brand" placeholder="请输入品牌"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="型号" v-bind="validateInfos.model" id="AdVehicleForm-model" name="model">
								<a-input v-model:value="formData.model" placeholder="请输入型号"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="颜色" v-bind="validateInfos.color" id="AdVehicleForm-color" name="color">
								<a-input v-model:value="formData.color" placeholder="请输入颜色"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="行驶证号" v-bind="validateInfos.vehicleLicense" id="AdVehicleForm-vehicleLicense" name="vehicleLicense">
								<a-input v-model:value="formData.vehicleLicense" placeholder="请输入行驶证号"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="行驶证图片" v-bind="validateInfos.licenseImage" id="AdVehicleForm-licenseImage" name="licenseImage">
								<JImageUpload v-model:value="formData.licenseImage" :maxCount="1" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="状态" v-bind="validateInfos.status" id="AdVehicleForm-status" name="status">
								<j-dict-select-tag v-model:value="formData.status" dictCode="ad_vehicle_status" placeholder="请选择状态" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="车窗可用面积(m²)" v-bind="validateInfos.windowArea" id="AdVehicleForm-windowArea" name="windowArea">
								<a-input-number v-model:value="formData.windowArea" placeholder="请输入车窗可用面积(m²)" :min="0" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="安装时间" v-bind="validateInfos.installationTime" id="AdVehicleForm-installationTime" name="installationTime">
								<a-date-picker
									v-model:value="formData.installationTime"
									show-time
									format="YYYY-MM-DD HH:mm:ss"
									placeholder="请选择安装时间"
									style="width: 100%"
								/>
							</a-form-item>
						</a-col>
            <a-col :span="24">
              <a-form-item label="安装图片" v-bind="validateInfos.installationImage" id="AdVehicleForm-installationImage" name="installationImage">
                <JUpload v-model:value="formData.installationImage" :maxCount="1" />
              </a-form-item>
            </a-col>
						<a-col :span="24">
							<a-form-item label="备注" v-bind="validateInfos.remark" id="AdVehicleForm-remark" name="remark">
								<a-input v-model:value="formData.remark" placeholder="请输入备注"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
          </a-row>
        </a-form>
      </template>
    </JFormContainer>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, defineProps, computed, onMounted, watch } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getValueType } from '/@/utils';
  import { saveOrUpdate } from '../AdVehicle.api';
  import { Form } from 'ant-design-vue';
  import JFormContainer from '/@/components/Form/src/container/JFormContainer.vue';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
  import JUpload from '/@/components/Form/src/jeecg/components/JUpload/JUpload.vue';
  import { SearchOutlined } from '@ant-design/icons-vue';
  const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({})},
    formBpm: { type: Boolean, default: true }
  });
  const formRef = ref();
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const formData = reactive<Record<string, any>>({
    id: '',
    driverId: '',   
    driverName: '',
    companyId: '',
    companyName: '',
    installationTime: null,
    plateNumber: '',   
    vehicleType: undefined,   
    brand: '',   
    model: '',   
    color: '',   
    vehicleLicense: '',   
    licenseImage: '',   
    status: undefined,
    windowArea: undefined,
    remark: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = reactive({
    driverName: [{ required: true, message: '请选择所属司机!'},],
    companyName: [{ required: true, message: '请选择公司!'},],
    plateNumber: [{ required: true, message: '请输入车牌号码!'},],
    vehicleType: [{ required: true, message: '请选择车辆类型!'},],
    vehicleLicense: [{ required: true, message: '请输入行驶证号!'},],
    status: [{ required: true, message: '请选择状态!'},],
  });
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: false });

  // 表单禁用
  const disabled = computed(()=>{
    if(props.formBpm === true){
      if(props.formData.disabled === false){
        return false;
      }else{
        return true;
      }
    }
    return props.formDisabled;
  });

  
  /**
   * 新增
   */
  function add() {
    edit({});
  }

  /**
   * 编辑
   */
  async function edit(record) {
    nextTick(async () => {
      resetFields();
      const tmpData = {};
      Object.keys(formData).forEach((key) => {
        if(record.hasOwnProperty(key)){
          tmpData[key] = record[key]
        }
      })
      //赋值
      Object.assign(formData, tmpData);
      // 如果是编辑状态，加载数据字典值
      if (record.id) {
        // 加载类型和状态的数据字典值
        const [statusDict] = await Promise.all([
          defHttp.get({ url: '/sys/dict/getDictItems/ad_status' })
        ]);
        if (record.status !== undefined && record.status !== null) {
          const statusItem = statusDict.find(item => String(item.value) === String(record.status));
          if (statusItem) {
            formData.status = statusItem.value;
          }
        }
        
      }
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    try {
      // 触发表单验证
      await validate();
    } catch (error: any) {
      const { errorFields } = error;
      if (errorFields) {
        const firstField = errorFields[0];
        if (firstField) {
          formRef.value.scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
        }
      }
      return Promise.reject(error);
    }
    confirmLoading.value = true;
    const isUpdate = ref<boolean>(false);
    //时间格式化
    let model = formData;
    if (model.id) {
      isUpdate.value = true;
    }
    //循环数据
    for (let data in model) {
      //如果该数据是数组并且是字符串类型
      if (model[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          model[data] = model[data].join(',');
        }
      }
    }
    await saveOrUpdate(model, isUpdate.value)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }

  // 司机选择相关
  const driverModalVisible = ref<boolean>(false);
  const driverSearchName = ref<string>('');
  const driverSearchStatus = ref<number>();
  const selectedDriverKeys = ref<string[]>([]);
  const selectedDriverName = ref<string>('');
  const driverList = ref<any[]>([]);
  const driverPagination = ref({
    current: 1,
    pageSize: 10,
    total: 0
  });

  const driverColumns = [
    {
      title: '序号',
      dataIndex: 'index',
      width: 80,
      customRender: ({ index }) => {
        return index + 1;
      }
    },
    {
      title: '司机姓名',
      dataIndex: 'name',
      width: 120
    },
    {
      title: '手机号码',
      dataIndex: 'phone',
      width: 120
    },
    {
      title: '身份证号',
      dataIndex: 'idCard',
      width: 180
    },
    {
      title: '驾驶证号',
      dataIndex: 'driverLicense',
      width: 150
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      customRender: ({ text }) => {
        return text === 1 ? '启用' : '禁用';
      }
    }
  ];

  // 显示司机选择弹窗
  const showDriverModal = () => {
    driverModalVisible.value = true;
    loadDriverList();
  };

  // 加载司机列表
  const loadDriverList = async () => {
    try {
      const params = {
        name: driverSearchName.value,
        status: driverSearchStatus.value,
        pageNo: driverPagination.value.current,
        pageSize: driverPagination.value.pageSize
      };
      const res = await defHttp.get({ url: '/ad/adDriver/list', params });
      driverList.value = res.records || [];
      driverPagination.value.total = res.total;
    } catch (error) {
      console.error('加载司机列表失败:', error);
    }
  };

  // 搜索司机
  const searchDrivers = () => {
    driverPagination.value.current = 1;
    loadDriverList();
  };

  // 重置搜索
  const resetDriverSearch = () => {
    driverSearchName.value = '';
    driverSearchStatus.value = undefined;
    driverPagination.value.current = 1;
    loadDriverList();
  };

  // 选择司机变更
  const onSelectChange = (selectedRowKeys: string[], selectedRows: any[]) => {
    selectedDriverKeys.value = selectedRowKeys;
    if (selectedRows.length > 0) {
      selectedDriverName.value = selectedRows[0].name;
    } else {
      selectedDriverName.value = '';
    }
  };

  // 处理分页变化
  const handleDriverTableChange = (pagination: any) => {
    driverPagination.value.current = pagination.current;
    loadDriverList();
  };

  // 确认选择司机
  const handleDriverModalOk = () => {
    if (selectedDriverKeys.value.length > 0) {
      formData.driverId = selectedDriverKeys.value[0];
    }
    driverModalVisible.value = false;
  };

  // 取消选择司机
  const handleDriverModalCancel = () => {
    driverModalVisible.value = false;
  };

  // 编辑时加载司机信息
  const loadSelectedDriver = async (driverId: string) => {
    if (driverId) {
      try {
        const res = await defHttp.get({ url: `/ad/adDriver/queryById`, params: { id: driverId } });
        if (res) {
          selectedDriverName.value = res.name;
          selectedDriverKeys.value = [driverId];
        }
      } catch (error) {
        console.error('加载司机信息失败:', error);
      }
    }
  };

  // 监听编辑表单时的driverId变化
  watch(() => formData.driverId, (newVal) => {
    if (newVal) {
      loadSelectedDriver(newVal);
    } else {
      selectedDriverName.value = '';
      selectedDriverKeys.value = [];
    }
  }, { immediate: true });

  // 处理司机选择变化
  const handleDriverChange = async (value: string | { value: string; label: string }) => {
    console.log("value", value);
    if (typeof value === 'string') {
      // 如果value是字符串，说明直接传入了id，需要获取司机详情
      try {
        const res = await defHttp.get({ url: `/ad/adDriver/queryById`, params: { id: value } });
        if (res) {
          formData.driverId = value;
          formData.driverName = res.name;
        }
      } catch (error) {
        console.error('获取司机信息失败:', error);
      }
    } else if (value && typeof value === 'object') {
      // 如果value是对象，包含label和value
      formData.driverName = value.label;
      formData.driverId = value.value;
    }
  };

  // 处理公司选择变化
  const handleCompanyChange = async (value: string | { value: string; label: string }) => {
    console.log("value", value);
    if (typeof value === 'string') {
      // 如果value是字符串，说明直接传入了id，需要获取公司详情
      try {
        const res = await defHttp.get({ url: `/ad/adCompany/queryById`, params: { id: value } });
        if (res) {
          formData.companyId = value;
          formData.companyName = res.name;
        }
      } catch (error) {
        console.error('获取公司信息失败:', error);
      }
    } else if (value && typeof value === 'object') {
      // 如果value是对象，包含label和value
      formData.companyName = value.label;
      formData.companyId = value.value;
    }
  };

  // 处理广告选择变化
  const handleAdPublishChange = async (value: string | { value: string; label: string }) => {
    console.log("value", value);
    if (typeof value === 'string') {
      // 如果value是字符串，说明直接传入了id，需要获取公司详情
      try {
        const res = await defHttp.get({ url: `/ad/adPublish/queryById`, params: { id: value } });
        if (res) {
          formData.adId = value;
          formData.adName = res.title;
        }
      } catch (error) {
        console.error('获取广告信息失败:', error);
      }
    } else if (value && typeof value === 'object') {
      // 如果value是对象，包含label和value
      formData.adName = value.label;
      formData.adId = value.value;
    }
  };

  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 14px;
  }

  .driver-select-wrapper {
    .ant-input {
      cursor: pointer;
    }
  }

  .driver-search-form {
    margin-bottom: 16px;
  }
</style>
