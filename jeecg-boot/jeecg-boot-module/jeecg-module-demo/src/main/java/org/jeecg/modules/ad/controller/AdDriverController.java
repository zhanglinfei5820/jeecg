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
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 司机表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="司机表")
@RestController
@RequestMapping("/ad/adDriver")
@Slf4j
public class AdDriverController extends JeecgController<AdDriver, IAdDriverService> {
	@Autowired
	private IAdDriverService adDriverService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adDriver
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "司机表-分页列表查询")
	@Operation(summary="司机表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdDriver>> queryPageList(AdDriver adDriver,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdDriver> queryWrapper = QueryGenerator.initQueryWrapper(adDriver, req.getParameterMap());
		Page<AdDriver> page = new Page<AdDriver>(pageNo, pageSize);
		IPage<AdDriver> pageList = adDriverService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adDriver
	 * @return
	 */
	@AutoLog(value = "司机表-添加")
	@Operation(summary="司机表-添加")
	@RequiresPermissions("ad:ad_driver:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdDriver adDriver) {
		adDriverService.AddDriverAndUser(adDriver);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adDriver
	 * @return
	 */
	@AutoLog(value = "司机表-编辑")
	@Operation(summary="司机表-编辑")
	@RequiresPermissions("ad:ad_driver:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdDriver adDriver) {
		adDriverService.updateById(adDriver);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "司机表-通过id删除")
	@Operation(summary="司机表-通过id删除")
	@RequiresPermissions("ad:ad_driver:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adDriverService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "司机表-批量删除")
	@Operation(summary="司机表-批量删除")
	@RequiresPermissions("ad:ad_driver:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adDriverService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "司机表-通过id查询")
	@Operation(summary="司机表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdDriver> queryById(@RequestParam(name="id",required=true) String id) {
		AdDriver adDriver = adDriverService.getById(id);
		if(adDriver==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adDriver);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adDriver
    */
    @RequiresPermissions("ad:ad_driver:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdDriver adDriver) {
        return super.exportXls(request, adDriver, AdDriver.class, "司机表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_driver:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdDriver.class);
    }

}
