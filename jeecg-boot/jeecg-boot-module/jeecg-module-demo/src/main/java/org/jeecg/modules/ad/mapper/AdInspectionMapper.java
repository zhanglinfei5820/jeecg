package org.jeecg.modules.ad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdInspection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionVO;

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
     * @param vehicleType
     * @param column
     * @param order
     * @return
     */
    IPage<AdInspectionVO> queryPageVxList(IPage<AdInspectionVO> page, @Param("adName") String adName, @Param("vehicleType") Integer vehicleType, @Param("column") String column, @Param("order") String order);
    
    /**
     * 司机查询年检数据
     * 
     * @param driverId 司机ID
     * @param type 年检类型
     * @param status 年检状态
     * @return 年检数据列表
     */
    List<DriverInspectionVO> queryDriverInspections(
        @Param("driverId") String driverId, 
        @Param("type") String type, 
        @Param("status") Integer status,@Param("order") String order);
}
