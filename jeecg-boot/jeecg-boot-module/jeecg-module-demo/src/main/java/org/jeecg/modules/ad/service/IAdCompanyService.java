package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdCompany;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 广告公司
 * @Author: jeecg-boot
 * @Date:   2025-04-26
 * @Version: V1.0
 */
public interface IAdCompanyService extends IService<AdCompany> {
    void addCompanyAndUser(AdCompany adCompany);
}
