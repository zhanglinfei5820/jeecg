package org.jeecg.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdInspectionService extends IService<AdInspection> {
    /**
     * 微信分页列表查询
     *
     * @param adInspection
     * @param pageNo
     * @param pageSize
     * @param adName
     * @return
     */
    IPage<AdInspectionVO> queryPageVxList(AdInspection adInspection, Integer pageNo, Integer pageSize, String adName);
}
