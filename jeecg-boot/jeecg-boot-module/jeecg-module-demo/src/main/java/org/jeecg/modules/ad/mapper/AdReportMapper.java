package org.jeecg.modules.ad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.Vo.AdReportTaskVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;

/**
 * @Description: 广告上报记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdReportMapper extends BaseMapper<AdReport> {

    /**
     * 根据状态查询上报任务列表（小程序接口）
     * 
     * @param status 状态
     * @return 上报任务列表
     */
    List<AdReportTaskVO> queryReportTasksByStatus(@Param("status") Integer status,@Param("driverId") String driverId);

    /**
     * 根据状态和是否到期查询车辆信息（小程序接口）
     *
     * @param status 车辆状态
     * @param due 是否检查到期状态
     * @param driverId 司机ID
     * @return 车辆信息列表
     */
    List<AdReportTaskVO> queryVehiclesByStatusAndDue(@Param("status") Integer status,
                                                     @Param("maintenanceStatus") Integer maintenanceStatus,
                                                      @Param("due") Integer due,
                                                      @Param("driverId") String driverId);
    List<AdReportTaskVO> queryVehiclesByStatusAndDue2(@Param("status") Integer status,
                                                     @Param("maintenanceStatus") Integer maintenanceStatus,
                                                     @Param("due") Integer due,
                                                     @Param("driverId") String driverId);
}
