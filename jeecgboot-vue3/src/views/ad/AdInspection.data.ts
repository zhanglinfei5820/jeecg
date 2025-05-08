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
    title: '年检时间',
    align:"center",
    dataIndex: 'inspectionTime'
   },
   {
    title: '检查人',
    align:"center",
    dataIndex: 'inspector'
   },
   {
    title: '车辆id',
    align:"center",
    dataIndex: 'vehicleId'
   },
   {
    title: '类型',
    align:"center",
    dataIndex: 'type',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_inspection_type');
    }
   },
   {
    title: '年检结果',
    align:"center",
    dataIndex: 'result',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_report_result');
    }
   },
   {
    title: '破损程度',
    align:"center",
    dataIndex: 'damageLevel',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_damage_level');
    }
   },
   {
    title: '破损描述',
    align:"center",
    dataIndex: 'damageDesc'
   },
   {
    title: '检查图片(多张)',
    align:"center",
    dataIndex: 'images',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_inspection_status');
    }
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '检查人',
    field: 'inspector',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '检查结果',
    field: 'result',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_result",
      placeholder: '请选择检查结果',
      stringToNumber: true
    },
    colProps: {span: 6},
  },
  {
    label: '类型',
    field: 'type',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_inspection_type",
      placeholder: '请选择类型',
      stringToNumber: true
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '发布广告',
    field: 'adId',
    component: 'JSearchSelect',
    componentProps: {
      dict: 'ad_publish,name,id,status=1 or status=4',
      placeholder: '请选择发布广告',
      stringToNumber: true,
      api: '/ad/adPublish/list',
      labelKey: 'name',
      valueKey: 'id'
    },
  },
  {
    label: '年检时间',
    field: 'inspectionTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '检查人',
    field: 'inspector',
    component: 'Input',
  },
  {
    label: '车辆id',
    field: 'vehicleId',
    component: 'Input',
    dynamicRules: () => {
          return [
                 { required: true, message: '请输入车辆id!'},
          ];
     },
  },
  {
    label: '类型',
    field: 'type',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_inspection_type",
      placeholder: '请选择类型',
      stringToNumber: true,
      valueField: 'value',
      labelField: 'text',
    },
  },
  {
    label: '检查结果',
    field: 'result',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_report_result",
      placeholder: '请选择检查结果',
      stringToNumber: true
    },
  },
  {
    label: '破损程度',
    field: 'damageLevel',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_damage_level",
      placeholder: '请选择破损程度',
      stringToNumber: true
    },
  },
  {
    label: '破损描述',
    field: 'damageDesc',
    component: 'Input',
  },
  {
    label: '检查图片(多张)',
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
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'ad_inspection_status',
      placeholder: '请选择状态',
      stringToNumber: true
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
  inspectionTime: {title: '年检时间',order: 1,view: 'datetime', type: 'string',},
  inspector: {title: '检查人',order: 2,view: 'text', type: 'string',},
  result: {title: '检查结果(0不合格,1合格)',order: 3,view: 'number', type: 'number',},
  damageLevel: {title: '破损程度(0无,1轻微,2中等,3严重)',order: 4,view: 'number', type: 'number',},
  damageDesc: {title: '破损描述',order: 5,view: 'text', type: 'string',},
  images: {title: '检查图片(多张)',order: 6,view: 'text', type: 'string',},
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