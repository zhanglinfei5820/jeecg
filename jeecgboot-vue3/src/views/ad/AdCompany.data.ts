import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '广告公司名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '联系人',
    align: "center",
    dataIndex: 'contact'
  },
  {
    title: '联系电话',
    align: "center",
    dataIndex: 'phone'
  },
  {
    title: '公司地址',
    align: "center",
    dataIndex: 'address'
  },
  {
    title: '营业执照号',
    align: "center",
    dataIndex: 'businessLicense'
  },
  {
    title: '营业执照图片',
    align: "center",
    dataIndex: 'licenseImage'
  },
  {
    title: '状态',
    align: "center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_status');
    }
  },
  {
    title: '备注',
    align: "center",
    dataIndex: 'remark'
  },
];

// 高级查询数据
export const superQuerySchema = {
  name: {title: '广告公司名称',order: 0,view: 'text', type: 'string',},
  contact: {title: '联系人',order: 1,view: 'text', type: 'string',},
  phone: {title: '联系电话',order: 2,view: 'text', type: 'string',},
  address: {title: '公司地址',order: 3,view: 'text', type: 'string',},
  businessLicense: {title: '营业执照号',order: 4,view: 'text', type: 'string',},
  licenseImage: {title: '营业执照图片',order: 5,view: 'text', type: 'string',},
  status: {title: '状态',order: 6,view: 'number', type: 'number',},
  remark: {title: '备注',order: 7,view: 'text', type: 'string',},
};
