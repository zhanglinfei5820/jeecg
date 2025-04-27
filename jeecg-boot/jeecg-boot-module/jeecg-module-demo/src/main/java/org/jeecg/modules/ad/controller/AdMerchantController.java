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
import org.jeecg.modules.ad.entity.AdMerchant;
import org.jeecg.modules.ad.service.IAdMerchantService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.ad.utils.CommonConstant;
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
 * @Description: 商户表（发布方）
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="商户表（发布方）")
@RestController
@RequestMapping("/ad/adMerchant")
@Slf4j
public class AdMerchantController extends JeecgController<AdMerchant, IAdMerchantService> {
	@Autowired
	private IAdMerchantService adMerchantService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adMerchant
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "商户表（发布方）-分页列表查询")
	@Operation(summary="商户表（发布方）-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdMerchant>> queryPageList(AdMerchant adMerchant,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdMerchant> queryWrapper = QueryGenerator.initQueryWrapper(adMerchant, req.getParameterMap());
		Page<AdMerchant> page = new Page<AdMerchant>(pageNo, pageSize);
		IPage<AdMerchant> pageList = adMerchantService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adMerchant
	 * @return
	 */
	@AutoLog(value = "商户表（发布方）-添加")
	@Operation(summary="商户表（发布方）-添加")
	@RequiresPermissions("ad:ad_merchant:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdMerchant adMerchant) {
		adMerchantService.AddMerchantAndUser(adMerchant);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adMerchant
	 * @return
	 */
	@AutoLog(value = "商户表（发布方）-编辑")
	@Operation(summary="商户表（发布方）-编辑")
	@RequiresPermissions("ad:ad_merchant:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdMerchant adMerchant) {
		adMerchantService.updateById(adMerchant);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商户表（发布方）-通过id删除")
	@Operation(summary="商户表（发布方）-通过id删除")
	@RequiresPermissions("ad:ad_merchant:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adMerchantService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商户表（发布方）-批量删除")
	@Operation(summary="商户表（发布方）-批量删除")
	@RequiresPermissions("ad:ad_merchant:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adMerchantService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "商户表（发布方）-通过id查询")
	@Operation(summary="商户表（发布方）-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdMerchant> queryById(@RequestParam(name="id",required=true) String id) {
		AdMerchant adMerchant = adMerchantService.getById(id);
		if(adMerchant==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adMerchant);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adMerchant
    */
    @RequiresPermissions("ad:ad_merchant:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdMerchant adMerchant) {
        return super.exportXls(request, adMerchant, AdMerchant.class, "商户表（发布方）");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_merchant:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdMerchant.class);
    }

}
