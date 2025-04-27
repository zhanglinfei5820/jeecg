<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol" name="AdMaterialForm">
          <a-row>
						<a-col :span="24">
							<a-form-item label="分类" v-bind="validateInfos.categoryId" id="AdMaterialForm-categoryId" name="categoryId">
								<div class="category-select-wrapper">
									<a-input v-model:value="selectedCategoryName" placeholder="请选择分类" readonly @click="showCategoryModal" allow-clear>
										<template #suffix>
											<SearchOutlined />
										</template>
									</a-input>
									<a-modal
										v-model:open="categoryModalVisible"
										title="分类选择"
										@ok="handleCategoryModalOk"
										@cancel="handleCategoryModalCancel"
										width="800px"
									>
										<div class="category-search-form">
											<a-input v-model:value="categorySearchName" placeholder="请输入分类名称" allow-clear style="width: 200px; margin-right: 8px;" />
											<a-input v-model:value="categorySearchCode" placeholder="请输入分类编码" allow-clear style="width: 200px; margin-right: 8px;" />
											<a-button type="primary" @click="searchCategories">查询</a-button>
											<a-button style="margin-left: 8px;" @click="resetCategorySearch">重置</a-button>
										</div>
										<a-table
											:columns="categoryColumns"
											:dataSource="categoryList"
											:rowSelection="{ 
												type: 'radio',
												selectedRowKeys: selectedCategoryKeys, 
												onChange: onCategorySelectChange 
											}"
											:pagination="categoryPagination"
											@change="handleCategoryTableChange"
											rowKey="id"
										>
										</a-table>
									</a-modal>
								</div>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="物料名称" v-bind="validateInfos.name" id="AdMaterialForm-name" name="name">
								<a-input v-model:value="formData.name" placeholder="请输入物料名称"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="广告内容" v-bind="validateInfos.content" id="AdMaterialForm-content" name="content">
								<a-input v-model:value="formData.content" placeholder="请输入广告内容"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
            <a-col :span="24">
							<a-form-item label="文件URL" v-bind="validateInfos.fileUrl" id="AdMaterialForm-fileUrl" name="fileUrl">
								<JUpload v-model:value="formData.fileUrl" :maxCount="1" :defaultFileList="getDefaultFileList()" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="类型" v-bind="validateInfos.type" id="AdMaterialForm-type" name="type">
								<JDictSelectTag v-model:value="formData.type" placeholder="请选择类型" dictCode="ad_material_type" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="尺寸规格" v-bind="validateInfos.size" id="AdMaterialForm-size" name="size">
								<a-input v-model:value="formData.size" placeholder="请输入尺寸规格"  allow-clear ></a-input>
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="播放时长(秒)" v-bind="validateInfos.duration" id="AdMaterialForm-duration" name="duration">
								<a-input-number v-model:value="formData.duration" placeholder="请输入播放时长(秒)" style="width: 100%" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="状态" v-bind="validateInfos.status" id="AdMaterialForm-status" name="status">
								<JDictSelectTag v-model:value="formData.status" placeholder="请选择状态" dictCode="ad_status" />
							</a-form-item>
						</a-col>
						<a-col :span="24">
							<a-form-item label="备注" v-bind="validateInfos.remark" id="AdMaterialForm-remark" name="remark">
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
  import { saveOrUpdate } from '../AdMaterial.api';
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
    categoryId: '',   
    name: '',   
    content: '',   
    type: undefined,
    size: '',   
    duration: undefined,
    status: undefined,
    remark: '',   
    categoryName: '',   
    fileUrl: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = reactive({
    categoryId: [{ required: true, message: '请输入分类ID!'},],
    name: [{ required: true, message: '请输入物料名称!'},],
    type: [{ required: true, message: '请选择类型!'},],
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

  // Category selection related state
  const categoryModalVisible = ref<boolean>(false);
  const categorySearchName = ref<string>('');
  const categorySearchCode = ref<string>('');
  const selectedCategoryKeys = ref<string[]>([]);
  const selectedCategoryName = ref<string>('');
  const categoryList = ref<any[]>([]);
  const categoryPagination = ref({
    current: 1,
    pageSize: 10,
    total: 0
  });

  const categoryColumns = [
    {
      title: '序号',
      dataIndex: 'index',
      width: 80,
      customRender: ({ index }) => {
        return index + 1;
      }
    },
    {
      title: '分类名称',
      dataIndex: 'name',
      width: 120
    },
    {
      title: '分类编码',
      dataIndex: 'code',
      width: 120
    }
  ];

  // 显示分类选择弹窗
  const showCategoryModal = () => {
    categoryModalVisible.value = true;
    loadCategoryList();
  };

  // 加载分类列表
  const loadCategoryList = async () => {
    try {
      const params = {
        name: categorySearchName.value,
        code: categorySearchCode.value,
        status: 1,
        pageNo: categoryPagination.value.current,
        pageSize: categoryPagination.value.pageSize
      };
      const res = await defHttp.get({ url: '/ad/adCategory/list', params });
      categoryList.value = res.records || [];
      categoryPagination.value.total = res.total;
    } catch (error) {
      console.error('加载分类列表失败:', error);
    }
  };

  // 搜索分类
  const searchCategories = () => {
    categoryPagination.value.current = 1;
    loadCategoryList();
  };

  // 重置搜索
  const resetCategorySearch = () => {
    categorySearchName.value = '';
    categorySearchCode.value = '';
    categoryPagination.value.current = 1;
    loadCategoryList();
  };

  // 选择分类变更
  const onCategorySelectChange = (selectedRowKeys: string[], selectedRows: any[]) => {
    selectedCategoryKeys.value = selectedRowKeys;
    if (selectedRows.length > 0) {
      selectedCategoryName.value = selectedRows[0].name;
    } else {
      selectedCategoryName.value = '';
    }
  };

  // 处理分页变化
  const handleCategoryTableChange = (pagination: any) => {
    categoryPagination.value.current = pagination.current;
    loadCategoryList();
  };

  // 确认选择分类
  const handleCategoryModalOk = () => {
    if (selectedCategoryKeys.value.length > 0) {
      formData.categoryId = selectedCategoryKeys.value[0];
    }
    categoryModalVisible.value = false;
  };

  // 取消选择分类
  const handleCategoryModalCancel = () => {
    categoryModalVisible.value = false;
  };

  // 编辑时加载分类信息
  const loadSelectedCategory = async (categoryId: string) => {
    if (categoryId) {
      try {
        const res = await defHttp.get({ url: `/ad/adCategory/queryById`, params: { id: categoryId } });
        if (res) {
          selectedCategoryName.value = res.name;
          selectedCategoryKeys.value = [categoryId];
        }
      } catch (error) {
        console.error('加载分类信息失败:', error);
      }
    }
  };

  // 监听编辑表单时的categoryId变化
  watch(() => formData.categoryId, (newVal) => {
    if (newVal) {
      loadSelectedCategory(newVal);
    } else {
      selectedCategoryName.value = '';
      selectedCategoryKeys.value = [];
    }
  }, { immediate: true });

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
      });
      //赋值
      Object.assign(formData, tmpData); 
      // 如果是编辑状态，加载数据字典值
      if (record.id) {
        // 加载类型和状态的数据字典值
        const [typeDict, statusDict] = await Promise.all([
          defHttp.get({ url: '/sys/dict/getDictItems/ad_material_type' }),
          defHttp.get({ url: '/sys/dict/getDictItems/ad_status' })
        ]);
        // 设置类型和状态的值
        if (record.type !== undefined && record.type !== null) {
          const typeItem = typeDict.find(item => String(item.value) === String(record.type));
          if (typeItem) {
            formData.type = typeItem.value;
          }
        }
        if (record.status !== undefined && record.status !== null) {  // 修改这里，使其能处理0值
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
    } catch ({ errorFields }) {
      if (errorFields) {
        const firstField = errorFields[0];
        if (firstField) {
          formRef.value.scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
        }
      }
      return Promise.reject(errorFields);
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

  // 获取默认文件列表
  const getDefaultFileList = () => {
    if (formData.fileUrl) {
      return [{
        uid: '-1',
        name: formData.fileUrl.split('/').pop(),
        status: 'done',
        url: formData.fileUrl
      }];
    }
    return [];
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
</style>
