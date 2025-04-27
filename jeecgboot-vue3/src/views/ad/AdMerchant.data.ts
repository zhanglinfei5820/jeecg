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
    title: '商户名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '联系人',
    align:"center",
    dataIndex: 'contact'
   },
   {
    title: '联系电话',
    align:"center",
    dataIndex: 'phone'
   },
   {
    title: '地址',
    align:"center",
    dataIndex: 'address'
   },
   {
    title: '营业执照号',
    align:"center",
    dataIndex: 'businessLicense'
   },
   {
    title: '营业执照图片',
    align:"center",
    dataIndex: 'licenseImage'
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
    label: '商户名称',
    field: 'name',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '联系人',
    field: 'contact',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: '联系电话',
    field: 'phone',
    component: 'Input',
    colProps: {span: 6},
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
    colProps: {span: 6},
  }
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '商户名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入商户名称!'},
          ];
     },
  },
  {
    label: '联系人',
    field: 'contact',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入联系人!'},
          ];
     },
  },
  {
    label: '联系电话',
    field: 'phone',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入联系电话!'},
                 { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码!' },
          ];
     },
  },
  {
    label: '地址',
    field: 'address',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入地址!'},
          ];
     },
  },
  {
    label: '营业执照号',
    field: 'businessLicense',
    component: 'Input',
  },
  {
    label: '营业执照图片',
    field: 'licenseImage',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 1,
      fileType: 'image',
      action: uploadUrl,
    },
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
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
];

// 高级查询数据
export const superQuerySchema = {
  name: {title: '商户名称',order: 0,view: 'text', type: 'string',},
  contact: {title: '联系人',order: 3,view: 'text', type: 'string',},
  phone: {title: '联系电话',order: 4,view: 'text', type: 'string',},
  address: {title: '地址',order: 5,view: 'text', type: 'string',},
  businessLicense: {title: '营业执照号',order: 6,view: 'text', type: 'string',},
  licenseImage: {title: '营业执照图片',order: 7,view: 'text', type: 'string',},
  status: {title: '状态',order: 8,view: 'number', type: 'number',},
  remark: {title: '备注',order: 13,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}