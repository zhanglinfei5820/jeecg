package org.jeecg.modules.ad.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ad.entity.AdVehicle;
import org.jeecg.modules.ad.service.IAdVehicleService;

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
	
	/**
	 * 分页列表查询
	 *
	 * @param adVehicle
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "车辆表-分页列表查询")
	@Operation(summary="车辆表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdVehicle>> queryPageList(AdVehicle adVehicle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   @RequestParam(name="keyword", required = false) String keyword,
								   HttpServletRequest req) {
        QueryWrapper<AdVehicle> queryWrapper = QueryGenerator.initQueryWrapper(adVehicle, req.getParameterMap());
        
        // 添加关键词搜索条件
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("plate_number", keyword)
                .or()
                .like("driver_name", keyword)
                .or()
                .like("ad_name", keyword)
            );
        }
        
		Page<AdVehicle> page = new Page<AdVehicle>(pageNo, pageSize);
		IPage<AdVehicle> pageList = adVehicleService.page(page, queryWrapper);
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
		adVehicleService.updateById(adVehicle);
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

	 @GetMapping("/statistics")
	 public Result<Map<String, Object>> getVehicleStatistics() {
		 Map<String, Object> statistics = adVehicleService.getVehicleStatistics();
		 return Result.OK(statistics);
	 }


	 /**
	  * 车辆管理查询
	  *
	  * @param adVehicle
	  * @return
	  */
	 @Operation(summary="车辆管理")
	 @GetMapping(value = "/queryVxList")
	 public Result<?> queryVxList(AdVehicle adVehicle) {
		 return Result.OK(adVehicleService.queryVxList(adVehicle));
	 }

}
