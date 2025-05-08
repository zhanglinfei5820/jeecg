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
    dataIndex: 'driverId',
    ifShow: false
   },
   {
    title: '司机名称',
    align:"center",
    dataIndex: 'driverName'
   },
   {
    title: '抽成',
    align:"center",
    dataIndex: 'percentage'
   },
   {
    title: '金额',
    align:"center",
    dataIndex: 'amount'
   },
  //  {
  //   title: '类型',
  //   align:"center",
  //   dataIndex: 'type',
  //   customRender: ({ text }) => { 
  //     return render.renderDict(text, 'ad_stream_type');
  //   }
  //  },
   {
    title: '交易类型',
    align:"center",
    dataIndex: 'transactionType',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_transaction_type');
    }
   },
   {
    title: '关联ID',
    align:"center",
    dataIndex: 'relatedId'
   },
   {
    title: '交易后余额',
    align:"center",
    dataIndex: 'balance'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_s_status');
    }
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
    label: '关联ID',
    field: 'relatedId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '交易类型',
    field: 'transactionType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_transaction_type",
      placeholder: '请选择交易类型',
      stringToNumber: true
    },
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_s_status",
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
  },
  {
    label: '抽成',
    field: 'percentage',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入金额!'},
          ];
     },
  },
  {
    label: '金额',
    field: 'amount',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入金额!'},
          ];
     },
  },
  // {
  //   label: '类型',
  //   field: 'type',
  //   component: 'JDictSelectTag',
  //   componentProps: {
  //     dictCode: "ad_stream_type",
  //     placeholder: '请选择类型',
  //     stringToNumber: true
  //   },
  //   dynamicRules: ({model,schema}) => {
  //         return [
  //                { required: true, message: '请选择类型!'},
  //         ];
  //    },
  // },
  {
    label: '交易类型',
    field: 'transactionType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_transaction_type",
      placeholder: '请选择交易类型',
      stringToNumber: true
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择交易类型!'},
          ];
     },
  },
  {
    label: '关联ID',
    field: 'relatedId',
    component: 'Input',
  },
  {
    label: '交易后余额',
    field: 'balance',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入交易后余额!'},
          ];
     },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_s_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入状态!'},
          ];
     },
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
  amount: {title: '金额',order: 1,view: 'number', type: 'number',},
  type: {title: '类型(1收入,2支出)',order: 2,view: 'number', type: 'number',},
  transactionType: {title: '交易类型(1广告收益,2维护金,3提现)',order: 3,view: 'number', type: 'number',},
  relatedId: {title: '关联ID(发布明细ID/提现ID等)',order: 4,view: 'text', type: 'string',},
  balance: {title: '交易后余额',order: 5,view: 'number', type: 'number',},
  status: {title: '状态(0失败,1成功)',order: 6,view: 'number', type: 'number',},
  remark: {title: '备注',order: 7,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}