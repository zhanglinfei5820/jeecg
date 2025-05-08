import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
import { useGlobSetting } from '/@/hooks/setting';

const { apiUrl } = useGlobSetting();
const uploadUrl = `${apiUrl}/sys/common/upload`; 
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '发布明细ID',
    align:"center",
    dataIndex: 'publishDetailId'
   },
   {
    title: '司机ID',
    align:"center",
    dataIndex: 'driverId'
   },
   {
    title: '上报时间',
    align:"center",
    dataIndex: 'reportTime'
   },
   {
    title: '上报类型',
    align:"center",
    dataIndex: 'reportType',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_report_type');
    }
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_report_process_status');
    }
   },
   {
    title: '上报内容',
    align:"center",
    dataIndex: 'content'
   },
   {
    title: '维修金次数',
    align:"center",
    dataIndex: 'maintenanceCount'
   },
   {
    title: '上报图片(多张)',
    align:"center",
    dataIndex: 'images',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
   },
   {
    title: '处理结果',
    align:"center",
    dataIndex: 'processResult'
   },
   {
    title: '处理时间',
    align:"center",
    dataIndex: 'processTime'
   },
   {
    title: '处理人',
    align:"center",
    dataIndex: 'processBy'
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
    label: '发布明细ID',
    field: 'publishDetailId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '司机ID',
    field: 'driverId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '上报类型',
    field: 'reportType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_type",
      placeholder: '请选择上报类型',
      stringToNumber: true
    },
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_process_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '发布明细',
    field: 'publishDetailId',
    component: 'JSearchSelect',
    componentProps: {
      dict: 'ad_publish_detail,name,id,status=1',
      placeholder: '请选择发布明细',
      stringToNumber: true,
      api: '/ad/adPublishDetail/list',
      labelKey: 'name',
      valueKey: 'id'
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择发布明细!'},
          ];
     },
  },
  {
    label: '司机',
    field: 'driverId',
    component: 'JSearchSelect',
    componentProps: {
      dict: 'ad_driver,name,id,status=1',
      placeholder: '请选择司机',
      stringToNumber: true,
      api: '/ad/adDriver/list',
      labelKey: 'name',
      valueKey: 'id'
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择司机!'},
          ];
     },
  },
  {
    label: '上报时间',
    field: 'reportTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入上报时间!'},
          ];
     },
  },
  {
    label: '上报类型',
    field: 'reportType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_type",
      placeholder: '请选择上报类型',
      stringToNumber: true
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请选择上报类型!'},
          ];
     },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_process_status",
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
    label: '上报内容',
    field: 'content',
    component: 'Input',
  },
  {
    label: '维修金次数',
    field: 'maintenanceCount',
    component: 'Input',
  },
  {
    label: '上报图片(多张)',
    field: 'images',
    component: 'JUpload',
    componentProps: {
      maxSize: 10 * 1024 * 1024,  // 100MB
      accept: '.jpg,.jpeg,.png,.gif',
      uploadUrl: uploadUrl,
      multiple: false,
      showUploadList: true,
      showDownloadList: true,
      listType: 'text',
      placeholder: '请上传图片'
    },
    colProps: { span: 24 }
  },
  {
    label: '处理结果',
    field: 'processResult',
    component: 'Input',
  },
  {
    label: '处理时间',
    field: 'processTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '处理人',
    field: 'processBy',
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
  publishDetailId: {title: '发布明细ID',order: 0,view: 'text', type: 'string',},
  driverId: {title: '司机ID',order: 1,view: 'text', type: 'string',},
  reportTime: {title: '上报时间',order: 2,view: 'datetime', type: 'string',},
  reportType: {title: '上报类型',order: 3,view: 'number', type: 'number',},
  status: {title: '状态',order: 4,view: 'number', type: 'number',},
  content: {title: '上报内容',order: 5,view: 'text', type: 'string',},
  images: {title: '上报图片(多张)',order: 6,view: 'text', type: 'string',},
  processResult: {title: '处理结果',order: 7,view: 'text', type: 'string',},
  processTime: {title: '处理时间',order: 8,view: 'datetime', type: 'string',},
  processBy: {title: '处理人',order: 9,view: 'text', type: 'string',},
  remark: {title: '备注',order: 10,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}