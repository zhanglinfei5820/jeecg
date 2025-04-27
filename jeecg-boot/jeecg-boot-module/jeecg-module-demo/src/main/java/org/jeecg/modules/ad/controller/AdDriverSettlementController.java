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
import org.jeecg.modules.ad.entity.AdDriverSettlement;
import org.jeecg.modules.ad.service.IAdDriverSettlementService;

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
 * @Description: 司机结算表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="司机结算表")
@RestController
@RequestMapping("/ad/adDriverSettlement")
@Slf4j
public class AdDriverSettlementController extends JeecgController<AdDriverSettlement, IAdDriverSettlementService> {
	@Autowired
	private IAdDriverSettlementService adDriverSettlementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adDriverSettlement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "司机结算表-分页列表查询")
	@Operation(summary="司机结算表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdDriverSettlement>> queryPageList(AdDriverSettlement adDriverSettlement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdDriverSettlement> queryWrapper = QueryGenerator.initQueryWrapper(adDriverSettlement, req.getParameterMap());
		Page<AdDriverSettlement> page = new Page<AdDriverSettlement>(pageNo, pageSize);
		IPage<AdDriverSettlement> pageList = adDriverSettlementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adDriverSettlement
	 * @return
	 */
	@AutoLog(value = "司机结算表-添加")
	@Operation(summary="司机结算表-添加")
	@RequiresPermissions("ad:ad_driver_settlement:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdDriverSettlement adDriverSettlement) {
		adDriverSettlementService.save(adDriverSettlement);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adDriverSettlement
	 * @return
	 */
	@AutoLog(value = "司机结算表-编辑")
	@Operation(summary="司机结算表-编辑")
	@RequiresPermissions("ad:ad_driver_settlement:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdDriverSettlement adDriverSettlement) {
		adDriverSettlementService.updateById(adDriverSettlement);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "司机结算表-通过id删除")
	@Operation(summary="司机结算表-通过id删除")
	@RequiresPermissions("ad:ad_driver_settlement:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adDriverSettlementService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "司机结算表-批量删除")
	@Operation(summary="司机结算表-批量删除")
	@RequiresPermissions("ad:ad_driver_settlement:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adDriverSettlementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "司机结算表-通过id查询")
	@Operation(summary="司机结算表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdDriverSettlement> queryById(@RequestParam(name="id",required=true) String id) {
		AdDriverSettlement adDriverSettlement = adDriverSettlementService.getById(id);
		if(adDriverSettlement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adDriverSettlement);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adDriverSettlement
    */
    @RequiresPermissions("ad:ad_driver_settlement:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdDriverSettlement adDriverSettlement) {
        return super.exportXls(request, adDriverSettlement, AdDriverSettlement.class, "司机结算表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_driver_settlement:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdDriverSettlement.class);
    }

}
