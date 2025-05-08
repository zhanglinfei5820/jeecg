package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.*;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;
import org.jeecg.modules.ad.entity.Vo.AdVehicleInfoVO;
import org.jeecg.modules.ad.mapper.AdVehicleMapper;
import org.jeecg.modules.ad.service.*;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Slf4j
@Service
public class AdVehicleServiceImpl extends ServiceImpl<AdVehicleMapper, AdVehicle> implements IAdVehicleService {

    @Resource
    private IAdDriverService adDriverService;
    @Resource
    private IAdPublishService adPublishService;
    @Resource
    private IAdPublishDetailService adPublishDetailService;
    @Resource
    private IAdMerchantService adMerchantService;
    @Resource
    private AdVehicleMapper adVehicleMapper;
    @Resource
    private ISysDictService sysDictService;
    @Resource
    private IAdReportService adReportService;
    @Resource
    private ICommonLoginUserService commonLoginUserService;

    @Override
    public Map<String, Object> getVehicleStatistics(SysUser loginUser) {
        // 查询车辆统计数据，status不等于2
        Map<String, Object> statistics = new HashMap<>();
        String userId = loginUser.getRelatedId();
        List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
        // 查询车辆总数（status != 2）
        QueryWrapper<AdVehicle> totalQuery = new QueryWrapper<>();
        totalQuery.ne("status", CommonConstant.INTEGER_VALUE_2);
        // 获取所有类型的车辆列表（status != 2）
        List<AdVehicle> vehicleList = new ArrayList<>();
        if (roleCodes.contains(CommonConstant.ROLE_CODE_ADVERTISERS)) {
            //广告公司
            QueryWrapper<AdPublish> validQueryWrapper = new QueryWrapper<>();
            validQueryWrapper.eq("merchant_id", userId);
            validQueryWrapper.in("status", Arrays.asList(1, 4));
            List<AdPublish> adPublishes = adPublishService.list(validQueryWrapper);
            List<String> adIdList = adPublishes.stream().map(AdPublish::getId).collect(Collectors.toList());
            if (!adIdList.isEmpty()) {
                totalQuery.lambda().in(AdVehicle::getAdId, adIdList);
                long total = this.count(totalQuery);
                statistics.put("total", total);
                vehicleList.addAll(this.list(totalQuery));
            }else {
                statistics.put("total", 0);
            }
        }
        if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
            //网约车
            totalQuery.lambda().eq(AdVehicle::getCompanyId, userId);
            long total = this.count(totalQuery);
            statistics.put("total", total);
            vehicleList.addAll(this.list(totalQuery));
//            QueryWrapper<AdPublishDetail> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(AdPublishDetail::getCompanyId,userId).eq(AdPublishDetail::getType,0);
//            List<AdPublishDetail> adPublishDetailList = adPublishDetailService.list(queryWrapper);
//            List<String> publishIdList = adPublishDetailList.stream().map(AdPublishDetail::getPublishId).collect(Collectors.toList());
//            if (!publishIdList.isEmpty()) {
//                totalQuery.lambda().in(AdVehicle::getAdId, publishIdList);
//                long total = this.count(totalQuery);
//                statistics.put("total", total);
//                vehicleList.addAll(this.list(totalQuery));
//            }else {
//                statistics.put("total", 0);
//            }
        }
        // 从数据字典中获取所有车辆类型
        List<DictModel> dictList = sysDictService.queryDictItemsByCode("ad_vehicle_type");
        
        // 统计各类型车辆数量
        Map<String, Long> vehicleCountMap = vehicleList.stream()
                .collect(Collectors.groupingBy(
                        vehicle -> vehicle.getVehicleType().toString(), 
                        Collectors.counting()));
        
        // 创建最终的类型-数量映射，确保包含所有字典类型
        Map<String, Object> typeCountMap = new LinkedHashMap<>();
        for (DictModel dictModel : dictList) {
            String typeValue = dictModel.getValue();
            String typeText = dictModel.getText();
            // 获取当前类型的车辆数量，如果没有则为0
            Long count = vehicleCountMap.getOrDefault(typeValue, 0L);
            // typeCountMap.put(typeValue, count);
            // typeCountMap.put(typeValue + "_text", typeText);
            typeCountMap.put(typeValue, count);
        }
        
        statistics.put("typeCountMap", typeCountMap);
        
