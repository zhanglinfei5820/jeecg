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
import org.jeecg.modules.ad.entity.AdSettlement;
import org.jeecg.modules.ad.service.IAdSettlementService;

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
 * @Description: 结算表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="结算表")
@RestController
@RequestMapping("/ad/adSettlement")
@Slf4j
public class AdSettlementController extends JeecgController<AdSettlement, IAdSettlementService> {
	@Autowired
	private IAdSettlementService adSettlementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adSettlement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "结算表-分页列表查询")
	@Operation(summary="结算表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdSettlement>> queryPageList(AdSettlement adSettlement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdSettlement> queryWrapper = QueryGenerator.initQueryWrapper(adSettlement, req.getParameterMap());
		Page<AdSettlement> page = new Page<AdSettlement>(pageNo, pageSize);
		IPage<AdSettlement> pageList = adSettlementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adSettlement
	 * @return
	 */
	@AutoLog(value = "结算表-添加")
	@Operation(summary="结算表-添加")
	@RequiresPermissions("ad:ad_settlement:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdSettlement adSettlement) {
		adSettlementService.save(adSettlement);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adSettlement
	 * @return
	 */
	@AutoLog(value = "结算表-编辑")
	@Operation(summary="结算表-编辑")
	@RequiresPermissions("ad:ad_settlement:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdSettlement adSettlement) {
		adSettlementService.updateById(adSettlement);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "结算表-通过id删除")
	@Operation(summary="结算表-通过id删除")
	@RequiresPermissions("ad:ad_settlement:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adSettlementService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "结算表-批量删除")
	@Operation(summary="结算表-批量删除")
	@RequiresPermissions("ad:ad_settlement:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adSettlementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "结算表-通过id查询")
	@Operation(summary="结算表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdSettlement> queryById(@RequestParam(name="id",required=true) String id) {
		AdSettlement adSettlement = adSettlementService.getById(id);
		if(adSettlement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adSettlement);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adSettlement
    */
    @RequiresPermissions("ad:ad_settlement:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdSettlement adSettlement) {
        return super.exportXls(request, adSettlement, AdSettlement.class, "结算表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_settlement:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdSettlement.class);
    }

}
