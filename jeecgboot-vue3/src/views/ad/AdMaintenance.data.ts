import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '广告年检表id',
    align:"center",
    dataIndex: 'adInspectionId'
   },
   {
    title: '发布明细ID',
    align:"center",
    dataIndex: 'publishDetailId'
   },
   {
    title: '破损等级',
    align:"center",
    dataIndex: 'damageSeverity'
   },
   {
    title: '破损具体情况描述',
    align:"center",
    dataIndex: 'damageDescription'
   },
   {
    title: '破损首次被上报的日期时间',
    align:"center",
    dataIndex: 'firstReportTime'
   },
   {
    title: '最近一次维护的日期时间',
    align:"center",
    dataIndex: 'lastMaintenanceTime'
   },
   {
    title: '状态枚举',
    align:"center",
    dataIndex: 'status'
   },
   {
    title: '维护处理结果说明',
    align:"center",
    dataIndex: 'processingResult'
   },
   {
    title: '负责处理的人员姓名或工号',
    align:"center",
    dataIndex: 'processorName'
   },
   {
    title: '处理操作完成的日期时间',
    align:"center",
    dataIndex: 'processingTime'
   },
   {
    title: '破损现场照片存储路径',
    align:"center",
    dataIndex: 'damageImages'
   },
   {
    title: '维修工单等文件存储路径',
    align:"center",
    dataIndex: 'maintenanceAttachments'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "破损等级",
      field: 'damageSeverity',
      component: 'InputNumber',
      //colProps: {span: 6},
 	},
	{
      label: "状态枚举",
      field: 'status',
      component: 'InputNumber',
      //colProps: {span: 6},
 	},
	{
      label: "负责处理的人员姓名或工号",
      field: 'processorName',
      component: 'Input',
      //colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '广告年检表id',
    field: 'adInspectionId',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入广告年检表id!'},
          ];
     },
  },
  {
    label: '发布明细ID',
    field: 'publishDetailId',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入发布明细ID!'},
          ];
     },
  },
  {
    label: '破损等级',
    field: 'damageSeverity',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入破损等级!'},
          ];
     },
  },
  {
    label: '破损具体情况描述',
    field: 'damageDescription',
    component: 'Input',
  },
  {
    label: '破损首次被上报的日期时间',
    field: 'firstReportTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入破损首次被上报的日期时间!'},
          ];
     },
  },
  {
    label: '最近一次维护的日期时间',
    field: 'lastMaintenanceTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '状态枚举',
    field: 'status',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入状态枚举!'},
          ];
     },
  },
  {
    label: '维护处理结果说明',
    field: 'processingResult',
    component: 'Input',
  },
  {
    label: '负责处理的人员姓名或工号',
    field: 'processorName',
    component: 'Input',
  },
  {
    label: '处理操作完成的日期时间',
    field: 'processingTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '破损现场照片存储路径',
    field: 'damageImages',
    component: 'Input',
  },
  {
    label: '维修工单等文件存储路径',
    field: 'maintenanceAttachments',
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
  adInspectionId: {title: '广告年检表id',order: 0,view: 'text', type: 'string',},
  publishDetailId: {title: '发布明细ID',order: 1,view: 'text', type: 'string',},
  damageSeverity: {title: '破损等级',order: 2,view: 'number', type: 'number',},
  damageDescription: {title: '破损具体情况描述',order: 3,view: 'text', type: 'string',},
  firstReportTime: {title: '破损首次被上报的日期时间',order: 4,view: 'datetime', type: 'string',},
  lastMaintenanceTime: {title: '最近一次维护的日期时间',order: 5,view: 'datetime', type: 'string',},
  status: {title: '状态枚举',order: 6,view: 'number', type: 'number',},
  processingResult: {title: '维护处理结果说明',order: 7,view: 'text', type: 'string',},
  processorName: {title: '负责处理的人员姓名或工号',order: 8,view: 'text', type: 'string',},
  processingTime: {title: '处理操作完成的日期时间',order: 9,view: 'datetime', type: 'string',},
  damageImages: {title: '破损现场照片存储路径',order: 10,view: 'text', type: 'string',},
  maintenanceAttachments: {title: '维修工单等文件存储路径',order: 11,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}