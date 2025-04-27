package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdMerchant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 商户表（发布方）
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdMerchantService extends IService<AdMerchant> {
    void AddMerchantAndUser(AdMerchant adMerchant);
    void insertSysUser(String phone,String deptId,String orgCode);
}
