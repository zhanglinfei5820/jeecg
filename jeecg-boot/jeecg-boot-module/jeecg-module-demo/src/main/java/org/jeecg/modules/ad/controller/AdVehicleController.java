package org.jeecg.modules.ad.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.entity.Vo.AdVehicleVO;
import org.jeecg.modules.ad.service.*;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="车辆表")
@RestController
@RequestMapping("/ad/adVehicle")
@Slf4j
public class AdVehicleController extends JeecgController<AdVehicle, IAdVehicleService> {
	@Autowired
	private IAdVehicleService adVehicleService;
	
	@Autowired
	private IAdPublishDetailService adPublishDetailService;
	
	@Autowired
	private IAdReportService adReportService;
	
	@Autowired
	private IAdPublishService adPublishService;
	 @Resource
	 private ICommonLoginUserService commonLoginUserService;
	 @Resource
	 private IAdDriverService adDriverService ;

	/**
	 * 分页列表查询
	 *
	 * @param adVehicle 查询条件
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param keyword 关键字
	 * @param driverName 司机名称
	 * @param adName 广告名称
	 * @param companyName 公司名称
	 * @param req 请求
	 * @return 分页结果
	 */
	//@AutoLog(value = "车辆表-分页列表查询")
	@Operation(summary="车辆表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdVehicleVO>> queryPageList(AdVehicle adVehicle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   @RequestParam(name="keyword", required = false) String keyword,
								   @RequestParam(name="driverName", required = false) String driverName,
								   @RequestParam(name="adName", required = false) String adName,
								   @RequestParam(name="companyName", required = false) String companyName,
								   HttpServletRequest req) {
        
        // 构建查询参数Map
        Map<String, Object> queryParams = new HashMap<>();
		SysUser loginUserInfo = commonLoginUserService.getLoginUserInfo(req);
		if (loginUserInfo == null) {
			return Result.error("用户未登录");
		}
		if (!CommonConstant.ADMIN.equals(loginUserInfo.getUsername())){
			List<String> roleCodeList = commonLoginUserService.getRoleCode(loginUserInfo);
			if (CollectionUtils.isEmpty(roleCodeList)){
				return Result.error("用户未绑定角色");
			}
			if (roleCodeList.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				// 网约车公司
				queryParams.put("companyId", loginUserInfo.getRelatedId());
			}
		}
        // 添加基本查询参数
        if (adVehicle != null) {
            if (adVehicle.getPlateNumber() != null) {
                queryParams.put("plateNumber", adVehicle.getPlateNumber());
            }
            if (adVehicle.getVehicleType() != null) {
                queryParams.put("vehicleType", adVehicle.getVehicleType());
            }
            if (adVehicle.getBrand() != null) {
                queryParams.put("brand", adVehicle.getBrand());
            }
            if (adVehicle.getStatus() != null) {
                queryParams.put("status", adVehicle.getStatus());
            }
            if (adVehicle.getSysOrgCode() != null) {
                queryParams.put("sysOrgCode", adVehicle.getSysOrgCode());
            }
        }
        
        // 添加特定查询参数
        queryParams.put("keyword", keyword);
        queryParams.put("driverName", driverName);
        queryParams.put("adName", adName);
        queryParams.put("companyName", companyName);
        
        // 调用一次性查询方法
        Page<AdVehicleVO> page = new Page<>(pageNo, pageSize);
        IPage<AdVehicleVO> pageList = adVehicleService.queryVehiclePageList(page, queryParams);
        
        return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adVehicle
	 * @return
	 */
	@AutoLog(value = "车辆表-添加")
	@Operation(summary="车辆表-添加")
	@RequiresPermissions("ad:ad_vehicle:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdVehicle adVehicle) {
		LambdaQueryWrapper<AdVehicle> queryWrapper = new QueryWrapper<AdVehicle>().lambda()
				.eq(AdVehicle::getPlateNumber, adVehicle.getPlateNumber());
		if (adVehicleService.count(queryWrapper) > 0) {
			return Result.error("车牌号码不能重复!");
		}
		adVehicleService.save(adVehicle);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adVehicle
	 * @return
	 */
	@AutoLog(value = "车辆表-编辑")
	@Operation(summary="车辆表-编辑")
	@RequiresPermissions("ad:ad_vehicle:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdVehicle adVehicle) {
		// 更新车辆信息
		adVehicleService.editAdVehicle(adVehicle);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆表-通过id删除")
	@Operation(summary="车辆表-通过id删除")
	@RequiresPermissions("ad:ad_vehicle:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adVehicleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "车辆表-批量删除")
	@Operation(summary="车辆表-批量删除")
	@RequiresPermissions("ad:ad_vehicle:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adVehicleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "车辆表-通过id查询")
	@Operation(summary="车辆表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdVehicle> queryById(@RequestParam(name="id",required=true) String id) {
		AdVehicle adVehicle = adVehicleService.getById(id);
		if(adVehicle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adVehicle);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adVehicle
    */
    @RequiresPermissions("ad:ad_vehicle:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdVehicle adVehicle) {
        return super.exportXls(request, adVehicle, AdVehicle.class, "车辆表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_vehicle:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdVehicle.class);
    }

	/**
	 * 车辆统计接口
	 * 返回车辆类型的数量、车辆总数等统计信息
	 * 查询条件status不等于2
	 * 显示所有数据字典中的车辆类型，如果某种类型没有车辆，则计数为0
	 * @return 包含车辆总数和各类型数量的统计数据
	 */
	@Operation(summary="车辆统计")
	@GetMapping("/statistics")
	public Result<Map<String, Object>> getVehicleStatistics(HttpServletRequest request) {
		SysUser loginUser = commonLoginUserService.getLoginUserInfo(request);
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
		Map<String, Object> statistics = adVehicleService.getVehicleStatistics(loginUser);
		return Result.OK(statistics);
	}


	/**
	 * 车辆管理查询
	 * 返回车辆类型、车牌号、车辆品牌、所属公司、广告名称、安装时间、维护金次数、车辆图片
	 * @param adVehicleVO
	 * @return
	 */
	@Operation(summary="车辆管理")
	@GetMapping(value = "/queryVxList")
	public Result<List<AdVehicleVO>> queryVxList(AdVehicleVO adVehicleVO) {
		List<AdVehicleVO> list = adVehicleService.queryVxList(adVehicleVO);
		return Result.OK(list);
	}

	/**
	 * 小程序年检维护分页查询接口
	 * 返回车辆信息（车牌号、状态、车辆类型、车辆品牌）、所属公司、广告名称、到期时间、车辆图片等信息
	 * 多表联查，基于ad_inspection的type条件和其他业务条件进行查询
	 * 
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param inspectionType 年检类型
	 * @param plateNumber 车牌号
	 * @param vehicleType 车辆类型
	 * @param companyName 公司名称
	 * @param adName 广告名称
	 * @param keyword 关键字搜索
	 * @return 包含车辆和年检信息的分页结果
	 */
	@Operation(summary="小程序年检维护分页查询")
	@GetMapping(value = "/queryInspectionMaintenanceList")
	public Result<IPage<AdVehicleVO>> queryInspectionMaintenanceList(
		@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
		@RequestParam(name="inspectionType", required = false) String inspectionType,
		@RequestParam(name="plateNumber", required = false) String plateNumber,
		@RequestParam(name="vehicleType", required = false) Integer vehicleType,
		@RequestParam(name="companyName", required = false) String companyName,
		@RequestParam(name="adName", required = false) String adName,
		@RequestParam(name="expireTime", required = false) String expireTime,
		@RequestParam(name="orderBy", required = false) String orderBy,
		@RequestParam(name="keyword", required = false) String keyword) {
		
		// 构建查询参数Map，统一参数处理
		Map<String, Object> queryParams = new HashMap<>();
		
		// 添加基本查询参数
		queryParams.put("inspectionType", inspectionType);  // ad_inspection表的type字段
		queryParams.put("plateNumber", plateNumber);
		queryParams.put("vehicleType", vehicleType);
		queryParams.put("companyName", companyName);
		queryParams.put("adName", adName);
		queryParams.put("keyword", keyword);
		queryParams.put("expireTime", expireTime);
		queryParams.put("orderBy", orderBy);

		// 创建分页对象
		Page<AdVehicleVO> page = new Page<>(pageNo, pageSize);
		
		// 调用服务层方法执行查询
		IPage<AdVehicleVO> pageList = adVehicleService.queryInspectionMaintenancePageList(page, queryParams);
		
		return Result.OK(pageList);
	}

	/**
	 * 统计指定公司启用状态车辆数
	 *
	 * @param publishId 广告idID
	 * @return 启用状态车辆数
	 */
	@AutoLog(value = "车辆表-统计启用车辆数")
	@Operation(summary="车辆表-统计启用车辆数")
	@GetMapping(value = "/countEnabledVehicles")
	public Result<Long> countEnabledVehicles(@RequestParam(name="id",required=true) String publishId) {
		return Result.OK(adVehicleService.countEnabledVehicles(publishId));
	}

	 @AutoLog(value = "车辆表-获取车牌号")
	 @Operation(summary="车辆表-获取车牌号")
	 @GetMapping(value = "/getAdVehicle")
	 public Result<AdVehicle> getAdVehicle(HttpServletRequest request) {
		 SysUser loginUser = commonLoginUserService.getLoginUserInfo(request);
		 if (loginUser == null) {
			 return Result.error("用户未登录");
		 }
		 String relatedId = loginUser.getRelatedId();
		 if (StringUtils.isBlank(relatedId)) {
			 return Result.error("获取用户失败!");
		 }
		 LambdaQueryWrapper<AdVehicle> queryWrapper = new QueryWrapper<AdVehicle>().lambda().eq(AdVehicle::getDriverId, relatedId);
		 AdVehicle vehicle = adVehicleService.getOne(queryWrapper);
		 if (vehicle == null) {
			 return Result.error("获取车辆信息失败!");
		 }
		 return Result.OK(vehicle);
	 }


	 /**
	  * 小程序-安装审核
	  *
	  * @param id 广告上报记录ID
	  * @param images 上报图片（多张）
	  * @return
	  */
	 @Operation(summary="小程序-安装审核")
	 @PostMapping(value = "/uploadInstallationImages")
	 public Result<String> uploadInstallationImages(@RequestParam(name="id", required=true) String id,
											  @RequestParam(name="images", required=true) String images) {
//		 adVehicleService.uploadInstallationImages(id, images);
		 return Result.OK("安装审核成功!");
	 }
}
