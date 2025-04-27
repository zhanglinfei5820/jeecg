package org.jeecg.modules.ad.service.impl;

import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.mapper.AdDriverMapper;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdMerchantService;
import org.jeecg.modules.ad.utils.CommonConstant;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void AddDriverAndUser(AdDriver adDriver) {
        adMerchantService.insertSysUser(adDriver.getPhone(),CommonConstant.DRIVER_USER_DEPT, CommonConstant.DRIVER_VALUE);
        this.saveOrUpdate(adDriver);
    }
}
