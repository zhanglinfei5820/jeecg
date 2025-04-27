package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdPublishDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdPublishDetailService extends IService<AdPublishDetail> {
/**
     * 分发广告
     * @param adPublishDetail 广告发布明细
     * @return 分发结果信息
     */
    String distribute(AdPublishDetail adPublishDetail);
}
