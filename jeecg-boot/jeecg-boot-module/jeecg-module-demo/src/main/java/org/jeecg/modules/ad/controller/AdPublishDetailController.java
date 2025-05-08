package org.jeecg.modules.ad.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.*;
import org.jeecg.modules.ad.entity.Vo.AdPublishDetailVO;
import org.jeecg.modules.ad.service.*;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

 /**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告发布明细表")
@RestController
@RequestMapping("/ad/adPublishDetail")
@Slf4j
public class AdPublishDetailController extends JeecgController<AdPublishDetail, IAdPublishDetailService> {
	@Autowired
	private IAdPublishDetailService adPublishDetailService;
	@Autowired
	private IAdPublishService adPublishService;
	@Autowired
	private IAdVehicleService adVehicleService;
	@Autowired
	private IAdDriverService adDriverService;
	@Resource
	private ICommonLoginUserService commonLoginUserService;
	@Resource
	private IAdMerchantService iAdMerchantService;
	@Resource
	private IAdCompanyService iAdCompanyService;

	/**
	 * 分页列表查询
	 *
	 * @param adPublishDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告发布明细表-分页列表查询")
	@Operation(summary="广告发布明细表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdPublishDetail>> queryPageList(AdPublishDetail adPublishDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		SysUser loginUser = commonLoginUserService.getLoginUserInfo(req);
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
//		if (CommonConstant.ADMIN.equals(loginUser.getUsername())) {
//			adPublishDetail.setType(null);
//		}
		String relatedId = loginUser.getRelatedId();
        QueryWrapper<AdPublishDetail> queryWrapper = QueryGenerator.initQueryWrapper(adPublishDetail, req.getParameterMap());
		if (!CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
			if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				queryAdPulishWrapper.lambda().eq(AdPublishDetail::getCompanyId, relatedId)
						.eq(AdPublishDetail::getType,adPublishDetail.getType());
				List<AdPublishDetail> adPublishDetailList = adPublishDetailService.list(queryAdPulishWrapper);
				List<String> publishDetailIdList = adPublishDetailList.stream().map(AdPublishDetail::getId).collect(Collectors.toList());
				if (publishDetailIdList.isEmpty()) {
					return Result.OK(new Page<>());
				}
				queryWrapper.lambda().in(AdPublishDetail::getId,publishDetailIdList);
			}
		}
		Page<AdPublishDetail> page = new Page<AdPublishDetail>(pageNo, pageSize);
		IPage<AdPublishDetail> pageList = adPublishDetailService.page(page, queryWrapper);
		
		// 增加返回数据：公司名称、发布广告的名称
		List<AdPublishDetail> records = pageList.getRecords();
		if(records != null && !records.isEmpty()) {
			// 收集所有发布ID
			List<String> publishIds = records.stream()
				.map(AdPublishDetail::getPublishId)
				.distinct()
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.toList());
				
			// 批量查询发布广告信息
			Map<String, AdPublish> publishMap = new HashMap<>();
			if(!publishIds.isEmpty()) {
				List<AdPublish> publishList = adPublishService.listByIds(publishIds);
				publishMap = publishList.stream()
					.collect(Collectors.toMap(AdPublish::getId, publish -> publish, (k1, k2) -> k1));
			}
			
			// 收集所有涉及的车辆ID
			List<String> vehicleIds = records.stream()
				.map(AdPublishDetail::getVehicleId)
				.distinct()
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.toList());
				
			// 批量查询车辆信息
			Map<String, AdVehicle> vehicleMap = new HashMap<>();
			if(!vehicleIds.isEmpty()) {
				List<AdVehicle> vehicleList = adVehicleService.listByIds(vehicleIds);
				vehicleMap = vehicleList.stream()
					.collect(Collectors.toMap(AdVehicle::getId, vehicle -> vehicle, (k1, k2) -> k1));
			}
			
			// 收集所有司机ID
			List<String> driverIds = records.stream()
				.map(AdPublishDetail::getDriverId)
				.distinct()
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.toList());
				
			// 批量查询司机信息
			Map<String, AdDriver> driverMap = new HashMap<>();
			if(!driverIds.isEmpty()) {
				List<AdDriver> driverList = adDriverService.listByIds(driverIds);
				driverMap = driverList.stream()
					.collect(Collectors.toMap(AdDriver::getId, driver -> driver, (k1, k2) -> k1));
			}
			
			// 收集所有公司ID
			List<String> companyIds = records.stream()
				.map(AdPublishDetail::getCompanyId)
				.distinct()
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.toList());
				
			// 批量查询公司信息
			Map<String, AdCompany> companyMap = new HashMap<>();
			if(!companyIds.isEmpty()) {
				// 使用AdCompany服务
				List<AdCompany> companyList = iAdCompanyService.listByIds(companyIds);
				companyMap = companyList.stream()
					.collect(Collectors.toMap(AdCompany::getId, company -> company, (k1, k2) -> k1));
			}
			
			// 设置相关信息
			for(AdPublishDetail detail : records) {
				// 设置发布广告名称
				if(detail.getPublishId() != null && publishMap.containsKey(detail.getPublishId())) {
					detail.setName(publishMap.get(detail.getPublishId()).getName());
				}

				// 司机名称已在实体中定义
				if(detail.getDriverId() != null && driverMap.containsKey(detail.getDriverId())) {
					detail.setDriverName(driverMap.get(detail.getDriverId()).getName());
				}

				// 车牌号
				if(detail.getVehicleId() != null && vehicleMap.containsKey(detail.getVehicleId())) {
					// 假设AdPublishDetail中新增了plateNumber字段
					AdVehicle vehicle = vehicleMap.get(detail.getVehicleId());
					detail.setPlateNumber(vehicle.getPlateNumber());
				}

				// 公司名称
				if(detail.getCompanyId() != null && companyMap.containsKey(detail.getCompanyId())) {
					// 使用商户名称
					AdCompany adCompany = companyMap.get(detail.getCompanyId());
					if(adCompany.getName() != null) {
						detail.setCompanyName(adCompany.getName());
					}
				}
			}
		}
		
		return Result.OK(pageList);
	}

	 @Operation(summary="广告发布明细表-分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<IPage<AdPublishDetailVO>> queryPageList2(AdPublishDetailVO adPublishDetailVO,
														  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														  HttpServletRequest req) {
		 SysUser loginUser = commonLoginUserService.getLoginUserInfo(req);
		 if (loginUser == null) {
			 return Result.error("用户未登录");
		 }
		 if (CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			 adPublishDetailVO.setType(null);
		 }
		 Map<String, Object> queryParams = new HashMap<>();
		 String relatedId = loginUser.getRelatedId();
		 if (!CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			 List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
			 if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				 QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				 queryAdPulishWrapper.lambda().eq(AdPublishDetail::getCompanyId, relatedId)
						 .eq(AdPublishDetail::getType,adPublishDetailVO.getType());
				 List<AdPublishDetail> adPublishDetailList = adPublishDetailService.list(queryAdPulishWrapper);
				 List<String> publishDetailIdList = adPublishDetailList.stream().map(AdPublishDetail::getId).collect(Collectors.toList());
				 if (publishDetailIdList.isEmpty()) {
					 return Result.OK(new Page<>());
				 }
				 queryParams.put("publishDetailIdList", publishDetailIdList);
			 }
		 }
		 queryParams.put("type",adPublishDetailVO.getType());
		 queryParams.put("publish_id",adPublishDetailVO.getPublishId());
		 queryParams.put("ad_name",adPublishDetailVO.getAdName());
		 queryParams.put("name",adPublishDetailVO.getName());
		 queryParams.put("driver_name",adPublishDetailVO.getDriverName());
		 queryParams.put("status",adPublishDetailVO.getStatus());
		 Page<AdPublishDetailVO> page = new Page<>(pageNo, pageSize);
		 IPage<AdPublishDetailVO> pageList = adPublishDetailService.queryPulishDetailPageList(page, queryParams);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param adPublishDetail
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-添加")
	@Operation(summary="广告发布明细表-添加")
	@RequiresPermissions("ad:ad_publish_detail:add")
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdPublishDetail adPublishDetail) {
		adPublishDetailService.save(adPublishDetail);
		adPublishDetailService.distribute(adPublishDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adPublishDetail
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-编辑")
	@Operation(summary="广告发布明细表-编辑")
	@RequiresPermissions("ad:ad_publish_detail:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdPublishDetail adPublishDetail) {
		adPublishDetailService.updateById(adPublishDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-通过id删除")
	@Operation(summary="广告发布明细表-通过id删除")
	@RequiresPermissions("ad:ad_publish_detail:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adPublishDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-批量删除")
	@Operation(summary="广告发布明细表-批量删除")
	@RequiresPermissions("ad:ad_publish_detail:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adPublishDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告发布明细表-通过id查询")
	@Operation(summary="广告发布明细表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdPublishDetail> queryById(@RequestParam(name="id",required=true) String id) {
		AdPublishDetail adPublishDetail = adPublishDetailService.getById(id);
		if(adPublishDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adPublishDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adPublishDetail
    */
    @RequiresPermissions("ad:ad_publish_detail:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdPublishDetail adPublishDetail) {
        return super.exportXls(request, adPublishDetail, AdPublishDetail.class, "广告发布明细表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_publish_detail:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdPublishDetail.class);
    }

	/**
	 * 公司分发车辆接口
	 *
	 * @param params 参数，包含publishId(广告ID)、id(公司AdPublishDetail ID)、vehicleList(车辆信息列表)
	 * @return 操作结果
	 */
	@AutoLog(value = "广告发布明细表-公司分发车辆")
	@Operation(summary = "广告发布明细表-公司分发车辆")
	@PostMapping(value = "/companyDistributeVehicles")
	@Transactional(rollbackFor = Exception.class)
	public Result<String> companyDistributeVehicles(@RequestBody Map<String, Object> params) {
		log.info("公司分发车辆参数: {}", params);
		
		// 1. 获取参数
		String publishId = (String) params.get("publishId");
		String detailId = (String) params.get("id");
		List<Map<String, Object>> vehicleMapList = (List<Map<String, Object>>) params.get("vehicleList");
		
		if (publishId == null || detailId == null || vehicleMapList == null || vehicleMapList.isEmpty()) {
			return Result.error("参数错误，广告ID、明细ID和车辆列表不能为空");
		}
		
		// 2. 获取当前登录用户信息
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String companyId = loginUser.getRelatedId();
		
		try {
			// 3. 一次性获取所有所需数据，减少数据库访问
			// 3.1 获取广告信息
			AdPublish adPublish = adPublishService.getById(publishId);
			if (adPublish == null) {
				return Result.error("未找到相应的广告信息");
			}
			
			// 3.2 获取公司的AdPublishDetail对象
			AdPublishDetail companyDetail = adPublishDetailService.getById(detailId);
			if (companyDetail == null) {
				return Result.error("未找到公司的广告发布明细");
			}
			
			// 检查明细是否属于当前登录公司
			if (!companyId.equals(companyDetail.getCompanyId())) {
				return Result.error("无权操作此广告发布明细");
			}
			
			// 3.3 提取所有车辆ID
			List<String> vehicleIds = vehicleMapList.stream()
					.map(v -> (String) v.get("id"))
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			
			if (vehicleIds.isEmpty()) {
				return Result.error("未提供有效的车辆ID");
			}
			
			// 3.4 批量获取车辆信息
			List<AdVehicle> vehicles = adVehicleService.listByIds(vehicleIds);
			Map<String, AdVehicle> vehicleMap = vehicles.stream()
					.collect(Collectors.toMap(AdVehicle::getId, v -> v, (v1, v2) -> v1));
			
			// 3.5 检查已分发车辆数量
			// 查询当前公司该广告已分发的车辆数量
			QueryWrapper<AdPublishDetail> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("publish_id", publishId)
					   .eq("company_id", companyId)
					   .eq("type", 1); // 类型为司机的明细记录
			
			long alreadyDistributedCount = adPublishDetailService.count(queryWrapper);
			
			// 获取公司能分发的总车辆数
			Integer totalDrivers = companyDetail.getDrivers();
			if (totalDrivers == null) {
				totalDrivers = 0;
			}
			
			// 计算还能分发的车辆数
			int remainingCount = totalDrivers - (int)alreadyDistributedCount;
			
			// 检查是否有足够的配额来分发
			if (remainingCount < vehicleIds.size()) {
				return Result.error("分发车辆数量不足，当前还能分发" + remainingCount + "辆车，但请求分发" + vehicleIds.size() + "辆车");
			}
			
			// 4. 构建批量保存的AdPublishDetail列表
			List<AdPublishDetail> detailList = new ArrayList<>();
			
			// 计算分成比例
//			BigDecimal percentage = companyDetail.getPercentage() == null ?
//					BigDecimal.valueOf(100) : companyDetail.getPercentage();
//			BigDecimal percentageFactor = percentage.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
			
			// 收集所有司机ID
			Set<String> driverIds = vehicles.stream()
					.map(AdVehicle::getDriverId)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			
			// 批量查询司机信息
			Map<String, String> driverNameMap = new HashMap<>();
			if (!driverIds.isEmpty()) {
				List<AdDriver> drivers = adDriverService.listByIds(driverIds);
				driverNameMap = drivers.stream()
						.collect(Collectors.toMap(AdDriver::getId, AdDriver::getName, (v1, v2) -> v1));
			}
			
			// 循环创建司机明细
			for (AdVehicle vehicle : vehicles) {
				String vehicleId = vehicle.getId();
				if (vehicle == null) {
					log.warn("未找到ID为{}的车辆信息", vehicleId);
					continue;
				}
				// 创建司机明细对象
				AdPublishDetail driverDetail = new AdPublishDetail();
				driverDetail.setPublishId(publishId);
				driverDetail.setPosition(adPublish.getPosition());

				// 计算司机的价格 = 广告单价 - (广告单价 * 公司分成比例)
				BigDecimal adPrice = adPublish.getPrice();
//				BigDecimal companyShare = adPrice.multiply(percentageFactor);
				BigDecimal companyShare = adPrice.subtract(companyDetail.getPercentage());
				BigDecimal driverPrice = adPrice.subtract(companyShare);
				driverDetail.setPrice(driverPrice);
				driverDetail.setPercentage(companyDetail.getPercentage());
				driverDetail.setStatus(3);  // 状态设置为3
				driverDetail.setType(1);    // 类型设置为1(司机)
				driverDetail.setDrivers(1);  // 默认为1
				driverDetail.setDriverId(vehicle.getDriverId());

				// 设置司机名称
				if (vehicle.getDriverId() != null) {
					driverDetail.setDriverName(driverNameMap.getOrDefault(vehicle.getDriverId(), ""));
				}

				driverDetail.setCompanyId(companyId);
				driverDetail.setVehicleId(vehicleId);
				driverDetail.setName(adPublish.getName());

				detailList.add(driverDetail);
				//待安装
				vehicle.setStatus(4);
				//重置维修金次数
				vehicle.setMaintenanceCount(0);
			}

			if (detailList.isEmpty()) {
				return Result.error("没有有效的车辆信息可以分发");
			}
			
			// 5. 批量保存AdPublishDetail对象
			boolean saveResult = adPublishDetailService.saveBatch(detailList);
			if (!saveResult) {
				return Result.error("保存司机广告发布明细失败");
			}
			boolean updateBatchById = adVehicleService.updateBatchById(vehicles);
			if (!updateBatchById) {
				return Result.error("保存司机广告发布明细失败");
			}
			// 更新选中车辆的状态为3
			if (!vehicleIds.isEmpty()) {
				List<AdVehicle> updateVehicles = new ArrayList<>();
				for (String vehicleId : vehicleIds) {
					AdVehicle vehicle = vehicleMap.get(vehicleId);
					if (vehicle != null) {
						vehicle.setAdId(publishId);
						vehicle.setStatus(3); // 将车辆状态更新为3
						updateVehicles.add(vehicle);
					}
				}
				
				if (!updateVehicles.isEmpty()) {
					boolean updateVehiclesResult = adVehicleService.updateBatchById(updateVehicles);
					if (!updateVehiclesResult) {
						log.warn("更新车辆状态失败，但分发流程已完成");
					} else {
						log.info("成功更新{}辆车状态为3", updateVehicles.size());
					}
				}
			}
			
			// 6. 分发后检查是否已完成全部分发
			int newTotalDistributed = (int)alreadyDistributedCount + detailList.size();
			
			// 如果分配的车辆数等于公司能分配的司机总数，则修改公司广告实体状态为3
			if (newTotalDistributed == totalDrivers) {
				companyDetail.setStatus(3); // 更新公司明细状态为3
				boolean updateResult = adPublishDetailService.updateById(companyDetail);
				if (!updateResult) {
					log.warn("更新公司广告明细状态失败, id: {}", detailId);
				}
				return Result.OK("分发成功，已完成所有车辆分发");
			}
			
			return Result.OK("分发成功，共分发给" + detailList.size() + "个司机，还剩" + (totalDrivers - newTotalDistributed) + "辆车未分发");
			
		} catch (Exception e) {
			log.error("分发车辆异常", e);
			return Result.error("分发失败：" + e.getMessage());
		}
	}

	/**
	 * 公司报名接口
	 *
	 * @param params 参数，包含广告ID(publishId)和报名车辆数(drivers)
	 * @return 操作结果
	 */
	@AutoLog(value = "公司报名广告")
	@Operation(summary = "公司报名广告")
	@PostMapping(value = "/companySignup")
	@Transactional(rollbackFor = Exception.class)
	public Result<String> companySignup(@RequestBody Map<String, Object> params) {
		log.info("公司报名广告参数: {}", params);
		
		// 1. 获取参数
		String publishId = (String) params.get("publishId");
		Integer drivers = Integer.parseInt(params.get("drivers").toString());
		
		if (publishId == null || drivers == null || drivers <= 0) {
			return Result.error("参数错误，广告ID和报名车辆数必须提供且车辆数必须大于0");
		}
		
		// 2. 获取当前登录用户信息
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String companyId = loginUser.getRelatedId();
		
		// 3. 检查可用车辆数量
		Long enabledVehiclesCount = adVehicleService.countEnabledVehicles(publishId);
		if (enabledVehiclesCount < drivers) {
			return Result.error("报名车辆数超过可选车辆数，请刷新查看");
		}
		
		// 4. 获取广告信息
		AdPublish adPublish = adPublishService.getById(publishId);
		if (adPublish == null) {
			return Result.error("未找到相应的广告信息");
		}
		
		// 5. 创建新的广告发布明细对象
		AdPublishDetail detail = new AdPublishDetail();
		detail.setPublishId(publishId);
		detail.setPosition(adPublish.getPosition());
		detail.setPrice(adPublish.getPrice());
		detail.setStatus(0); // 设置状态为启用
		detail.setType(0);   // 设置类型为公司
		detail.setDrivers(drivers);
		detail.setCompanyId(companyId);
		//detail.setTitle(adPublish.getName());
		detail.setName(adPublish.getName());
		
		// 6. 保存广告发布明细
		boolean saveResult = adPublishDetailService.save(detail);
		if (!saveResult) {
			return Result.error("保存广告报名信息失败");
		}
		
		// 7. 更新广告表的实际司机数
		BigDecimal currentActualDrivers = adPublish.getActualDrivers() == null ? 
				BigDecimal.ZERO : adPublish.getActualDrivers();
		BigDecimal newActualDrivers = currentActualDrivers.add(new BigDecimal(drivers));
		
		adPublish.setActualDrivers(newActualDrivers);
		boolean updateResult = adPublishService.updateById(adPublish);
		if (!updateResult) {
			return Result.error("更新广告实际司机数失败");
		}
		
		return Result.OK("报名成功");
	}

	/**
	 *  抽成
	 *
	 * @param adPublishDetail
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-抽成")
	@Operation(summary="广告发布明细表-抽成")
	@RequestMapping(value = "/distribute", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> distribute(@RequestBody AdPublishDetail adPublishDetail) {
		try {
			String result = adPublishDetailService.distribute(adPublishDetail);
			return Result.OK(result);
		} catch (Exception e) {
			log.error("抽成广告失败", e);
			return Result.error("抽成失败：" + e.getMessage());
		}
	}
	/**
	 *  安装
	 *
	 * @param adPublishDetailVO
	 * @return
	 */
	@AutoLog(value = "司机任务-安装")
	@Operation(summary="司机任务-安装")
	@RequestMapping(value = "/installation", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> installation(@RequestBody AdPublishDetailVO adPublishDetailVO) {
		try {
			String result = adPublishDetailService.installation(adPublishDetailVO);
			return Result.OK(result);
		} catch (Exception e) {
			log.error("司机任务安装失败", e);
			return Result.error("司机任务安装失败：" + e.getMessage());
		}
	}
}
