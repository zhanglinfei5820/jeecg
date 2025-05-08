import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
import { useUserStore } from '/@/store/modules/user';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: 'ID',
    align:"center",
    dataIndex: 'id',
    width:220
  },
  {
    title: '商户ID',
    align:"center", 
    dataIndex: 'merchantId',
    width:220,
    ifShow: false
   },
   {
    title: '商户名称',
    align:"center", 
    dataIndex: 'merchantId_dictText'
   },
   {
    title: '物料ID',
    align:"center",
    dataIndex: 'materialId',
    width:220,
    ifShow: false
   },
   {
    title: '物料名称',
    align:"center",
    dataIndex: 'materialId_dictText'
   },
   {
    title: '广告标题',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '开始报名时间',
    align:"center",
    dataIndex: 'startTime'
   },
   {
    title: '报名结束时间',
    align:"center",
    dataIndex: 'endTime'
   },
   {
    title: '预算金额',
    align:"center",
    dataIndex: 'budget'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_publish_status');
    }
   },
   {
    title: '安装时间',
    align:"center",
    dataIndex: 'installationTime'
   },
   {
    title: '安装地址',
    align:"center",
    dataIndex: 'installationAddress'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
   {
    title: '广告位置',
    align:"center",
    dataIndex: 'position',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_position');
    }
   },
   {
    title: '司机数',
    align:"center",
    dataIndex: 'drivers'
   },
   {
    title: '单价',
    align:"center",
    dataIndex: 'price'
   },
];
//查询表单数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '商户ID',
    field: 'merchantId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '物料ID',
    field: 'materialId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '广告标题',
    field: 'name',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '开始报名时间',
    field: 'startTime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss'
    },
    colProps: {span: 6},
  },
  {
    label: '报名结束时间',
    field: 'endTime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss'
    },
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_publish_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '商户ID',
    field: 'merchantId',
    component: 'JSearchSelect',
    componentProps: ({formModel}) => {
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo; 
      const relatedId = userInfo.relatedId;
      let dictCode = "ad_merchant,name,id,status=1";
      if (relatedId) {
        dictCode = `ad_merchant,name,id,status=1 and id=`+relatedId;
      }
      return {
        dict: dictCode,
        placeholder: '请选择商户',
        labelKey: 'name',
        valueKey: 'id',
        stringToNumber: true
      }
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择商户!'},
          ];
     },
  },
  {
    label: '物料ID',
    field: 'materialId',
    component: 'JSearchSelect',
    componentProps: ({formModel}) => {
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo; 
      const relatedId = userInfo.relatedId;
      let dictCode = "ad_material,name,id,status=1";
      if (relatedId) {
        dictCode = `ad_material,name,id,status=1 and merchant_id=`+relatedId;
      }
      return {
        dict: dictCode,
        placeholder: '请选择物料',
        stringToNumber: true,
        api: '/ad/adMaterial/list',
        labelKey: 'name',
        valueKey: 'id'
      }
      
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择物料!'},
          ];
     },
  },
  {
    label: '广告标题',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入广告标题!'},
          ];
     },
  },
  {
    label: '开始报名时间',
    field: 'startTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入开始报名时间!'},
                 { validator: (_, value) => {
                    if (value && model.endTime && value >= model.endTime) {
                      return Promise.reject('开始报名时间必须在报名结束时间之前!');
                    }
                    return Promise.resolve();
                 }}
          ];
     },
  },
  {
    label: '报名结束时间',
    field: 'endTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入报名结束时间!'},
                 { validator: (_, value) => {
                    if (value && model.startTime && value <= model.startTime) {
                      return Promise.reject('报名结束时间必须在开始报名时间之后!');
                    }
                    return Promise.resolve();
                 }}
          ];
     },
  },
  {
    label: '投放开始时间',
    field: 'launchStartTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入开始报名时间!'},
                 { validator: (_, value) => {
                    if (value && model.launchEndTime && value >= model.launchEndTime) {
                      return Promise.reject('投放开始时间必须在投放结束时间之前!');
                    }
                    return Promise.resolve();
                 }}
          ];
     },
  },
  {
    label: '投放结束时间',
    field: 'launchEndTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入报名结束时间!'},
                 { validator: (_, value) => {
                    if (value && model.launchStartTime && value <= model.launchStartTime) {
                      return Promise.reject('投放结束时间必须在投放开始时间之后!');
                    }
                    return Promise.resolve();
                 }}
          ];
     },
  },
  {
    label: '预算金额',
    field: 'budget',
    component: 'InputNumber',
    componentProps: {
      min: 0,
      precision: 2
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入预算金额!'},
          ];
     },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: ({formActionType}) => {
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo;
      // 检查用户是否为管理员
      const isAdmin =  userInfo.username === 'admin';
      // 如果不是管理员，默认设置为0（待审核）且禁用选择
      if (!isAdmin) {
        // 设置表单默认值为0
        setTimeout(() => {
          formActionType.setFieldsValue({
            status: 0
          });
        }, 10);
        
        return {
          dictCode: "ad_publish_status",
          placeholder: '请选择状态',
          stringToNumber: true,
          disabled: true  // 禁用下拉选择
        };
      }
      
      // 如果是管理员，维持原有行为
      return {
        dictCode: "ad_publish_status",
        placeholder: '请选择状态',
        stringToNumber: true
      };
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择状态!'},
          ];
     },
  },
  {
    label: '安装时间',
    field: 'installationTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入安装时间!'},
                 { validator: (_, value) => {
                    if (value && model.startTime && model.endTime) {
                      if (value < model.startTime || value > model.endTime) {
                        return Promise.reject('安装时间必须在开始报名时间和报名结束时间之间!');
                      }
                    }
                    return Promise.resolve();
                 }}
          ];
     },
  },
  {
    label: '安装地址',
    field: 'installationAddress',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入安装地址!'},
          ];
     },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
  },
  {
    label: '广告位置',
    field: 'position',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_position",
      placeholder: '请选择广告位置',
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择广告位置!'},
          ];
     },
  },
  {
    label: '司机数',
    field: 'drivers',
    component: 'InputNumber',
    componentProps: {
      min: 0,
      precision: 0
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入司机数!'},
          ];
     },
  },
  {
    label: '单价',
    field: 'price',
    componentProps: {
      min: 0,
      precision: 2
    },
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入单价!'},
          ];
     },
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];

// 高级查询数据
export const superQuerySchema = {
  merchantId: {title: '商户ID',order: 0,view: 'text', type: 'string',},
  materialId: {title: '物料ID',order: 1,view: 'text', type: 'string',},
  title: {title: '广告标题',order: 2,view: 'text', type: 'string',},
  startTime: {title: '开始报名时间',order: 3,view: 'datetime', type: 'string',},
  endTime: {title: '报名结束时间',order: 4,view: 'datetime', type: 'string',},
  budget: {title: '预算金额',order: 5,view: 'number', type: 'number',},
  status: {title: '状态',order: 6,view: 'number', type: 'number',},
  installationTime: {title: '安装时间',order: 7,view: 'datetime', type: 'string',},
  installationAddress: {title: '安装地址',order: 8,view: 'text', type: 'string',},
  remark: {title: '备注',order: 9,view: 'text', type: 'string',},
  position: {title: '广告位置',order: 10,view: 'text', type: 'string',},
  drivers: {title: '司机数',order: 11,view: 'number', type: 'number',},
  price: {title: '单价',order: 12,view: 'number', type: 'number',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}