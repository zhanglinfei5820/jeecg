import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '司机ID',
    align:"center",
    dataIndex: 'driverId'
   },
   {
    title: '提现金额',
    align:"center",
    dataIndex: 'amount'
   },
   {
    title: '银行名称',
    align:"center",
    dataIndex: 'bankName'
   },
   {
    title: '银行账号',
    align:"center",
    dataIndex: 'bankAccount'
   },
   {
    title: '开户名',
    align:"center",
    dataIndex: 'accountName'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_withdrawal_status');
    }
   },
   {
    title: '审核时间',
    align:"center",
    dataIndex: 'auditTime'
   },
   {
    title: '审核人',
    align:"center",
    dataIndex: 'auditBy'
   },
   {
    title: '审核备注',
    align:"center",
    dataIndex: 'auditRemark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '司机ID',
    field: 'driverId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_withdrawal_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '司机ID',
    field: 'driverId',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入司机ID!'},
          ];
     },
  },
  {
    label: '提现金额',
    field: 'amount',
    component: 'InputNumber',
    componentProps: {
      min: 0,
      precision: 0
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入提现金额!'},
          ];
     },
  },
  {
    label: '银行名称',
    field: 'bankName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入银行名称!'},
          ];
     },
  },
  {
    label: '银行账号',
    field: 'bankAccount',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入银行账号!'},
          ];
     },
  },
  {
    label: '开户名',
    field: 'accountName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入开户名!'},
          ];
     },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_withdrawal_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择状态!'},
          ];
     },
  },
  {
    label: '审核时间',
    field: 'auditTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入审核时间!'},
          ];
     },
  },
  {
    label: '审核人',
    field: 'auditBy',
    component: 'Input',
  },
  {
    label: '审核备注',
    field: 'auditRemark',
    component: 'Input',
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
  driverId: {title: '司机ID',order: 0,view: 'text', type: 'string',},
  amount: {title: '提现金额',order: 1,view: 'number', type: 'number',},
  bankName: {title: '银行名称',order: 2,view: 'text', type: 'string',},
  bankAccount: {title: '银行账号',order: 3,view: 'text', type: 'string',},
  accountName: {title: '开户名',order: 4,view: 'text', type: 'string',},
  status: {title: '状态(0待处理,1处理中,2已完成,3已拒绝)',order: 5,view: 'number', type: 'number',},
  auditTime: {title: '审核时间',order: 6,view: 'datetime', type: 'string',},
  auditBy: {title: '审核人',order: 7,view: 'text', type: 'string',},
  auditRemark: {title: '审核备注',order: 8,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}