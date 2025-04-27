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
    title: '结算月份(YYYY-MM)',
    align:"center",
    dataIndex: 'month'
   },
   {
    title: '维护次数',
    align:"center",
    dataIndex: 'maintenanceCount'
   },
   {
    title: '维护金额',
    align:"center",
    dataIndex: 'maintenanceAmount'
   },
   {
    title: '广告收益',
    align:"center",
    dataIndex: 'adAmount'
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
    label: '结算月份(YYYY-MM)',
    field: 'month',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入结算月份(YYYY-MM)!'},
          ];
     },
  },
  {
    label: '维护次数',
    field: 'maintenanceCount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入维护次数!'},
          ];
     },
  },
  {
    label: '维护金额',
    field: 'maintenanceAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入维护金额!'},
          ];
     },
  },
  {
    label: '广告收益',
    field: 'adAmount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入广告收益!'},
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
  driverId: {title: '司机ID',order: 0,view: 'text', type: 'string',},
  month: {title: '结算月份(YYYY-MM)',order: 1,view: 'text', type: 'string',},
  maintenanceCount: {title: '维护次数',order: 2,view: 'number', type: 'number',},
  maintenanceAmount: {title: '维护金额',order: 3,view: 'number', type: 'number',},
  adAmount: {title: '广告收益',order: 4,view: 'number', type: 'number',},
  totalAmount: {title: '总金额',order: 5,view: 'number', type: 'number',},
  paidAmount: {title: '已付金额',order: 6,view: 'number', type: 'number',},
  unpaidAmount: {title: '未付金额',order: 7,view: 'number', type: 'number',},
  status: {title: '状态(0未结算,1部分结算,2已结算)',order: 8,view: 'number', type: 'number',},
  settlementTime: {title: '结算时间',order: 9,view: 'datetime', type: 'string',},
  settlementBy: {title: '结算人',order: 10,view: 'text', type: 'string',},
  remark: {title: '备注',order: 11,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}