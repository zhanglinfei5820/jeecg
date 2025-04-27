import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '分类ID',
    align: "center",
    dataIndex: 'categoryId'
  },
  {
    title: '物料名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '广告内容',
    align: "center",
    dataIndex: 'content'
  },{
    title: '文件URL',
    align: "center",
    dataIndex: 'fileUrl'
  },
  {
    title: '类型',
    align: "center",
    dataIndex: 'type',
    customRender: ({ text }) => { 
      return render.renderDict(text, 'ad_material_type');
    }
  },
  {
    title: '尺寸规格',
    align: "center",
    dataIndex: 'size'
  },
  {
    title: '播放时长(秒)',
    align: "center",
    dataIndex: 'duration'
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
  categoryId: {title: '分类ID',order: 0,view: 'text', type: 'string',},
  name: {title: '物料名称',order: 1,view: 'text', type: 'string',},
  fileUrl: {title: '文件URL',order: 2,view: 'text', type: 'string',},
  content: {title: '广告内容',order: 2,view: 'text', type: 'string',},
  type: {title: '类型',order: 3,view: 'number', type: 'number',},
  size: {title: '尺寸规格',order: 4,view: 'text', type: 'string',},
  duration: {title: '播放时长(秒)',order: 5,view: 'number', type: 'number',},
  status: {title: '状态',order: 6,view: 'number', type: 'number',},
  remark: {title: '备注',order: 7,view: 'text', type: 'string',},
};
