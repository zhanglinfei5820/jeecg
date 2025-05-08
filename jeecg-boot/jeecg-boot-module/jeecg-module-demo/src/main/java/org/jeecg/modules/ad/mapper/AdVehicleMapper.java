package org.jeecg.modules.ad.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdVehicleMapper extends BaseMapper<AdVehicle> {

    /**
     * 分页查询车辆信息，直接关联查询广告名称、公司名称和司机名称
     * @param page 分页参数
     * @param queryParams 查询参数
     * @return 包含关联信息的车辆VO分页结果
     */
    IPage<AdVehicleVO> queryVehiclePageList(Page<AdVehicleVO> page, @Param("params") Map<String, Object> queryParams);
    
    /**
     * 车辆管理查询列表
     * @param queryParams 查询参数
     * @return 车辆列表
     */
    List<AdVehicleVO> queryVxList(@Param("params") Map<String, Object> queryParams);
    
    /**
     * 小程序年检维护分页查询
     * 根据年检类型、车牌号等条件分页查询车辆和年检信息
     * 
     * @param page 分页参数
     * @param queryParams 查询参数，包含年检类型、车牌号、车辆类型等
     * @return 包含车辆和年检信息的分页结果
     */
    IPage<AdVehicleVO> queryInspectionMaintenancePageList(Page<AdVehicleVO> page, @Param("params") Map<String, Object> queryParams);

}
