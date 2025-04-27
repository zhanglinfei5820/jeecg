import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '分类名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '分类编码',
    align:"center",
    dataIndex: 'code'
   },
   {
    title: '排序值',
    align:"center",
    sorter: true,
    dataIndex: 'sort'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status_dictText'
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
    label: "分类名称",
    field: "name",
    component: 'JInput',
  },
  {
    label: "分类编码",
    field: "code",
    component: 'JInput',
  },
	{
      label: "状态",
      field: 'status',
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"ad_status"
      },
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '分类名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入分类名称!'},
          ];
     },
  },
  {
    label: '分类编码',
    field: 'code',
    component: 'Input',
  },
  {
    label: '排序值',
    field: 'sort',
    component: 'InputNumber',
    componentProps: {
      min: 0
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_status",
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
  name: {title: '分类名称',order: 0,view: 'text', type: 'string',},
  code: {title: '分类编码',order: 1,view: 'text', type: 'string',},
  sort: {title: '排序值',order: 2,view: 'number', type: 'number',},
  status: {title: '状态',order: 3,view: 'number', type: 'number',dictCode: 'ad_status',},
  remark: {title: '备注',order: 4,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}