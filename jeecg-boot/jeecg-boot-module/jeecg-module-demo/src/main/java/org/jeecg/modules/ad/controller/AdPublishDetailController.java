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
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.service.IAdPublishDetailService;
import org.jeecg.modules.ad.service.IAdPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="广告发布明细表")
@RestController
@RequestMapping("/ad/adPublishDetail")
@Slf4j
public class AdPublishDetailController extends JeecgController<AdPublishDetail, IAdPublishDetailService> {
	@Autowired
	private IAdPublishDetailService adPublishDetailService;
	@Autowired
	private IAdPublishService adPublishService;

	/**
	 * 分页列表查询
	 *
	 * @param adPublishDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告发布明细表-分页列表查询")
	@Operation(summary="广告发布明细表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdPublishDetail>> queryPageList(AdPublishDetail adPublishDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdPublishDetail> queryWrapper = QueryGenerator.initQueryWrapper(adPublishDetail, req.getParameterMap());
		Page<AdPublishDetail> page = new Page<AdPublishDetail>(pageNo, pageSize);
		IPage<AdPublishDetail> pageList = adPublishDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adPublishDetail
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-添加")
	@Operation(summary="广告发布明细表-添加")
	@RequiresPermissions("ad:ad_publish_detail:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdPublishDetail adPublishDetail) {
		adPublishDetailService.save(adPublishDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adPublishDetail
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-编辑")
	@Operation(summary="广告发布明细表-编辑")
	@RequiresPermissions("ad:ad_publish_detail:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdPublishDetail adPublishDetail) {
		adPublishDetailService.updateById(adPublishDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-通过id删除")
	@Operation(summary="广告发布明细表-通过id删除")
	@RequiresPermissions("ad:ad_publish_detail:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adPublishDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告发布明细表-批量删除")
	@Operation(summary="广告发布明细表-批量删除")
	@RequiresPermissions("ad:ad_publish_detail:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adPublishDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告发布明细表-通过id查询")
	@Operation(summary="广告发布明细表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdPublishDetail> queryById(@RequestParam(name="id",required=true) String id) {
		AdPublishDetail adPublishDetail = adPublishDetailService.getById(id);
		if(adPublishDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adPublishDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adPublishDetail
    */
    @RequiresPermissions("ad:ad_publish_detail:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdPublishDetail adPublishDetail) {
        return super.exportXls(request, adPublishDetail, AdPublishDetail.class, "广告发布明细表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_publish_detail:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdPublishDetail.class);
    }


	 /**
	  *  分发
	  *
	  * @param adPublishDetail
	  * @return
	  */
	 @AutoLog(value = "广告发布明细表-分发")
	 @Operation(summary="广告发布明细表-分发")
	 @RequiresPermissions("ad:ad_publish_detail:distribute")
	 @RequestMapping(value = "/distribute", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> distribute(@RequestBody AdPublishDetail adPublishDetail) {
		 try {
			 String result = adPublishDetailService.distribute(adPublishDetail);
			 return Result.OK(result);
		 } catch (Exception e) {
			 log.error("分发广告失败", e);
			 return Result.error("分发失败：" + e.getMessage());
		 }
	 }

}
