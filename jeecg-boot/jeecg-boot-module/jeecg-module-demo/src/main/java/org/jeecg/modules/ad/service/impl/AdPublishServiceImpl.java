package org.jeecg.modules.ad.service.impl;

import java.util.List;
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.entity.Vo.DriverAdVO;
import org.jeecg.modules.ad.mapper.AdPublishMapper;
import org.jeecg.modules.ad.service.IAdPublishService;
import org.jeecg.modules.ad.utils.EntityNameCache;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 广告发布表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdPublishServiceImpl extends ServiceImpl<AdPublishMapper, AdPublish> implements IAdPublishService {

    @Resource
    private EntityNameCache entityNameCache;

    @Override
    public String getAdNameById(String adId) {
        if (adId == null || adId.trim().isEmpty()) {
            return "";
        }
        
        // 优先从缓存中获取
        String cachedName = entityNameCache.getAdName(adId);
        if (cachedName != null) {
            return cachedName;
        }
        
        // 缓存未命中，从数据库查询
        AdPublish adPublish = this.getById(adId);
        String adName = adPublish != null ? adPublish.getName() : "";
        
        // 存入缓存
        entityNameCache.putAdName(adId, adName);
        
        return adName;
    }
    
    /**
     * 查询司机广告信息
     * 通过一次联合查询获取所有需要的信息，减少数据库访问次数
     * 
     * @param phone 司机手机号
     * @return 广告信息列表
     */
    @Override
    public List<AdPublish> getDriverAds(String phone) {
        // 通过XML中定义的联合查询一次性获取所有需要的数据
        return baseMapper.queryDriverAds(phone);
    }
}
