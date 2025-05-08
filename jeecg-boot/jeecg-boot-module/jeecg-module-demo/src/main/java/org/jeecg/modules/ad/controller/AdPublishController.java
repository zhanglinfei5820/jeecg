package org.jeecg.modules.ad.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.mapper.AdInspectionMapper;
import org.jeecg.modules.ad.mapper.AdPublishDetailMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 广告发布表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告发布表")
@RestController
@RequestMapping("/ad/adPublish")
@Slf4j
public class AdPublishController extends JeecgController<AdPublish, IAdPublishService> {
	@Autowired
	private IAdPublishService adPublishService;
	@Resource
	private AdInspectionMapper adInspectionMapper;
	@Autowired
	private IAdInspectionService iAdInspectionService;
	@Resource
	private AdPublishDetailMapper adPublishDetailMapper;
	@Autowired
	private IAdPublishDetailService iAdPublishDetailService;
	@Resource
	private ICommonLoginUserService commonLoginUserService;
	@Resource
	private IAdReportService iAdReportService;

	/**
	 * 分页列表查询
	 *
	 * @param adPublish
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告发布表-分页列表查询")
	@Operation(summary="广告发布表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdPublish>> queryPageList(AdPublish adPublish,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AdPublish> queryWrapper = QueryGenerator.initQueryWrapper(adPublish, req.getParameterMap());
		SysUser loginUserInfo = commonLoginUserService.getLoginUserInfo(req);
		if (loginUserInfo == null) {
			return Result.error("用户未登录");
		}
		if (!CommonConstant.ADMIN.equals(loginUserInfo.getUsername())){
			List<String> roleCodeList = commonLoginUserService.getRoleCode(loginUserInfo);
			if (CollectionUtils.isEmpty(roleCodeList)){
				return Result.error("用户未绑定角色");
			}
			if (roleCodeList.contains(CommonConstant.ROLE_CODE_ADVERTISERS)) {
				// 广告方
				queryWrapper.eq("merchant_id", loginUserInfo.getRelatedId());
			}
		}

        // 添加商户ID查询条件
        String merchantId = req.getParameter("merchantId");
        if (oConvertUtils.isNotEmpty(merchantId)) {
            queryWrapper.eq("merchant_id", merchantId);
        }
        // 添加物料ID查询条件
        String materialId = req.getParameter("materialId");
        if (oConvertUtils.isNotEmpty(materialId)) {
            queryWrapper.eq("material_id", materialId);
        }
        // 添加广告标题模糊查询
        String name = req.getParameter("name");
        if (oConvertUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 添加开始报名时间范围查询
        String startTime_begin = req.getParameter("startTime_begin");
        String startTime_end = req.getParameter("startTime_end");
        if (oConvertUtils.isNotEmpty(startTime_begin) && oConvertUtils.isNotEmpty(startTime_end)) {
            queryWrapper.between("start_time", startTime_begin, startTime_end);
        }
        // 添加报名结束时间范围查询
        String endTime_begin = req.getParameter("endTime_begin");
        String endTime_end = req.getParameter("endTime_end");
        if (oConvertUtils.isNotEmpty(endTime_begin) && oConvertUtils.isNotEmpty(endTime_end)) {
            queryWrapper.between("end_time", endTime_begin, endTime_end);
        }
        // 添加状态查询
        String status = req.getParameter("status");
        if (oConvertUtils.isNotEmpty(status)) {
            queryWrapper.eq("status", status);
        }
		Page<AdPublish> page = new Page<AdPublish>(pageNo, pageSize);
		IPage<AdPublish> pageList = adPublishService.page(page, queryWrapper);
		
		// 增加返回商户名称、物料名称、广告名称
		List<AdPublish> records = pageList.getRecords();
		if(records != null && !records.isEmpty()) {
			for(AdPublish item : records) {
				// 商户名称和物料名称通过@Dict注解已经自动处理
				// 确保不重复设置名称字段，避免覆盖已有数据
				if(item.getName() == null) {
					item.setName(item.getName());
				}
			}
		}
		
		return Result.OK(pageList);
	}
	
	/**
	 * 广告统计接口
	 *
	 * @return 返回包含广告总数、年检待安装、破损待处理、有效期的统计数据
	 */
	@AutoLog(value = "广告发布表-广告统计")
	@Operation(summary="广告发布表-广告统计")
	@GetMapping(value = "/statistics")
	public Result<Map<String, Long>> getAdStatistics() {
		Map<String, Long> result = new HashMap<>(4);
		long totalCount = 0;
		long validCount = 0;
		long inspectionPendingCount = 0;
		long damagePendingCount = 0;
		
		// 获取当前登录用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
		
		String userId = loginUser.getRelatedId();
		String roleCode = loginUser.getRoleCode();
		
		try {
			// 根据角色区分查询逻辑
			if (CommonConstant.ROLE_CODE_ADVERTISERS.contains(roleCode)) {
				// 1. 商户角色
				// 1.1 广告总数：查询商户发布的广告
				QueryWrapper<AdPublish> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("merchant_id", userId);
				totalCount = adPublishService.count(queryWrapper);
				
				// 1.2 有效期：状态为1或4的广告
//				QueryWrapper<AdPublish> validQueryWrapper = new QueryWrapper<>();
//				validQueryWrapper.eq("merchant_id", userId);
//				validQueryWrapper.in("status", Arrays.asList(1, 4));
//				validCount = adPublishService.count(validQueryWrapper);

				// 获取商户所有广告的ID列表
				List<AdPublish> adPublishList = adPublishService.list(queryWrapper);
				List<String> adIdList = adPublishList.stream().map(AdPublish::getId).collect(Collectors.toList());

				// 1.2 即将过期
				QueryWrapper<AdReport> adReportQueryWrapper = new QueryWrapper<>();
				adReportQueryWrapper.lambda().eq(AdReport::getStatus,0).in(AdReport::getId, adIdList);
				validCount = iAdReportService.count(adReportQueryWrapper);
				
				if (!adIdList.isEmpty()) {
					// 1.3 年检待安装：type为0的数据
					QueryWrapper<AdInspection> inspectionQueryWrapper = new QueryWrapper<>();
					inspectionQueryWrapper.in("ad_id", adIdList);
					inspectionQueryWrapper.eq("type", "0");
//					inspectionPendingCount = iAdInspectionService.count(inspectionQueryWrapper);
					inspectionPendingCount = adInspectionMapper.selectCount(inspectionQueryWrapper);
					
					// 1.4 破损待处理：type为1的数据
					QueryWrapper<AdInspection> damageQueryWrapper = new QueryWrapper<>();
					damageQueryWrapper.in("ad_id", adIdList);
					damageQueryWrapper.eq("type", "1");
//					damagePendingCount = iAdInspectionService.count(damageQueryWrapper);
					damagePendingCount = adInspectionMapper.selectCount(damageQueryWrapper);
				}
			} else if (CommonConstant.ROLE_CODE_ADCOMPANY.contains(roleCode)) {
				// 2. 公司角色
				// 2.1 广告总数：查询公司下的广告
				QueryWrapper<AdPublishDetail> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("company_id", userId);
				queryWrapper.eq("type", 0);
//				totalCount = iAdPublishDetailService.count(queryWrapper);
				totalCount = adPublishDetailMapper.selectCount(queryWrapper);

				// 2.2 有效期：状态为1或4的广告
				QueryWrapper<AdPublishDetail> validQueryWrapper = new QueryWrapper<>();
				validQueryWrapper.eq("company_id", userId);
				validQueryWrapper.eq("type", 0);
				validQueryWrapper.in("status", Arrays.asList(1, 4));
//				validCount = iAdPublishDetailService.count(validQueryWrapper);
				validCount = adPublishDetailMapper.selectCount(validQueryWrapper);

				// 获取公司所有广告明细的ID列表
				List<AdPublishDetail> publishDetailList = adPublishDetailMapper.selectList(queryWrapper);
				List<String> publishIdList = publishDetailList.stream().map(AdPublishDetail::getPublishId).collect(Collectors.toList());
				
				if (!publishIdList.isEmpty()) {
					// 2.3 年检待安装：type为0的数据
					QueryWrapper<AdInspection> inspectionQueryWrapper = new QueryWrapper<>();
					inspectionQueryWrapper.in("ad_id", publishIdList);
					inspectionQueryWrapper.eq("type", "0");
//					inspectionPendingCount = iAdInspectionService.count(inspectionQueryWrapper);
					inspectionPendingCount = adInspectionMapper.selectCount(inspectionQueryWrapper);

					// 2.4 破损待处理：type为1的数据
					QueryWrapper<AdInspection> damageQueryWrapper = new QueryWrapper<>();
					damageQueryWrapper.in("ad_id", publishIdList);
					damageQueryWrapper.eq("type", "1");
//					damagePendingCount = iAdInspectionService.count(damageQueryWrapper);
					damagePendingCount = adInspectionMapper.selectCount(damageQueryWrapper);
				}
			} else {
				// 管理员或其他角色，不做限制，获取全部数据
//				totalCount = adPublishService.count();
//
//				QueryWrapper<AdPublish> validQueryWrapper = new QueryWrapper<>();
//				validQueryWrapper.in("status", Arrays.asList(1, 4));
//				validCount = adPublishService.count(validQueryWrapper);
//
//				QueryWrapper<AdInspection> inspectionQueryWrapper = new QueryWrapper<>();
//				inspectionQueryWrapper.eq("type", "0");
//				inspectionPendingCount = adInspectionMapper.selectCount(inspectionQueryWrapper);
//
//				QueryWrapper<AdInspection> damageQueryWrapper = new QueryWrapper<>();
//				damageQueryWrapper.eq("type", "1");
//				damagePendingCount = adInspectionMapper.selectCount(damageQueryWrapper);
			}
			
			// 封装结果
			result.put("totalCount", totalCount);
			result.put("validCount", validCount);
			result.put("inspectionPendingCount", inspectionPendingCount);
			result.put("damagePendingCount", damagePendingCount);
			
			return Result.OK(result);
		} catch (Exception e) {
			log.error("广告统计获取失败", e);
			return Result.error("广告统计获取失败: " + e.getMessage());
		}
	}
	
