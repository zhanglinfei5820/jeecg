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
import org.jeecg.modules.ad.entity.AdCompany;
import org.jeecg.modules.ad.service.IAdCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 广告公司
 * @Author: jeecg-boot
 * @Date:   2025-04-26
 * @Version: V1.0
 */
@Tag(name="广告公司")
@RestController
@RequestMapping("/ad/adCompany")
@Slf4j
public class AdCompanyController extends JeecgController<AdCompany, IAdCompanyService> {
	@Autowired
	private IAdCompanyService adCompanyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adCompany
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "广告公司-分页列表查询")
	@Operation(summary="广告公司-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdCompany>> queryPageList(AdCompany adCompany,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdCompany> queryWrapper = QueryGenerator.initQueryWrapper(adCompany, req.getParameterMap());
		Page<AdCompany> page = new Page<AdCompany>(pageNo, pageSize);
		IPage<AdCompany> pageList = adCompanyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adCompany
	 * @return
	 */
	@AutoLog(value = "广告公司-添加")
	@Operation(summary="广告公司-添加")
	@RequiresPermissions("ad:ad_company:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdCompany adCompany) {
		adCompanyService.save(adCompany);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adCompany
	 * @return
	 */
	@AutoLog(value = "广告公司-编辑")
	@Operation(summary="广告公司-编辑")
	@RequiresPermissions("ad:ad_company:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdCompany adCompany) {
		adCompanyService.updateById(adCompany);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告公司-通过id删除")
	@Operation(summary="广告公司-通过id删除")
	@RequiresPermissions("ad:ad_company:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adCompanyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告公司-批量删除")
	@Operation(summary="广告公司-批量删除")
	@RequiresPermissions("ad:ad_company:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adCompanyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "广告公司-通过id查询")
	@Operation(summary="广告公司-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdCompany> queryById(@RequestParam(name="id",required=true) String id) {
		AdCompany adCompany = adCompanyService.getById(id);
		if(adCompany==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adCompany);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adCompany
    */
    @RequiresPermissions("ad:ad_company:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdCompany adCompany) {
        return super.exportXls(request, adCompany, AdCompany.class, "广告公司");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_company:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdCompany.class);
    }

}