        return statistics;
    }
    
    @Override
    public IPage<AdVehicleVO> pageVO(Page<AdVehicle> page, QueryWrapper<AdVehicle> queryWrapper) {
        // 先查询原始数据
        IPage<AdVehicle> pageResult = this.page(page, queryWrapper);
        List<AdVehicle> vehicleList = pageResult.getRecords();
        
        if (CollectionUtils.isEmpty(vehicleList)) {
            // 如果没有数据，直接返回空页
            Page<AdVehicleVO> emptyPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), 0);
            emptyPage.setRecords(new ArrayList<>());
            return emptyPage;
        }
        
        // 创建VO分页对象
        Page<AdVehicleVO> pageVO = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        
        // 收集所有需要查询的ID
        Set<String> adIds = new HashSet<>();
        Set<String> companyIds = new HashSet<>();
        Set<String> driverIds = new HashSet<>();
        
        for (AdVehicle vehicle : vehicleList) {
            if (StringUtils.isNotBlank(vehicle.getAdId())) {
                adIds.add(vehicle.getAdId());
            }
            if (StringUtils.isNotBlank(vehicle.getCompanyId())) {
                companyIds.add(vehicle.getCompanyId());
            }
            if (StringUtils.isNotBlank(vehicle.getDriverId())) {
                driverIds.add(vehicle.getDriverId());
            }
        }
        
        // 批量查询关联数据
        final Map<String, String> adNameMap = getBatchAdNames(adIds);
        final Map<String, String> companyNameMap = getBatchCompanyNames(companyIds);
        final Map<String, String> driverNameMap = getBatchDriverNames(driverIds);
        
        // 转换为VO对象
        List<AdVehicleVO> voList = vehicleList.stream().map(vehicle -> {
            AdVehicleVO vo = new AdVehicleVO();
            // 复制基本属性
            BeanUtils.copyProperties(vehicle, vo);
            
            // 从映射中获取名称
            if (StringUtils.isNotBlank(vehicle.getAdId())) {
                vo.setAdName(adNameMap.getOrDefault(vehicle.getAdId(), ""));
            }
            
            if (StringUtils.isNotBlank(vehicle.getCompanyId())) {
                vo.setCompanyName(companyNameMap.getOrDefault(vehicle.getCompanyId(), ""));
            }
            
            if (StringUtils.isNotBlank(vehicle.getDriverId())) {
                vo.setDriverName(driverNameMap.getOrDefault(vehicle.getDriverId(), ""));
            }
            
            return vo;
        }).collect(Collectors.toList());
        
        pageVO.setRecords(voList);
        return pageVO;
    }

    @Override
    public List<AdVehicleVO> queryVxList(AdVehicleVO adVehicleVO) {
        // 构建查询参数
        Map<String, Object> queryParams = new HashMap<>();
        
        // 获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        
        // 添加来自VO对象的所有查询参数
        if (adVehicleVO != null) {
            // 处理基本字段
            if (adVehicleVO.getPlateNumber() != null) {
                queryParams.put("plateNumber", adVehicleVO.getPlateNumber());
            }
            if (adVehicleVO.getSysOrgCode() != null) {
                queryParams.put("sysOrgCode", adVehicleVO.getSysOrgCode());
            }
            if (adVehicleVO.getVehicleType() != null) {
                queryParams.put("vehicleType", adVehicleVO.getVehicleType());
            }
            if (adVehicleVO.getBrand() != null) {
                queryParams.put("brand", adVehicleVO.getBrand());
            }
            if (adVehicleVO.getStatus() != null) {
                queryParams.put("status", adVehicleVO.getStatus());
            }
            if (adVehicleVO.getKeyword() != null) {
                queryParams.put("keyword", adVehicleVO.getKeyword());
            }
            if (adVehicleVO.getOrderBy() != null) {
                queryParams.put("orderBy", adVehicleVO.getOrderBy());
            }

            // 处理关联表字段
            if (adVehicleVO.getAdName() != null) {
                queryParams.put("adName", adVehicleVO.getAdName());
            }
            if (adVehicleVO.getCompanyName() != null) {
                queryParams.put("companyName", adVehicleVO.getCompanyName());
            }
            if (adVehicleVO.getDriverName() != null) {
                queryParams.put("driverName", adVehicleVO.getDriverName());
            }
            
            // 处理安装时间范围
            if (adVehicleVO.getInstallationTime() != null) {
                queryParams.put("installationTimeStart", adVehicleVO.getInstallationTime());
                // 可以设置结束时间为当前时间，或者从请求中获取
            }
        }
        
        // 根据不同角色设置不同的查询条件
        if (CommonConstant.ROLE_CODE_ADVERTISERS.contains(roleCode)) {
            // 广告方
            queryParams.put("merchantId", sysUser.getRelatedId());
        }
        if (CommonConstant.ROLE_CODE_ADCOMPANY.contains(roleCode)) {
            // 公司
            queryParams.put("companyId", sysUser.getRelatedId());
        }
        if (CommonConstant.ROLE_CODE_ADDRIVE.contains(roleCode)) {
            // 司机
            queryParams.put("driverId", sysUser.getRelatedId());
        }
        
        // 直接调用Mapper的方法，使用XML中定义的SQL语句进行一次性联合查询
        return adVehicleMapper.queryVxList(queryParams);
    }
    
    /**
     * 批量获取广告名称
     * @param adIds 广告ID集合
     * @return 广告ID到名称的映射
     */
    private Map<String, String> getBatchAdNames(Set<String> adIds) {
        if (adIds.isEmpty()) {
            return new HashMap<>();
        }
        
        List<AdPublish> adPublishList = adPublishService.listByIds(adIds);
        return adPublishList.stream().collect(
            Collectors.toMap(AdPublish::getId, AdPublish::getName, (a, b) -> a));
    }
    
    /**
     * 批量获取公司名称
     * @param companyIds 公司ID集合
     * @return 公司ID到名称的映射
     */
    private Map<String, String> getBatchCompanyNames(Set<String> companyIds) {
        if (companyIds.isEmpty()) {
            return new HashMap<>();
        }
        
        Map<String, String> companyNameMap = new HashMap<>();
        for (String companyId : companyIds) {
            // 使用getCompanyNameById方法，该方法内部已经实现了缓存
            String companyName = adMerchantService.getCompanyNameById(companyId);
            companyNameMap.put(companyId, companyName);
        }
        return companyNameMap;
    }
    
    /**
     * 批量获取司机名称
     * @param driverIds 司机ID集合
     * @return 司机ID到名称的映射
     */
    private Map<String, String> getBatchDriverNames(Set<String> driverIds) {
        if (driverIds.isEmpty()) {
            return new HashMap<>();
        }
        
        List<AdDriver> driverList = adDriverService.listByIds(driverIds);
        return driverList.stream().collect(
            Collectors.toMap(AdDriver::getId, AdDriver::getName, (a, b) -> a));
    }

    /**
     * 通过XML SQL直接分页查询车辆信息
     * @param page 分页参数
     * @param queryParams 查询参数
     * @return 包含关联信息的车辆VO分页结果
     */
    @Override
    public IPage<AdVehicleVO> queryVehiclePageList(Page<AdVehicleVO> page, Map<String, Object> queryParams) {
        // 直接调用Mapper的方法，使用XML中定义的SQL语句进行一次性联合查询
        return adVehicleMapper.queryVehiclePageList(page, queryParams);
    }
    
    /**
     * 小程序年检维护分页查询
     * 多表联查，关联车辆、年检、广告、公司信息
     * 根据ad_inspection表的type字段和其他业务条件进行查询
     * 
     * @param page 分页参数
     * @param queryParams 查询参数，包含年检类型、车牌号、车辆类型等
     * @return 包含车辆和年检信息的分页结果
     */
    @Override
    public IPage<AdVehicleVO> queryInspectionMaintenancePageList(Page<AdVehicleVO> page, Map<String, Object> queryParams) {
        // 登录用户相关处理
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String relatedId = sysUser.getRelatedId();
        List<String> adIdList  = new ArrayList<>();
//        boolean expireTimeBoolean = false;
//        String expireTime = queryParams.get("expireTime").toString();
//        if (StringUtils.isNotBlank(expireTime)) {
//            expireTimeBoolean = true;
//            queryParams.put("expireTime", "");
//        }
        // 根据角色区分查询逻辑
        // 根据角色区分查询逻辑
        if (CommonConstant.ROLE_CODE_ADVERTISERS.contains(roleCode)) {
            QueryWrapper<AdPublish> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("merchant_id", relatedId);
            List<AdPublish> adPublishList = adPublishService.list(queryWrapper);
            adIdList.addAll(adPublishList.stream().map(AdPublish::getId).collect(Collectors.toList()));
        }
        if (CommonConstant.ROLE_CODE_ADCOMPANY.contains(roleCode)) {
            QueryWrapper<AdPublishDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_id", relatedId);
            queryWrapper.eq("type", 0);
            List<AdPublishDetail> adPublishDetailList = adPublishDetailService.list(queryWrapper);
            adIdList.addAll(adPublishDetailList.stream().map(AdPublishDetail::getPublishId).collect(Collectors.toList()));
        }
        // 根据不同角色设置不同的查询条件（权限过滤）
        /*if (CommonConstant.ROLE_CODE_ADVERTISERS.contains(roleCode)) {
            // 广告方
            queryParams.put("adId", sysUser.getRelatedId());
        }
        if (CommonConstant.ROLE_CODE_ADCOMPANY.contains(roleCode)) {
            // 公司
            queryParams.put("companyId", sysUser.getRelatedId());
        }
        if (CommonConstant.ROLE_CODE_ADDRIVE.contains(roleCode)) {
            // 司机
            queryParams.put("driverId", sysUser.getRelatedId());
        }*/
        if (adIdList.isEmpty()) {
            return new Page<>();
        }
        queryParams.put("adIdList", adIdList);
        // 直接调用Mapper的方法，使用XML中定义的SQL语句进行一次性联合查询
        // 避免多次数据库查询，提高性能
        return adVehicleMapper.queryInspectionMaintenancePageList(page, queryParams);
    }

    @Override
    public Long countEnabledVehicles(String publishId) {
        // 获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String roleCode = sysUser.getRoleCode();
        String relatedId = sysUser.getRelatedId();
        
        if (CommonConstant.ROLE_CODE_ADCOMPANY.contains(roleCode)) {
            // 1. 根据publishId和company_id查询AdPublishDetail表里面已报名车辆数
            QueryWrapper<AdPublishDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("publish_id", publishId)
                              .eq("company_id", relatedId)
                              .eq("status", 1)  // 状态为启用
                              .eq("type", 0);   // 类型为公司
            
            List<AdPublishDetail> publishDetails = adPublishDetailService.list(detailQueryWrapper);
            
            // 计算已报名司机总数
            int totalSignedDrivers = 0;
            if (publishDetails != null && !publishDetails.isEmpty()) {
                totalSignedDrivers = publishDetails.stream()
                    .mapToInt(detail -> detail.getDrivers() != null ? detail.getDrivers() : 0)
                    .sum();
            }
            
            // 2. 获取公司下状态为1（启用）的车辆总数
            QueryWrapper<AdVehicle> vehicleQueryWrapper = new QueryWrapper<>();
            vehicleQueryWrapper.eq("company_id", relatedId)
                               .eq("status", 0);  // 状态为启用
            
            long totalEnabledVehicles = this.count(vehicleQueryWrapper);
            
            // 3. 计算可用车辆数 = 启用车辆总数 - 已报名司机总数
            long availableVehicles = totalEnabledVehicles - totalSignedDrivers;
            
            // 如果结果为负数，则返回0
            return Math.max(availableVehicles, 0L);
        }
        
        return 0L;
    }

    @Override
    @Transactional
    public void editAdVehicle(AdVehicle adVehicle) {
        // 判断车辆状态是否为1(启用)，如果是则执行后续逻辑
        if (adVehicle != null && adVehicle.getStatus() != null && adVehicle.getStatus() == 1) {
            Date installationTime = adVehicle.getInstallationTime();
            if (installationTime == null){
                throw new RuntimeException("设置安装状态，安装时间不能为空");
            }
            log.info("车辆状态为启用，执行后续查询和处理逻辑");
            adVehicle.setMaintenanceCount(0);
            // 获取车辆ID和司机ID
            String vehicleId = adVehicle.getId();
            String driverId = adVehicle.getDriverId();

            if (vehicleId == null || driverId == null) {
                log.warn("车辆ID或司机ID为空，无法继续处理");
                throw new RuntimeException("车辆ID或司机ID为空，无法继续处理");
            }

            try {
                // 查询adPublishDetail数据
                // 构建查询条件：查询与该车辆、司机相关的广告发布明细记录
                QueryWrapper<AdPublishDetail> detailQueryWrapper = new QueryWrapper<>();
                detailQueryWrapper.eq("vehicle_id", vehicleId)
                        .eq("driver_id", driverId)
                        .eq("type", 1)      // 类型为1(司机)
                        .eq("status", 3);   // 状态为3

                // 执行查询
                List<AdPublishDetail> publishDetails = adPublishDetailService.list(detailQueryWrapper);

                // 如果有符合条件的广告发布明细记录，则处理每一条
                if (publishDetails != null && !publishDetails.isEmpty()) {
                    log.info("查询到{}条符合条件的广告发布明细记录", publishDetails.size());

                    for (AdPublishDetail detail : publishDetails) {
                        String publishDetailId = detail.getId();
                        String publishId = detail.getPublishId();

                        // 查询是否已存在相关的AdReport记录
                        QueryWrapper<AdReport> reportQueryWrapper = new QueryWrapper<>();
                        reportQueryWrapper.eq("driver_id", driverId)
                                .eq("publish_detail_id", publishDetailId)
                                .eq("status", 0); // 状态为0(待处理)

                        // 执行查询
                        long count = adReportService.count(reportQueryWrapper);

                        // 如果没有找到对应的记录，则需要创建新的AdReport
                        if (count == 0) {
                            log.info("未找到driverId={}和publishDetailId={}的对应AdReport记录，创建新记录", driverId, publishDetailId);

                            // 获取广告信息，以便获取投放开始时间
                            AdPublish adPublish = adPublishService.getById(publishId);
                            if (adPublish == null) {
                                log.warn("未找到publishId={}的广告信息，跳过创建AdReport", publishId);
                                throw new RuntimeException("未找到publishId="+publishId+"的广告信息，跳过创建AdReport");
                            }

                            // 创建AdReport对象
                            AdReport adReport = new AdReport();

                            // 设置基本信息
                            adReport.setDriverId(driverId);
                            adReport.setPublishDetailId(publishDetailId);
                            adReport.setStatus(0); // 设置状态为0(待处理)
                            adReport.setMaintenanceCount(0);
                            // 计算上报时间
                            Date launchStartTime = adPublish.getLaunchStartTime();
                            Date launchEndTime = adPublish.getLaunchEndTime();
                            if (installationTime.after(launchEndTime)){
                                throw new RuntimeException("安装时间不能在投放结束之间之后！");
                            }
                            Date reportTime = calculateReportTime(installationTime, launchStartTime, launchEndTime);
                            adReport.setReportTime(reportTime);
                            adReport.setReportType(1);
                            // 保存AdReport对象
                            boolean saveResult = adReportService.save(adReport);
                            if (saveResult) {
                                log.info("成功创建AdReport记录，driverId={}，publishDetailId={}, reportTime={}",
                                        driverId, publishDetailId, reportTime);
                            } else {
                                log.error("创建AdReport记录失败");
                                throw new RuntimeException("创建AdReport记录失败");
                            }
                        } else {
                            log.info("已存在driverId={}和publishDetailId={}的AdReport记录，跳过创建", driverId, publishDetailId);
                        }
                    }
                } else {
                    log.info("未查询到符合条件的广告发布明细记录");
                    throw new RuntimeException("未查询到符合条件的广告发布明细记录");
                }
            } catch (Exception e) {
                log.error("处理AdReport逻辑时发生异常", e);
                throw new RuntimeException("处理AdReport逻辑时发生异常:" + e.getMessage());
            }
        }
        // 更新车辆信息
        adVehicleMapper.updateById(adVehicle);
    }

    /**
     * 计算上报时间
     *
     * @param launchStartTime 广告投放开始时间
     * @param launchEndTime 广告投放结束时间
     * @return 计算得到的上报时间
     */
    private Date calculateReportTime(Date installationTime,Date launchStartTime, Date launchEndTime) {
//        Date now = new Date();
        Date baseDate = installationTime;
//
//        // 判断当前日期是否在投放开始时间之内
//        if (launchStartTime != null && now.after(launchStartTime)) {
//            // 当前日期在投放开始时间之后，以当前日期为起点
//            baseDate = now;
//        } else if (launchStartTime != null) {
//            // 当前日期在投放开始时间之前，以投放开始时间为起点
//            baseDate = launchStartTime;
//        } else {
//            // 如果投放开始时间为空，则使用当前时间
//            baseDate = now;
//        }
        if(launchStartTime != null && launchStartTime.after(installationTime)){
            baseDate = launchStartTime;
        }

        // 创建日历对象，用于日期计算
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);

        // 将时间往后推一个月
        calendar.add(Calendar.MONTH, 1);

        // 设置时间为00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date calculatedDate = calendar.getTime();

        // 如果计算出的日期比广告投放结束时间还要大，就返回广告投放结束时间
        if (launchEndTime != null && calculatedDate.after(launchEndTime)) {
            return launchEndTime;
        }

        return calculatedDate;
    }

}