	/**
	 *   添加
	 *
	 * @param adPublish
	 * @return
	 */
	@AutoLog(value = "广告发布表-添加")
	@Operation(summary="广告发布表-添加")
	@RequiresPermissions("ad:ad_publish:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdPublish adPublish) {
		adPublishService.save(adPublish);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adPublish
	 * @return
	 */
	@AutoLog(value = "广告发布表-编辑")
	@Operation(summary="广告发布表-编辑")
	@RequiresPermissions("ad:ad_publish:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdPublish adPublish) {
		adPublishService.updateById(adPublish);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告发布表-通过id删除")
	@Operation(summary="广告发布表-通过id删除")
	@RequiresPermissions("ad:ad_publish:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adPublishService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告发布表-批量删除")
	@Operation(summary="广告发布表-批量删除")
	@RequiresPermissions("ad:ad_publish:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adPublishService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告发布表-通过id查询")
	@Operation(summary="广告发布表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdPublish> queryById(@RequestParam(name="id",required=true) String id) {
		AdPublish adPublish = adPublishService.getById(id);
		if(adPublish==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adPublish);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adPublish
    */
    @RequiresPermissions("ad:ad_publish:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdPublish adPublish) {
        return super.exportXls(request, adPublish, AdPublish.class, "广告发布表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_publish:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdPublish.class);
    }

	/**
	 * 小程序司机查询广告接口
	 * 
	 * @return 广告信息列表
	 */
	@AutoLog(value = "广告发布表-司机查询广告")
	@Operation(summary="广告发布表-司机查询广告")
	@GetMapping(value = "/driverAds")
	public Result<List<AdPublish>> driverAds(HttpServletRequest request) {
		try {
			// 获取当前登录用户
			SysUser loginUser = commonLoginUserService.getLoginUserInfo(request);
			if (loginUser == null) {
				return Result.error("用户未登录");
			}

			// 从登录用户获取手机号
			String phone = loginUser.getPhone();
			if (phone == null || phone.trim().isEmpty()) {
				return Result.error("手机号不存在");
			}
			// 查询广告信息
			List<AdPublish> adList = adPublishService.getDriverAds(phone);
			
			return Result.OK(adList);
		} catch (Exception e) {
			log.error("司机查询广告失败", e);
			return Result.error("查询失败: " + e.getMessage());
		}
	}
}
