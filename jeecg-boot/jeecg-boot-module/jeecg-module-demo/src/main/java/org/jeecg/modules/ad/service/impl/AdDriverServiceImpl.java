package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.mapper.AdDriverMapper;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdMerchantService;
import org.jeecg.modules.ad.service.ICommonLoginUserService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.ad.utils.EntityNameCache;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 司机表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdDriverServiceImpl extends ServiceImpl<AdDriverMapper, AdDriver> implements IAdDriverService {

    @Resource
    private IAdMerchantService adMerchantService;
    @Resource
    private ICommonLoginUserService commonLoginUserService;
    
    @Resource
    private EntityNameCache entityNameCache;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void AddDriverAndUser(AdDriver adDriver) {
        LambdaQueryWrapper<AdDriver> queryWrapper = new QueryWrapper<AdDriver>().lambda()
                .eq(AdDriver::getPhone, adDriver.getPhone());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("手机号码已经被录用!");
        }
        this.saveOrUpdate(adDriver);
        commonLoginUserService.insertSysUser(adDriver.getPhone(),adDriver.getName(),adDriver.getId(),CommonConstant.DRIVER_USER_ROLE, CommonConstant.DRIVER_VALUE);
    }
    
    @Override
    public String getDriverNameById(String driverId) {
        if (driverId == null || driverId.trim().isEmpty()) {
            return "";
        }
        
        // 优先从缓存中获取
        String cachedName = entityNameCache.getDriverName(driverId);
        if (cachedName != null) {
            return cachedName;
        }
        
        // 缓存未命中，从数据库查询
        AdDriver driver = this.getById(driverId);
        String driverName = driver != null ? driver.getName() : "";
        
        // 存入缓存
        entityNameCache.putDriverName(driverId, driverName);
        
        return driverName;
    }
    
    /**
     * 获取当前登录司机信息
     * 通过当前登录用户的phone查询司机表获取完整信息
     * 
     * @return 司机信息
     */
    @Override
    public AdDriver getCurrentDriverInfo() {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return null;
        }
        
        // 获取手机号
        String phone = loginUser.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        // 构建查询条件：根据手机号查询
        LambdaQueryWrapper<AdDriver> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdDriver::getPhone, phone);
        
        // 查询一次数据库，获取司机信息
        return this.getOne(queryWrapper);
    }
}
