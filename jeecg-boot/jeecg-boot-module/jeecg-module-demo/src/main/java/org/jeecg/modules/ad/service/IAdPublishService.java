package org.jeecg.modules.ad.service;

import java.util.List;
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.entity.Vo.DriverAdVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 广告发布表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdPublishService extends IService<AdPublish> {
    /**
     * 根据广告ID获取广告名称
     * @param adId 广告ID
     * @return 广告名称
     */
    String getAdNameById(String adId);
    
    /**
     * 查询司机广告信息
     * 
     * @param phone 司机手机号
     * @return 广告信息列表
     */
    List<AdPublish> getDriverAds(String phone);
}
