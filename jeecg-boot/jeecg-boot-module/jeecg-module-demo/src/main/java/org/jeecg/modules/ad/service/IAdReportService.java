package org.jeecg.modules.ad.service;

import java.util.List;

import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.Vo.AdReportTaskVO;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;

/**
 * @Description: 广告上报记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdReportService extends IService<AdReport> {

    /**
     * 根据状态查询上报任务列表（小程序接口）
     * 
     * @param status 状态
     * @return 上报任务列表
     */
    List<AdReportTaskVO> queryReportTasksByStatus(Integer status);

    /**
     * 根据状态和是否到期查询车辆信息（小程序接口）
     *
     * @param status 车辆状态
     * @param due 是否检查到期状态
     * @return 车辆信息列表
     */
    List<AdReportTaskVO> queryVehiclesByStatusAndDue(Integer status,Integer maintenanceStatus, Integer due);

    /**
     * 根据状态和是否到期查询车辆信息（小程序接口）
     *
     * @param status 车辆状态
     * @param due 是否检查到期状态
     * @return 车辆信息列表
     */
    List<AdReportTaskVO> queryVehiclesByStatusAndDue2(Integer status,Integer maintenanceStatus, Integer due);

    /**
     * 小程序领取维修金
     * 
     * @param reportId 广告上报记录ID
     * @return 是否领取成功
     */
    boolean receiveMaintenanceFee(String reportId);
}
