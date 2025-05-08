package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdVehicleService extends IService<AdVehicle> {
    /**
     * 获取车辆统计信息
     * 查询条件status不等于2
     * 从数据字典中获取所有车辆类型，并统计每种类型的车辆数量
     * 如果某种类型没有车辆，则计数为0
     * @return 统计信息
     */
    Map<String, Object> getVehicleStatistics(SysUser loginUser);

    /**
     * 分页查询，返回VO对象
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return VO分页结果
     */
    IPage<AdVehicleVO> pageVO(Page<AdVehicle> page, QueryWrapper<AdVehicle> queryWrapper);

    /**
     * 车辆管理查询
     * @param adVehicleVO 查询条件
     * @return 车辆列表
     */
    List<AdVehicleVO> queryVxList(AdVehicleVO adVehicleVO);
    
    /**
     * 通过XML SQL直接分页查询车辆信息
     * @param page 分页参数
     * @param queryParams 查询参数
     * @return 包含关联信息的车辆VO分页结果
     */
    IPage<AdVehicleVO> queryVehiclePageList(Page<AdVehicleVO> page, Map<String, Object> queryParams);
    
    /**
     * 小程序年检维护分页查询
     * 多表联查，关联车辆、年检、广告、公司信息
     * 根据ad_inspection表的type字段和其他业务条件进行查询
     * 
     * @param page 分页参数
     * @param queryParams 查询参数，包含年检类型、车牌号、车辆类型等
     * @return 包含车辆和年检信息的分页结果
     */
    IPage<AdVehicleVO> queryInspectionMaintenancePageList(Page<AdVehicleVO> page, Map<String, Object> queryParams);

    Long countEnabledVehicles(String publishId);

    void editAdVehicle(AdVehicle adVehicle);


}
