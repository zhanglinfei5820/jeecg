import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '司机姓名',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '手机号码',
    align:"center",
    dataIndex: 'phone'
   },
   {
    title: '身份证号',
    align:"center",
    dataIndex: 'idCard'
   },
   {
    title: '身份证正面照',
    align:"center",
    dataIndex: 'idCardFront',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
   },
   {
    title: '身份证反面照',
    align:"center",
    dataIndex: 'idCardBack',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
   },
   {
    title: '驾驶证号',
    align:"center",
    dataIndex: 'driverLicense'
   },
   {
    title: '驾驶证图片',
    align:"center",
    dataIndex: 'licenseImage',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => {
      return render.renderDict(text, 'ad_status');
    }
   },
   {
    title: '账户余额',
    align:"center",
    dataIndex: 'balance'
   },
   {
    title: '累计收入',
    align:"center",
    dataIndex: 'totalIncome'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '司机姓名',
    field: 'name',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '身份证号',
    field: 'idCard',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: "状态",
    field: "status",
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"ad_status",
        placeholder: '请选择状态'
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '司机姓名',
    field: 'name',
    component: 'Input',
    dynamicRules: () => {
          return [
                 { required: true, message: '请输入司机姓名!'},
          ];
     },
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    dynamicRules: () => {
          return [
                 { required: true, message: '请输入手机号码!'},
                 { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码!'}
          ];
     },
  },
  {
    label: '身份证号',
    field: 'idCard',
    component: 'Input',
    dynamicRules: () => {
          return [
                 { required: true, message: '请输入身份证号!'},
          ];
     },
  },
  {
    label: '身份证正面照',
    field: 'idCardFront',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 1
    }
  },
  {
    label: '身份证反面照',
    field: 'idCardBack',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 1
    }
  },
  {
    label: '驾驶证号',
    field: 'driverLicense',
    component: 'Input',
  },
  {
    label: '驾驶证图片',
    field: 'licenseImage',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 1
    }
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'ad_status',
      placeholder: '请选择状态',
      stringToNumber: true
    },
    dynamicRules: () => {
          return [
                 { required: true, message: '请选择状态!'},
          ];
     },
  },
  {
    label: '账户余额',
    field: 'balance',
    component: 'InputNumber',
    componentProps: {
      min: 0
    },
    dynamicRules: () => {
          return [
                 {  message: '请输入账户余额!'},
          ];
     },
  },
  {
    label: '累计收入',
    field: 'totalIncome',
    component: 'InputNumber',
    componentProps: {
      min: 0
    },
    dynamicRules: () => {
          return [
                 {  message: '请输入累计收入!'},
          ];
     },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
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
  name: {title: '司机姓名',order: 0,view: 'text', type: 'string',},
  phone: {title: '手机号码',order: 1,view: 'text', type: 'string',},
  idCard: {title: '身份证号',order: 2,view: 'text', type: 'string',},
  status: {title: '状态',order: 3,view: 'select', type: 'number', dictCode: 'ad_status'},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}