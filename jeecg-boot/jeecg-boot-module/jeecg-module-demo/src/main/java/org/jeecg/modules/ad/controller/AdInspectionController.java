package org.jeecg.modules.ad.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.service.IAdInspectionService;
import org.jeecg.modules.ad.service.IAdVehicleService;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

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
        QueryWrapper<AdInspection> queryWrapper = QueryGenerator.initQueryWrapper(adInspection, req.getParameterMap());
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
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "广告年检表-微信分页列表查询")
	 @Operation(summary="广告年检表-微信分页列表查询")
	 @GetMapping(value = "/vxList")
	 public Result<IPage<?>> queryPageVxList(AdInspection adInspection,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		 QueryWrapper<AdInspection> queryWrapper = QueryGenerator.initQueryWrapper(adInspection, req.getParameterMap());
		 Page<AdInspection> page = new Page<AdInspection>(pageNo, pageSize);
		 IPage<AdInspection> pageList = adInspectionService.page(page, queryWrapper);
		 // 获取所有车辆ID
		List<String> vehicleIds = pageList.getRecords().stream()
			.map(AdInspection::getVehicleId)
			.distinct()
			.collect(Collectors.toList());
			
		// 查询车辆信息
		List<AdVehicle> vehicleList = new ArrayList<>();
		if (!vehicleIds.isEmpty()) {
			vehicleList = adVehicleService.listByIds(vehicleIds);
		}
		
		//配置AdVehicle类型的IPage
		Page<AdVehicle> voPage = new Page<>();
		//pageList属性值赋值过来
		BeanUtils.copyProperties(pageList, voPage, "records");
		
		//将vehicleList放进去
		voPage.setRecords(vehicleList);
		
		return Result.OK(voPage);
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
	 * 获取年检数据集合，包含车辆信息
	 *
	 * @param adInspection
	 * @return
	 */
	@AutoLog(value = "广告年检表-获取年检数据集合")
	@Operation(summary="广告年检表-获取年检数据集合")
	@GetMapping(value = "/listWithVehicle")
	public Result<List<AdVehicle>> listWithVehicle(AdInspection adInspection) {
		QueryWrapper<AdInspection> queryWrapper = QueryGenerator.initQueryWrapper(adInspection, null);
		List<AdInspection> inspectionList = adInspectionService.list(queryWrapper);
		
		// 获取所有车辆ID
		List<String> vehicleIds = inspectionList.stream()
			.map(AdInspection::getVehicleId)
			.distinct()
			.collect(Collectors.toList());
			
		// 查询车辆信息
		List<AdVehicle> vehicleList = new ArrayList<>();
		if (!vehicleIds.isEmpty()) {
			vehicleList = adVehicleService.listByIds(vehicleIds);
		}
		
		return Result.OK(vehicleList);
	}

}
