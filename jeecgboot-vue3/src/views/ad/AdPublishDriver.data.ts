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
    title: '发布ID',
    align:"center",
    dataIndex: 'publishId',
    ifShow: false
   },
   {
    title: '发布广告',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '车辆ID',
    align:"center",
    dataIndex: 'vehicleId',
    ifShow: false
   },
   {
    title: '车牌号',
    align:"center",
    dataIndex: 'plateNumber'
   },
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
      title: '公司ID',
      align:"center",
      dataIndex: 'companyId',
      ifShow: false
    },
    {
      title: '公司名称',
      align:"center",
      dataIndex: 'companyName'
    },
    {
      title: '司机数',
      align:"center",
      dataIndex: 'drivers'
    },
    {
      title: '实际司机数',
      align:"center",
      dataIndex: 'actualDrivers'
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
    title: '单价(元/天)',
    align:"center",
    dataIndex: 'price'
   },
   {
    title: '抽成',
    align:"center",
    dataIndex: 'percentage'
   },
   {
    title: '类型',
    align:"center",
    dataIndex: 'type',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_publish_detail_type');
    }
   },
   {
    title: '状态',
    align:"center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_publish_detail_status');
    }
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '发布ID',
    field: 'publishId',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '司机名称',
    field: 'driverName',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '公司名称',
    field: 'companyName',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_publish_detail_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    colProps: {span: 6},
  },
  {
    label: '类型',
    field: 'type',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_publish_detail_type",
      placeholder: '请选择类型',
    },
    colProps: {span: 6},
  }
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '发布ID',
    field: 'publishId',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入发布ID!'},
          ];
     },
  },
  {
    label: '广告标题',
    field: 'title',
    component: 'Input',
  },
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
    label: '司机名称',
    field: 'driverName',
    component: 'Input',
  },
  {
    label: '司机数',
    field: 'drivers',
    component: 'InputNumber',
  },
  {
    label: '实际司机数',
    field: 'actualDrivers',
    component: 'InputNumber',
  },
  {
    label: '公司ID',
    field: 'companyId',
    component: 'Input',
  },
  {
    label: '公司名称',
    field: 'companyName',
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
                 { required: true, message: '请输入广告位置!'},
          ];
     },
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
    label: '单价(元/天)',
    field: 'price',
    component: 'InputNumber',
    componentProps: {
      min: 0,
      precision: 2
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入单价(元/天)!'},
          ];
     },
  },
  {
    label: '抽成',
    field: 'percentage',
    component: 'InputNumber',
    componentProps: {
      min: 0,
      max: 100,
      precision: 2
    },
  },
  {
    label: '类型',
    field: 'type',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_publish_detail_type",
      placeholder: '请选择类型',
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "ad_publish_detail_status",
      placeholder: '请选择状态',
      stringToNumber: true
    },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入状态!'},
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
  publishId: {title: '发布ID',order: 0,view: 'text', type: 'string',},
  vehicleId: {title: '车辆ID',order: 1,view: 'text', type: 'string',},
  driverId: {title: '司机ID',order: 2,view: 'text', type: 'string',},
  position: {title: '广告位置',order: 3,view: 'text', type: 'string',},
  price: {title: '单价',order: 4,view: 'number', type: 'number',},
  status: {title: '状态',order: 5,view: 'number', type: 'number',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}