import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '所属司机ID',
    align: "center",
    dataIndex: 'driverId'
  },
  {
    title: '所属司机名称',
    align: "center",
    dataIndex: 'driverName'
  },
  {
    title: '公司id',
    align: "center",
    dataIndex: 'companyId'
  },
  // {
  //   title: '公司名称',
  //   align: "center",
  //   dataIndex: 'companyName'
  // },
  // {
  //   title: '广告名称',
  //   align: "center",
  //   dataIndex: 'adName'
  // },
  {
    title: '广告id',
    align: "center",
    dataIndex: 'adId'
  },
  {
    title: '安装时间',
    align: "center",
    dataIndex: 'installationTime'
  },
  {
    title: '安装图片',
    align: "center",
    dataIndex: 'installationImage',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
  },
  {
    title: '车辆图片',
    align: "center",
    dataIndex: 'vehicleImage',
    customRender: ({ text }) => {
      return render.renderImage({ text });
    }
  },
  {
    title: '车牌号码',
    align: "center",
    dataIndex: 'plateNumber'
  },
  {
    title: '车辆类型',
    align: "center",
    dataIndex: 'vehicleType',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_vehicle_type');
    }
  },
  {
    title: '品牌',
    align: "center",
    dataIndex: 'brand'
  },
  {
    title: '型号',
    align: "center",
    dataIndex: 'model'
  },
  {
    title: '颜色',
    align: "center",
    dataIndex: 'color'
  },
  {
    title: '行驶证号',
    align: "center",
    dataIndex: 'vehicleLicense'
  },
  {
    title: '行驶证图片',
    align: "center",
    dataIndex: 'licenseImage'
  },
  {
    title: '状态',
    align: "center",
    dataIndex: 'status',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_vehicle_status');
    }
  },
  {
    title: '车窗可用面积(m²)',
    align: "center",
    dataIndex: 'windowArea'
  },
  {
    title: '备注',
    align: "center",
    dataIndex: 'remark'
  },
];

// 高级查询数据
export const superQuerySchema = {
  driverId: {title: '所属司机ID',order: 0,view: 'text', type: 'string',},
  plateNumber: {title: '车牌号码',order: 1,view: 'text', type: 'string',},
  vehicleType: {title: '车辆类型',order: 2,view: 'text', type: 'string',},
  brand: {title: '品牌',order: 3,view: 'text', type: 'string',},
  model: {title: '型号',order: 4,view: 'text', type: 'string',},
  color: {title: '颜色',order: 5,view: 'text', type: 'string',},
  vehicleLicense: {title: '行驶证号',order: 6,view: 'text', type: 'string',},
  licenseImage: {title: '行驶证图片',order: 7,view: 'text', type: 'string',},
  status: {title: '状态(0禁用,1启用)',order: 8,view: 'number', type: 'number',},
  windowArea: {title: '车窗可用面积(m²)',order: 9,view: 'number', type: 'number',},
  remark: {title: '备注',order: 10,view: 'text', type: 'string',},
};
