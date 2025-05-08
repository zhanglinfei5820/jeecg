package org.jeecg.modules.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.AdDriver;

/**
 * @Description: 司机表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdDriverService extends IService<AdDriver> {
    /**
     * 添加司机并创建用户
     * @param adDriver 司机信息
     */
    void AddDriverAndUser(AdDriver adDriver);
    
    /**
     * 根据司机ID获取司机名称
     * @param driverId 司机ID
     * @return 司机名称
     */
    String getDriverNameById(String driverId);
    
    /**
     * 获取当前登录司机信息
     * 通过当前登录用户的phone查询司机表获取完整信息
     * 
     * @return 司机信息
     */
    AdDriver getCurrentDriverInfo();
}
