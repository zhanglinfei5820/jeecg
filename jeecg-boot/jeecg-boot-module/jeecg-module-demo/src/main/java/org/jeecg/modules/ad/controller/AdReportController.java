package org.jeecg.modules.ad.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ad.entity.*;
import org.jeecg.modules.ad.entity.Vo.AdReportTaskVO;
import org.jeecg.modules.ad.service.*;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 广告上报记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告上报记录表")
@RestController
@RequestMapping("/ad/adReport")
@Slf4j
public class AdReportController extends JeecgController<AdReport, IAdReportService> {
	@Autowired
	private IAdReportService adReportService;
	 @Resource
	 private ICommonLoginUserService commonLoginUserService;
	 @Resource
	 private IAdPublishDetailService iAdPublishDetailService;
	 @Resource
	 private IAdPublishService iAdPublishService;
	 @Resource
	 private IAdMerchantService iAdMerchantService;

	/**
	 * 分页列表查询
	 *
	 * @param adReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告上报记录表-分页列表查询")
	@Operation(summary="广告上报记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdReport>> queryPageList(AdReport adReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		SysUser loginUser = commonLoginUserService.getLoginUserInfo(req);
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
		String relatedId = loginUser.getRelatedId();
		QueryWrapper<AdReport> queryWrapper = QueryGenerator.initQueryWrapper(adReport, req.getParameterMap());
		if (!CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
			if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				//网约车公司
				QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				queryAdPulishWrapper.eq("company_id", relatedId);
				List<AdPublishDetail> adPublishDetailList = iAdPublishDetailService.list(queryAdPulishWrapper);
				List<String> publishDetailIdList = adPublishDetailList.stream().map(AdPublishDetail::getId).collect(Collectors.toList());
				if (publishDetailIdList.isEmpty()) {
					return Result.OK(new Page<>());
				}
				queryWrapper.lambda().in(AdReport::getPublishDetailId,publishDetailIdList);
			}
			if (roleCodes.contains(CommonConstant.ROLE_CODE_ADVERTISERS)) {
				//广告公司
				AdMerchant adMerchant = iAdMerchantService.getById(relatedId);
				if (adMerchant == null) {
					return Result.OK(new Page<>());
				}
				QueryWrapper<AdPublish> queryAdPublishWrapper = new QueryWrapper<>();
				queryAdPublishWrapper.lambda().eq(AdPublish::getMerchantId,adMerchant.getId());
				List<AdPublish> adPublishes = iAdPublishService.list(queryAdPublishWrapper);
				List<String> pulishIds = adPublishes.stream().map(AdPublish::getId).collect(Collectors.toList());
				if (pulishIds.isEmpty()) {
					return Result.OK(new Page<>());
				}
				QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				queryAdPulishWrapper.lambda().in(AdPublishDetail::getPublishId,pulishIds);
				List<AdPublishDetail> adPublishDetailList = iAdPublishDetailService.list(queryAdPulishWrapper);
				List<String> publishDetailIdList = adPublishDetailList.stream().map(AdPublishDetail::getId).collect(Collectors.toList());
				if (publishDetailIdList.isEmpty()) {
					return Result.OK(new Page<>());
				}
				queryWrapper.lambda().in(AdReport::getPublishDetailId,publishDetailIdList);
			}
		}
		Page<AdReport> page = new Page<AdReport>(pageNo, pageSize);
		IPage<AdReport> pageList = adReportService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adReport
	 * @return
	 */
	@AutoLog(value = "广告上报记录表-添加")
	@Operation(summary="广告上报记录表-添加")
	@RequiresPermissions("ad:ad_report:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdReport adReport) {
		adReportService.save(adReport);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adReport
	 * @return
	 */
	@AutoLog(value = "广告上报记录表-编辑")
	@Operation(summary="广告上报记录表-编辑")
	@RequiresPermissions("ad:ad_report:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdReport adReport) {
		try {
			// 检查状态是否为3，如果是，需要创建或更新待上报数据
			if (adReport != null && adReport.getStatus() != null && adReport.getStatus() == 3) {
				log.info("检测到状态为3的上报记录，将进行待上报数据处理");
				Integer currentMaintenanceCount = adReport.getMaintenanceCount();
				if (currentMaintenanceCount == null) {
					currentMaintenanceCount = 0;
				}
				adReport.setMaintenanceCount(currentMaintenanceCount+1);
				// 获取必要的参数
				String publishDetailId = adReport.getPublishDetailId();
				String driverId = adReport.getDriverId();
				
				if (publishDetailId == null || driverId == null) {
					log.error("缺少必要参数：publishDetailId或driverId为空");
					return Result.error("缺少必要参数：广告发布明细ID或司机ID");
				}
				
				// 使用单次查询检查是否存在待上报的数据
				QueryWrapper<AdReport> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("publish_detail_id", publishDetailId)
						  .eq("driver_id", driverId)
						  .eq("status", 0);
				
				AdReport pendingReport = adReportService.getOne(queryWrapper);
				
				// 获取当前report的report_time用于计算新的上报时间
				Date currentReportTime = adReport.getReportTime();
				if (currentReportTime == null) {
					currentReportTime = new Date(); // 如果为空，默认使用当前时间
				}
				
				// 计算新的上报时间：当前report_time往后推一个月
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentReportTime);
				calendar.add(Calendar.MONTH, 1);
				Date newReportTime = calendar.getTime();
				
				if (pendingReport != null) {
					log.info("找到待上报数据，将更新现有记录：{}", pendingReport.getId());
					
					
					// 更新记录
					boolean updateResult = adReportService.updateById(pendingReport);
					if (!updateResult) {
						log.error("更新待上报数据失败");
						return Result.error("更新待上报数据失败");
					}
					
					log.info("成功更新待上报数据, ID: {}, 新上报时间: {}", pendingReport.getId(), newReportTime);
				} else {
					log.info("未找到待上报数据，将创建新记录");
					
					// 创建新的AdReport对象
					AdReport newReport = new AdReport();
					newReport.setPublishDetailId(publishDetailId);
					newReport.setDriverId(driverId);
					newReport.setReportTime(newReportTime);
					newReport.setReportType(1); // 设置报告类型为1
					newReport.setStatus(0);     // 设置状态为0（待上报）
					// 更新维护次数
					newReport.setMaintenanceCount(currentMaintenanceCount + 1);
					// 保存新记录
					boolean saveResult = adReportService.save(newReport);
					if (!saveResult) {
						log.error("创建新的待上报数据失败");
						return Result.error("创建新的待上报数据失败");
					}
					
					log.info("成功创建新的待上报数据, ID: {}, 上报时间: {}", newReport.getId(), newReportTime);
				}
			}
			
			// 最后更新原始的报告记录
			boolean result = adReportService.updateById(adReport);
			if (!result) {
				return Result.error("编辑失败!");
			}
			
			return Result.OK("编辑成功!");
		} catch (Exception e) {
			log.error("处理广告上报记录编辑时发生异常", e);
			return Result.error("编辑失败: " + e.getMessage());
		}
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告上报记录表-通过id删除")
	@Operation(summary="广告上报记录表-通过id删除")
	@RequiresPermissions("ad:ad_report:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adReportService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告上报记录表-批量删除")
	@Operation(summary="广告上报记录表-批量删除")
	@RequiresPermissions("ad:ad_report:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告上报记录表-通过id查询")
	@Operation(summary="广告上报记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdReport> queryById(@RequestParam(name="id",required=true) String id) {
		AdReport adReport = adReportService.getById(id);
		if(adReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adReport);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adReport
    */
    @RequiresPermissions("ad:ad_report:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdReport adReport) {
        return super.exportXls(request, adReport, AdReport.class, "广告上报记录表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_report:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdReport.class);
    }

	/**
	 * 小程序查询任务接口
	 *
	 * @param status 状态
	 * @return
	 */
	@Operation(summary="小程序-根据状态查询上报任务")
	@GetMapping(value = "/queryTasksByStatus")
	public Result<List<AdReportTaskVO>> queryTasksByStatus(@RequestParam(name="status", required=false) Integer status) {
		// 调用服务方法查询数据
		List<AdReportTaskVO> taskList = adReportService.queryReportTasksByStatus(status);
		return Result.OK(taskList);
	}

	/**
	 * 小程序上报接口
	 *
	 * @param id 广告上报记录ID
	 * @param images 上报图片（多张）
	 * @return
	 */
	@Operation(summary="小程序-上报图片")
	@PostMapping(value = "/uploadReportImages")
	public Result<String> uploadReportImages(@RequestParam(name="id", required=true) String id, 
	                                         @RequestParam(name="images", required=true) String images) {
		// 查询记录是否存在
		AdReport adReport = adReportService.getById(id);
		if(adReport == null) {
			return Result.error("未找到对应的上报记录!");
		}
		
		// 更新images字段
		adReport.setImages(images);
		adReport.setStatus(1);
		// 保存更新
		boolean success = adReportService.updateById(adReport);
		if(success) {
			return Result.OK("上报图片成功!");
		} else {
			return Result.error("上报图片失败!");
		}
	}


	 /**
	  * 小程序根据状态获取车辆信息接口
	  *
	  * @param status 车辆状态（非必传）
	  * @param due 是否到期（非必传）
	  * @return 车辆信息列表
	  */
	 @Operation(summary="小程序-根据状态获取车辆信息")
	 @GetMapping(value = "/queryVehiclesByStatus")
	 public Result<List<AdReportTaskVO>> queryVehiclesByStatus(
			 @RequestParam(name="status", required=false) Integer status,
			 @RequestParam(name="maintenanceStatus", required=false) Integer maintenanceStatus,
			 @RequestParam(name="due", required=false) Integer due) {

		 // 调用Service方法获取符合条件的车辆信息
		 List<AdReportTaskVO> adReportTaskVOList = adReportService.queryVehiclesByStatusAndDue(status,maintenanceStatus, due);

		 return Result.OK(adReportTaskVOList);
	 }
	 /**
	  * 小程序根据状态获取车辆信息接口
	  *
	  * @param status 车辆状态（非必传）
	  * @param due 是否到期（非必传）
	  * @return 车辆信息列表
	  */
	 @Operation(summary="小程序-根据状态获取车辆信息")
	 @GetMapping(value = "/queryVehiclesByStatus2")
	 public Result<List<AdReportTaskVO>> queryVehiclesByStatus2(
			 @RequestParam(name="status", required=false) Integer status,
			 @RequestParam(name="maintenanceStatus", required=false) Integer maintenanceStatus,
			 @RequestParam(name="due", required=false) Integer due) {

		 // 调用Service方法获取符合条件的车辆信息
		 List<AdReportTaskVO> adReportTaskVOList = adReportService.queryVehiclesByStatusAndDue2(status,maintenanceStatus, due);

		 return Result.OK(adReportTaskVOList);
	 }

	/**
	 * 小程序领取维修金接口
	 *
	 * @param id 广告上报记录ID
	 * @return
	 */
	@Operation(summary="小程序-领取维修金")
	@PostMapping(value = "/receiveMaintenanceFee")
	public Result<String> receiveMaintenanceFee(@RequestParam(name="id", required=true) String id) {
		try {
			// 调用服务方法处理领取维修金的业务逻辑
			boolean success = adReportService.receiveMaintenanceFee(id);
			
			if (success) {
				return Result.OK("维修金领取成功！");
			} else {
				return Result.error("维修金领取失败！");
			}
		} catch (Exception e) {
			log.error("领取维修金失败", e);
			return Result.error("领取维修金失败：" + e.getMessage());
		}
	}

}
