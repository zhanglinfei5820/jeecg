package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.AdMerchant;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.mapper.AdVehicleMapper;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdMerchantService;
import org.jeecg.modules.ad.service.IAdPublishService;
import org.jeecg.modules.ad.service.IAdVehicleService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdVehicleServiceImpl extends ServiceImpl<AdVehicleMapper, AdVehicle> implements IAdVehicleService {

    @Resource
    private IAdDriverService adDriverService;
    @Resource
    private IAdPublishService adPublishService;
    @Resource
    private IAdMerchantService adMerchantService;

    @Override
    public Map<String, Object> getVehicleStatistics() {
        // TODO: 从数据库查询实际的车辆统计数据
        // 这里暂时返回模拟数据
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 100);
        statistics.put("enabled", 80);
        statistics.put("disabled", 20);
        return statistics;
    }

    @Override
    public List<?> queryVxList(AdVehicle adVehicle) {
        String plateNumber = adVehicle.getPlateNumber();
        String driverName = adVehicle.getDriverName();
        String adName = adVehicle.getAdName();
        QueryWrapper<AdVehicle> adVehicleQueryWrapper = new QueryWrapper<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        if (CommonConstant.ROLE_CODE_ADVERTISERS.equals(roleCode)) {
            //广告方
            adVehicleQueryWrapper.lambda().eq(AdVehicle::getAdId, sysUser.getRelatedId());
        }
        if (roleCode.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
            //公司
            adVehicleQueryWrapper.lambda().eq(AdVehicle::getCompanyId, sysUser.getRelatedId());
        }
        if (roleCode.contains(CommonConstant.ROLE_CODE_ADDRIVE)) {
            //司机
            adVehicleQueryWrapper.lambda().eq(AdVehicle::getDriverId, sysUser.getRelatedId());
        }
        adVehicleQueryWrapper.lambda()
                .eq(StringUtils.isNotBlank(plateNumber),AdVehicle::getPlateNumber, plateNumber)
                .eq(StringUtils.isNotBlank(driverName),AdVehicle::getDriverName, driverName)
                .eq(StringUtils.isNotBlank(adName),AdVehicle::getAdName, adName);
        return this.list(adVehicleQueryWrapper);
        /*String plateNumber = adVehicleVo.getPlateNumber();
        String driverName = adVehicleVo.getDriverName();
        String adName = adVehicleVo.getAdName();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String phone = sysUser.getPhone();
        if (StringUtils.isBlank(roleCode)){
            return List.of();
        }
        List<AdDriver> adDriverList = new ArrayList<>();
        QueryWrapper<AdVehicle> adVehicleQueryWrapper = new QueryWrapper<>();
        adVehicleQueryWrapper.lambda().eq(StringUtils.isNotBlank(plateNumber),AdVehicle::getPlateNumber, plateNumber);
        if (StringUtils.isNotBlank(driverName)) {
            //司机
            QueryWrapper<AdDriver> queryDriverWrapper = new QueryWrapper<>();
            queryDriverWrapper.lambda().in(AdDriver::getName, driverName);
            if (CommonConstant.ROLE_CODE_ADDRIVE.equals(roleCode)){
                queryDriverWrapper.lambda().eq(AdDriver::getPhone, phone);
            }
            List<AdDriver> adDrivers = adDriverService.list(queryDriverWrapper);
            adVehicleQueryWrapper.lambda().in(CollectionUtils.isNotEmpty(adDrivers),AdVehicle::getDriverId, adDrivers);
        }
        if (CommonConstant.ROLE_CODE_ADCOMPANY.equals(roleCode)) {
            //公司
        }
        if (CommonConstant.ROLE_CODE_ADVERTISERS.equals(roleCode)) {
            //广告方
            if (StringUtils.isNotBlank(adName)) {
                QueryWrapper<AdPublish> queryPublishWrapper = new QueryWrapper<>();
                queryPublishWrapper.lambda().eq(AdPublish::getTitle, adName);
                List<AdPublish> adPublishes = adPublishService.list(queryPublishWrapper);
                adVehicleQueryWrapper.lambda().in(CollectionUtils.isNotEmpty(adPublishes),AdVehicle::get, adDrivers);
            }
        }*/
//        return List.of();
    }
}
