package org.jeecg.modules.ad.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.entity.Vo.AdReportTaskVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;
import org.jeecg.modules.ad.mapper.AdReportMapper;
import org.jeecg.modules.ad.mapper.AdVehicleMapper;
import org.jeecg.modules.ad.service.IAdReportService;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdPublishDetailService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 广告上报记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdReportServiceImpl extends ServiceImpl<AdReportMapper, AdReport> implements IAdReportService {

    @Autowired
    private IAdPublishDetailService adPublishDetailService;
    
    @Autowired
    private IAdDriverService adDriverService;
    
    @Resource
    private AdVehicleMapper adVehicleMapper;

    @Override
    public List<AdReportTaskVO> queryReportTasksByStatus(Integer status) {
        // 获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String relatedId = sysUser.getRelatedId();

        if (CommonConstant.ROLE_CODE_ADDRIVE.contains(roleCode)) {
            // 调用Mapper中的方法执行SQL查询
            return baseMapper.queryReportTasksByStatus(status,relatedId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<AdReportTaskVO> queryVehiclesByStatusAndDue(Integer status,Integer maintenanceStatus, Integer due) {
        // 获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String relatedId = sysUser.getRelatedId();
        // 如果是司机角色，则只查询该司机的车辆
        if (CommonConstant.ROLE_CODE_ADDRIVE.contains(roleCode)) {
            return baseMapper.queryVehiclesByStatusAndDue(status,maintenanceStatus, due, relatedId);
        }

        // 其他角色返回空列表
        return new ArrayList<>();
//        return baseMapper.queryVehiclesByStatusAndDue(status, due, "1914507382362198018");
    }

    @Override
    public List<AdReportTaskVO> queryVehiclesByStatusAndDue2(Integer status,Integer maintenanceStatus, Integer due) {
        // 获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String relatedId = sysUser.getRelatedId();
        // 如果是司机角色，则只查询该司机的车辆
        if (CommonConstant.ROLE_CODE_ADDRIVE.contains(roleCode)) {
            return baseMapper.queryVehiclesByStatusAndDue2(status,maintenanceStatus, due, relatedId);
        }

        // 其他角色返回空列表
        return new ArrayList<>();
//        return baseMapper.queryVehiclesByStatusAndDue(status, due, "1914507382362198018");
    }

    /**
     * 小程序领取维修金
     * 处理流程：
     * 1. 更新广告上报记录状态为已处理(status=4)
     * 2. 获取广告明细中的价格
     * 3. 更新司机余额(balance+price)
     * 4. 更新车辆维护次数(maintenance_count+1)
     * 
     * @param reportId 广告上报记录ID
     * @return 是否领取成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveMaintenanceFee(String reportId) {
        // 1. 查询广告上报记录
        AdReport adReport = this.getById(reportId);
        if (adReport == null) {
            throw new RuntimeException("未找到对应的上报记录");
        }
        
        // 记录原始状态，检查是否可以领取
        int originalStatus = adReport.getStatus();
        if (originalStatus == 4) {
            throw new RuntimeException("该维修金已领取，请勿重复操作");
        }
        
        // 2. 获取发布明细ID和司机ID
        String publishDetailId = adReport.getPublishDetailId();
        String driverId = adReport.getDriverId();
        
        if (publishDetailId == null || driverId == null) {
            throw new RuntimeException("上报记录信息不完整，无法处理");
        }
        
        // 3. 查询广告明细，获取价格和车辆ID
        AdPublishDetail publishDetail = adPublishDetailService.getById(publishDetailId);
        if (publishDetail == null) {
            throw new RuntimeException("未找到对应的广告明细");
        }
        
        BigDecimal price = publishDetail.getPrice();
        String vehicleId = publishDetail.getVehicleId();
        
        if (price == null) {
            price = new BigDecimal(0);
        }
        
        if (vehicleId == null) {
            throw new RuntimeException("车辆信息不完整，无法处理");
        }
        
        // 4. 更新司机余额
        AdDriver driver = adDriverService.getById(driverId);
        if (driver == null) {
            throw new RuntimeException("未找到对应的司机信息");
        }
        
        // 计算新的余额
        BigDecimal currentBalance = driver.getBalance();
        if (currentBalance == null) {
            currentBalance = new BigDecimal(0);
        }
        
        BigDecimal newBalance = currentBalance.add(price);
        driver.setBalance(newBalance);
        
        // 5. 更新车辆维护次数 - 修改为直接使用Mapper
        // 先查询车辆信息
        AdVehicle vehicle = adVehicleMapper.selectById(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("未找到对应的车辆信息");
        }
        
        // 计算新的维护次数
        Integer maintenanceCount = vehicle.getMaintenanceCount();
        if (maintenanceCount == null) {
            maintenanceCount = 0;
        }
        
        // 更新维护次数
        vehicle.setMaintenanceCount(maintenanceCount + 1);
        
        // 6. 更新上报记录状态为已处理(status=4)
        adReport.setStatus(4);
        
        // 7. 批量更新数据
        boolean updateAdReportResult = this.updateById(adReport);
        boolean updateDriverResult = adDriverService.updateById(driver);
        
        // 直接使用mapper更新车辆信息
        int vehicleUpdateResult = adVehicleMapper.updateById(vehicle);
        
        // 所有更新都成功才返回true
        return updateAdReportResult && updateDriverResult && (vehicleUpdateResult > 0);
    }
}
