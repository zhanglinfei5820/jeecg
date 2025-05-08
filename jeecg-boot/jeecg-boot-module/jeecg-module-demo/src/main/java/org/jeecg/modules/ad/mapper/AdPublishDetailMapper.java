package org.jeecg.modules.ad.mapper;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.ad.entity.Vo.AdPublishDetailVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;

/**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdPublishDetailMapper extends BaseMapper<AdPublishDetail> {

    IPage<AdPublishDetailVO> queryPulishDetailPageList(Page<AdPublishDetailVO> page, Map<String, Object> queryParams);
}
