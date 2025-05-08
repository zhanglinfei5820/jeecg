package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionAddVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionVO;
import org.jeecg.modules.ad.mapper.AdInspectionMapper;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdInspectionService;
import org.jeecg.modules.ad.service.IAdVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdInspectionServiceImpl extends ServiceImpl<AdInspectionMapper, AdInspection> implements IAdInspectionService {

    @Autowired
    private IAdDriverService adDriverService;
    
    @Autowired
    private IAdVehicleService adVehicleService;

    @Override
    public IPage<AdInspectionVO> queryPageVxList(AdInspection adInspection, Integer pageNo, Integer pageSize, String adName, Integer vehicleType, String column, String order) {
        // 创建分页对象
        Page<AdInspectionVO> page = new Page<>(pageNo, pageSize);
        
        // 调用Mapper执行SQL查询
        return baseMapper.queryPageVxList(page, adName, vehicleType, column, order);
    }
    
    /**
     * 司机查询年检数据
     * 通过关联查询获取广告名称，减少数据库访问次数
     * 
     * @param driverId 司机ID
     * @param type 年检类型
     * @param status 年检状态
     * @return 年检数据列表
     */
    @Override
    public List<DriverInspectionVO> getDriverInspections(String driverId, String type, Integer status,String order) {
        // 调用Mapper一次性查询所有相关数据，包括广告名称
        return baseMapper.queryDriverInspections(driverId, type, status,order);
    }
    
    /**
     * 小程序司机提交年检信息
     * 通过一次查询获取司机信息，再通过车牌号查询车辆信息，减少数据库访问次数
     * 
     * @param inspectionVO 年检信息
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDriverInspection(DriverInspectionAddVO inspectionVO) {
        // 1. 验证司机信息
        AdDriver driver = adDriverService.getById(inspectionVO.getRelatedId());
        if (driver == null) {
            return "司机信息不存在";
        }
        
        // 2. 根据车牌号查询车辆信息
        LambdaQueryWrapper<AdVehicle> vehicleQuery = new LambdaQueryWrapper<>();
        vehicleQuery.eq(AdVehicle::getPlateNumber, inspectionVO.getPlateNumber())
                   .eq(AdVehicle::getDriverId, inspectionVO.getRelatedId());
        AdVehicle vehicle = adVehicleService.getOne(vehicleQuery);
        
        if (vehicle == null) {
            return "车牌号不存在或不属于该司机";
        }
        
        // 3. 创建年检记录
        AdInspection inspection = new AdInspection();
        inspection.setAdId(inspectionVO.getAdId());
        inspection.setVehicleId(vehicle.getId());
        inspection.setDriveId(inspectionVO.getRelatedId());
        inspection.setInspectionTime(new Date());
        inspection.setType(inspectionVO.getType());
        inspection.setDamageDesc(inspectionVO.getDamageDesc());
        inspection.setImages(inspectionVO.getImages());
        inspection.setStatus(0); // 设置状态为待审核
        inspection.setCreateTime(new Date());
        inspection.setInspectionTime(DateUtil.parse(inspectionVO.getInspectionTime()));
        
        // 4. 保存年检记录
        this.save(inspection);
        
        return "年检信息提交成功";
    }
}
