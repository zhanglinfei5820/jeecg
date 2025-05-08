package org.jeecg.modules.ad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.entity.Vo.DriverAdVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 广告发布表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdPublishMapper extends BaseMapper<AdPublish> {
    
    /**
     * 根据司机手机号查询对应车辆上的广告信息
     * 
     * @param phone 司机手机号
     * @return 广告信息列表
     */
    List<AdPublish> queryDriverAds(@Param("phone") String phone);
}
