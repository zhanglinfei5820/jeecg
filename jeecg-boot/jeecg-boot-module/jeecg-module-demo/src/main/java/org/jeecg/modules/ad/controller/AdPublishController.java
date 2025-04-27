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
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.service.IAdPublishService;

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
        String title = req.getParameter("title");
        if (oConvertUtils.isNotEmpty(title)) {
            queryWrapper.like("title", title);
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
		return Result.OK(pageList);
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

}
