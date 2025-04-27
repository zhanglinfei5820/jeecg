package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;
import org.jeecg.modules.ad.mapper.AdInspectionMapper;
import org.jeecg.modules.ad.service.IAdInspectionService;
import org.springframework.stereotype.Service;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdInspectionServiceImpl extends ServiceImpl<AdInspectionMapper, AdInspection> implements IAdInspectionService {

    @Override
    public IPage<AdInspectionVO> queryPageVxList(AdInspection adInspection, Integer pageNo, Integer pageSize, String adName) {
        // 创建分页对象
        Page<AdInspectionVO> page = new Page<>(pageNo, pageSize);
        
        // 调用Mapper执行SQL查询
        return baseMapper.queryPageVxList(page, adName);
    }
}
