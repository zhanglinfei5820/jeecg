package org.jeecg.modules.ad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdInspection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdInspectionMapper extends BaseMapper<AdInspection> {

    /**
     * 微信分页列表查询
     *
     * @param page
     * @param adName
     * @return
     */
    IPage<AdInspectionVO> queryPageVxList(IPage<AdInspectionVO> page, @Param("adName") String adName);
}
