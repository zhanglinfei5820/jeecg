import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '商户ID',
    align:"center",
    dataIndex: 'merchantId'
   },
   {
    title: '结算月份(YYYY-MM)',
    align:"center",
    dataIndex: 'month'
   },
   {
    title: '广告数量',
    align:"center",
    dataIndex: 'adCount'
   },
   {
    title: '总金额',
    align:"center",
    dataIndex: 'totalAmount'
   },
   {
    title: '已付金额',
    align:"center",
    dataIndex: 'paidAmount'
   },
   {
    title: '未付金额',
    align:"center",
    dataIndex: 'unpaidAmount'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_settlement_status');
    }
   },
   {
    title: '结算时间',
    align:"center",
    dataIndex: 'settlementTime'
   },
   {
    title: '结算人',
    align:"center",
    dataIndex: 'settlementBy'
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
    label: '商户ID',
    field: 'merchantId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_settlement_status",
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
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入商户ID!'},
          ];
     },
  },
  {
    label: '结算月份(YYYY-MM)',
    field: 'month',
    component: 'Input',
  },
  {
    label: '广告数量',
    field: 'adCount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入广告数量!'},
          ];
     },
  },
  {
    label: '总金额',
    field: 'totalAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入总金额!'},
          ];
     },
  },
  {
    label: '已付金额',
    field: 'paidAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入已付金额!'},
          ];
     },
  },
  {
    label: '未付金额',
    field: 'unpaidAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入未付金额!'},
          ];
     },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_settlement_status",
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
    label: '结算时间',
    field: 'settlementTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '结算人',
    field: 'settlementBy',
    component: 'Input',
  },
  {
    label: '备注',
    field: 'remark',
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
  merchantId: {title: '商户ID',order: 0,view: 'text', type: 'string',},
  month: {title: '结算月份(YYYY-MM)',order: 1,view: 'text', type: 'string',},
  adCount: {title: '广告数量',order: 2,view: 'number', type: 'number',},
  totalAmount: {title: '总金额',order: 3,view: 'number', type: 'number',},
  paidAmount: {title: '已付金额',order: 4,view: 'number', type: 'number',},
  unpaidAmount: {title: '未付金额',order: 5,view: 'number', type: 'number',},
  status: {title: '状态(0未结算,1部分结算,2已结算)',order: 6,view: 'number', type: 'number',},
  settlementTime: {title: '结算时间',order: 7,view: 'datetime', type: 'string',},
  settlementBy: {title: '结算人',order: 8,view: 'text', type: 'string',},
  remark: {title: '备注',order: 9,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}