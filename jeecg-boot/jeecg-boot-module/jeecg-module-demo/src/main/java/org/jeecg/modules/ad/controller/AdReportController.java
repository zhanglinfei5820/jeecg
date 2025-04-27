package org.jeecg.modules.ad.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.service.IAdReportService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

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
        QueryWrapper<AdReport> queryWrapper = QueryGenerator.initQueryWrapper(adReport, req.getParameterMap());
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
		adReportService.updateById(adReport);
		return Result.OK("编辑成功!");
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

}
