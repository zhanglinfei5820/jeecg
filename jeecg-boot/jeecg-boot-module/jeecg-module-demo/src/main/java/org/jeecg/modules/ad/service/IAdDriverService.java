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
    void AddDriverAndUser(AdDriver adDriver);
}
