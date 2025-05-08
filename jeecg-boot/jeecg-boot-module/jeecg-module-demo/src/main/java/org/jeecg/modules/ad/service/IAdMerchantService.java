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
    /**
     * 根据公司ID获取公司名称
     * @param companyId 公司ID
     * @return 公司名称
     */
    String getCompanyNameById(String companyId);
}
