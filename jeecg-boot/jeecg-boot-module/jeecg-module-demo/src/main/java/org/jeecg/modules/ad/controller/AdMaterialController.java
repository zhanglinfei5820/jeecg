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
import org.jeecg.modules.ad.entity.AdMaterial;
import org.jeecg.modules.ad.service.IAdMaterialService;

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
 * @Description: 广告物料表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告物料表")
@RestController
@RequestMapping("/ad/adMaterial")
@Slf4j
public class AdMaterialController extends JeecgController<AdMaterial, IAdMaterialService> {
	@Autowired
	private IAdMaterialService adMaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告物料表-分页列表查询")
	@Operation(summary="广告物料表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdMaterial>> queryPageList(AdMaterial adMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdMaterial> queryWrapper = QueryGenerator.initQueryWrapper(adMaterial, req.getParameterMap());
		Page<AdMaterial> page = new Page<AdMaterial>(pageNo, pageSize);
		IPage<AdMaterial> pageList = adMaterialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adMaterial
	 * @return
	 */
	@AutoLog(value = "广告物料表-添加")
	@Operation(summary="广告物料表-添加")
	@RequiresPermissions("ad:ad_material:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdMaterial adMaterial) {
		adMaterialService.save(adMaterial);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adMaterial
	 * @return
	 */
	@AutoLog(value = "广告物料表-编辑")
	@Operation(summary="广告物料表-编辑")
	@RequiresPermissions("ad:ad_material:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdMaterial adMaterial) {
		adMaterialService.updateById(adMaterial);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告物料表-通过id删除")
	@Operation(summary="广告物料表-通过id删除")
	@RequiresPermissions("ad:ad_material:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adMaterialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告物料表-批量删除")
	@Operation(summary="广告物料表-批量删除")
	@RequiresPermissions("ad:ad_material:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adMaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告物料表-通过id查询")
	@Operation(summary="广告物料表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdMaterial> queryById(@RequestParam(name="id",required=true) String id) {
		AdMaterial adMaterial = adMaterialService.getById(id);
		if(adMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adMaterial
    */
    @RequiresPermissions("ad:ad_material:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdMaterial adMaterial) {
        return super.exportXls(request, adMaterial, AdMaterial.class, "广告物料表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_material:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdMaterial.class);
    }

}
