package org.jeecg.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionAddVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionVO;

import java.util.List;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdInspectionService extends IService<AdInspection> {
    /**
     * 微信分页列表查询
     *
     * @param adInspection
     * @param pageNo
     * @param pageSize
     * @param adName
     * @param vehicleType
     * @param column
     * @param order
     * @return
     */
    IPage<AdInspectionVO> queryPageVxList(AdInspection adInspection, Integer pageNo, Integer pageSize, String adName, Integer vehicleType, String column, String order);
    
    /**
     * 司机查询年检数据
     * 
     * @param driverId 司机ID
     * @param type 年检类型
     * @param status 年检状态
     * @return 年检数据列表
     */
    List<DriverInspectionVO> getDriverInspections(String driverId, String type, Integer status,String order);
    
    /**
     * 小程序司机提交年检信息
     * 
     * @param inspectionVO 年检信息
     * @return 操作结果
     */
    String addDriverInspection(DriverInspectionAddVO inspectionVO);
}
