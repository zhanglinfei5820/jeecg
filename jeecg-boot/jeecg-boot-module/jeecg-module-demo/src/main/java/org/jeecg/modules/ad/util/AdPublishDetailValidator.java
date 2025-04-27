package org.jeecg.modules.ad.util;

import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.common.exception.JeecgBootException;
import org.apache.commons.lang3.StringUtils;

/**
 * 广告发布明细参数校验工具类
 */
public class AdPublishDetailValidator {
    
    /**
     * 校验分发参数
     * @param detail 广告发布明细
     * @throws JeecgBootException 参数校验异常
     */
    public static void validateDistribute(AdPublishDetail detail) {
        if (detail == null) {
            throw new JeecgBootException("参数错误：广告发布明细不能为空");
        }
        
        if (StringUtils.isBlank(detail.getPublishId())) {
            throw new JeecgBootException("参数错误：发布ID不能为空");
        }
        
        if (detail.getDrivers() == null || detail.getDrivers() <= 0) {
            throw new JeecgBootException("参数错误：司机数必须大于0");
        }
    }
} 