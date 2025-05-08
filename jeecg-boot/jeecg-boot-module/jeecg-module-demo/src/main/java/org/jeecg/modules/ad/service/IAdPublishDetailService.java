package org.jeecg.modules.ad.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.Vo.AdPublishDetailVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;

import java.util.Map;

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
    String installation(AdPublishDetailVO adPublishDetailVO);

    IPage<AdPublishDetailVO> queryPulishDetailPageList(Page<AdPublishDetailVO> page, Map<String, Object> queryParams);
}
