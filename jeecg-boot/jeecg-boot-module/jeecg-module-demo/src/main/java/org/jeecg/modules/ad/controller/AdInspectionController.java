package org.jeecg.modules.ad.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ad.entity.*;
import org.jeecg.modules.ad.entity.Vo.AdInspectionVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionVO;
import org.jeecg.modules.ad.entity.Vo.DriverInspectionAddVO;
import org.jeecg.modules.ad.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;

/**
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告年检表")
@RestController
@RequestMapping("/ad/adInspection")
@Slf4j
public class AdInspectionController extends JeecgController<AdInspection, IAdInspectionService> {
	@Autowired
	private IAdInspectionService adInspectionService;
	
	@Autowired
	private IAdVehicleService adVehicleService;
	
	@Autowired
	private IAdDriverService adDriverService;
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
	 * @param adInspection
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告年检表-分页列表查询")
	@Operation(summary="广告年检表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdInspection>> queryPageList(AdInspection adInspection,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		SysUser loginUser = commonLoginUserService.getLoginUserInfo(req);
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
		String relatedId = loginUser.getRelatedId();
		QueryWrapper<AdInspection> queryWrapper = QueryGenerator.initQueryWrapper(adInspection, req.getParameterMap());
		if (!CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
			if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				queryAdPulishWrapper.eq("company_id", relatedId);
				queryAdPulishWrapper.eq("type", CommonConstant.INTEGER_VALUE_0);
				List<AdPublishDetail> adPublishDetailList = iAdPublishDetailService.list(queryAdPulishWrapper);
				List<String> publishIdList = adPublishDetailList.stream().map(AdPublishDetail::getPublishId).collect(Collectors.toList());
				if (publishIdList.isEmpty()) {
					return Result.OK(new Page<>());
				}
				queryWrapper.lambda().in(AdInspection::getAdId,publishIdList);
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
				queryWrapper.lambda().in(AdInspection::getAdId,pulishIds);
			}
		}
		Page<AdInspection> page = new Page<AdInspection>(pageNo, pageSize);
		IPage<AdInspection> pageList = adInspectionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 微信分页列表查询
	 *
	 * @param adInspection
	 * @param pageNo
	 * @param pageSize
	 * @param adName
	 * @return
	 */
	@Operation(summary="广告年检表-微信分页列表查询")
	@GetMapping(value = "/vxList")
	public Result<IPage<AdInspectionVO>> queryPageVxList(AdInspection adInspection,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  @RequestParam(name="adName", required = false) String adName,
													  @RequestParam(name="vehicleType", required = false) Integer vehicleType,
													  @RequestParam(name="column", required = false) String column,
													  @RequestParam(name="order", required = false) String order) {
		IPage<AdInspectionVO> pageList = adInspectionService.queryPageVxList(adInspection, pageNo, pageSize, adName, vehicleType, column, order);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adInspection
	 * @return
	 */
	@AutoLog(value = "广告年检表-添加")
	@Operation(summary="广告年检表-添加")
	@RequiresPermissions("ad:ad_inspection:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdInspection adInspection) {
		// 检查是否提供了vehicleId
		if (adInspection.getVehicleId() != null && !adInspection.getVehicleId().isEmpty()) {
			// 通过vehicleId查询车辆信息
			AdVehicle vehicle = adVehicleService.getById(adInspection.getVehicleId());
			if (vehicle != null && vehicle.getDriverId() != null) {
				// 设置司机ID
				adInspection.setDriveId(vehicle.getDriverId());
				log.info("根据vehicleId[{}]查询到司机ID[{}]并赋值", adInspection.getVehicleId(), vehicle.getDriverId());
			} else {
				log.warn("未找到vehicleId[{}]对应的车辆信息或司机ID为空", adInspection.getVehicleId());
			}
		}
		
		adInspectionService.save(adInspection);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adInspection
	 * @return
	 */
	@AutoLog(value = "广告年检表-编辑")
	@Operation(summary="广告年检表-编辑")
	@RequiresPermissions("ad:ad_inspection:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdInspection adInspection) {
		adInspectionService.updateById(adInspection);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告年检表-通过id删除")
	@Operation(summary="广告年检表-通过id删除")
	@RequiresPermissions("ad:ad_inspection:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adInspectionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告年检表-批量删除")
	@Operation(summary="广告年检表-批量删除")
	@RequiresPermissions("ad:ad_inspection:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adInspectionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告年检表-通过id查询")
	@Operation(summary="广告年检表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdInspection> queryById(@RequestParam(name="id",required=true) String id) {
		AdInspection adInspection = adInspectionService.getById(id);
		if(adInspection==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adInspection);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adInspection
    */
    @RequiresPermissions("ad:ad_inspection:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdInspection adInspection) {
        return super.exportXls(request, adInspection, AdInspection.class, "广告年检表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_inspection:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdInspection.class);
    }

    /**
     * 小程序-司机查询年检数据
     * 根据司机ID查询年检数据，并关联获取广告名称
     *
     * @param type 年检类型
     * @param status 年检状态
     * @return 年检数据列表
     */
    @Operation(summary="小程序-司机查询年检数据")
    @GetMapping(value = "/driverInspections")
    public Result<List<DriverInspectionVO>> driverInspections(
            @RequestParam(name="type", required=false) String type,
            @RequestParam(name="order", required=false) String order,
            @RequestParam(name="status", required=false) Integer status,HttpServletRequest request) {
        try {
            // 获取当前登录用户
			SysUser loginUser = commonLoginUserService.getLoginUserInfo(request);
            if (loginUser == null) {
                return Result.error("未登录或登录已过期");
            }
            
            // 获取用户手机号
            String phone = loginUser.getPhone();
            if (phone == null || phone.trim().isEmpty()) {
                return Result.error("用户信息不完整，缺少手机号");
            }

            // 根据手机号查询司机信息
            LambdaQueryWrapper<AdDriver> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AdDriver::getPhone, phone);
            AdDriver driver = adDriverService.getOne(queryWrapper);

            if (driver == null) {
                return Result.error("未找到司机信息");
            }
            
            // 查询年检数据
			String driverId = driver.getId();
			List<DriverInspectionVO> inspections = adInspectionService.getDriverInspections(driverId, type, status,order);
            
            return Result.OK(inspections);
        } catch (Exception e) {
            log.error("查询司机年检数据失败", e);
            return Result.error("查询年检数据失败: " + e.getMessage());
        }
    }

    /**
     * 分页列表查询
     *
     * @param adInspection
     * @param pageNo
     * @param pageSize
     * @param adName
     * @param vehicleType
     * @param column
     * @param order
     * @return
     */
    @AutoLog(value = "广告年检表-分页列表查询")
    @Operation(summary = "广告年检表-分页列表查询")
    @GetMapping(value = "/vx/list")
    public Result<IPage<AdInspectionVO>> queryPageToVxList(AdInspection adInspection,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   @RequestParam(name="adName", required=false) String adName,
                                   @RequestParam(name="vehicleType", required=false) Integer vehicleType,
                                   @RequestParam(name="column", required=false) String column,
                                   @RequestParam(name="order", required=false) String order) {
        IPage<AdInspectionVO> pageList = adInspectionService.queryPageVxList(adInspection, pageNo, pageSize, adName, vehicleType, column, order);
        return Result.OK(pageList);
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
    @AutoLog(value = "广告年检表-司机查询年检数据")
    @Operation(summary = "广告年检表-司机查询年检数据")
    @GetMapping(value = "/vx/driver/list")
    public Result<List<DriverInspectionVO>> getDriverInspections(
            @RequestParam(name="driverId",required=true) String driverId,
            @RequestParam(name="type",required=false) String type,
            @RequestParam(name="status",required=false) Integer status,
            @RequestParam(name="order",required=false) String order) {
        List<DriverInspectionVO> list = adInspectionService.getDriverInspections(driverId, type, status,order);
        return Result.OK(list);
    }
    
    /**
     * 小程序司机提交年检信息
     * 通过一次查询获取司机信息，再通过车牌号查询车辆信息，减少数据库访问次数
     *
     * @param inspectionVO 年检信息
     * @return 操作结果
     */
    @AutoLog(value = "广告年检表-小程序司机提交年检信息")
    @Operation(summary = "广告年检表-小程序司机提交年检信息")
    @PostMapping(value = "/vx/driver/add")
    public Result<String> addDriverInspection(@RequestBody DriverInspectionAddVO inspectionVO) {
        String result = adInspectionService.addDriverInspection(inspectionVO);
        if (result.contains("成功")) {
            return Result.OK(result);
        } else {
            return Result.error(result);
        }
    }
}
