package org.jeecg.modules.ad.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdTransaction;
import org.jeecg.modules.ad.entity.AdWithdrawal;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdWithdrawalService;

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

import java.math.BigDecimal;

 /**
 * @Description: 提现申请表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="提现申请表")
@RestController
@RequestMapping("/ad/adWithdrawal")
@Slf4j
public class AdWithdrawalController extends JeecgController<AdWithdrawal, IAdWithdrawalService> {
	@Autowired
	private IAdWithdrawalService adWithdrawalService;
	@Resource
	private IAdDriverService iAdDriverService;
	
	/**
	 * 分页列表查询
	 *
	 * @param adWithdrawal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "提现申请表-分页列表查询")
	@Operation(summary="提现申请表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdWithdrawal>> queryPageList(AdWithdrawal adWithdrawal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AdWithdrawal> queryWrapper = QueryGenerator.initQueryWrapper(adWithdrawal, req.getParameterMap());
		Page<AdWithdrawal> page = new Page<AdWithdrawal>(pageNo, pageSize);
		IPage<AdWithdrawal> pageList = adWithdrawalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adWithdrawal
	 * @return
	 */
	@AutoLog(value = "提现申请表-添加")
	@Operation(summary="提现申请表-添加")
	@RequiresPermissions("ad:ad_withdrawal:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdWithdrawal adWithdrawal) {
		adWithdrawalService.save(adWithdrawal);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adWithdrawal
	 * @return
	 */
	@AutoLog(value = "提现申请表-编辑")
	@Operation(summary="提现申请表-编辑")
	@RequiresPermissions("ad:ad_withdrawal:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdWithdrawal adWithdrawal) {
		if (adWithdrawal.getStatus()==2){
			//增加一条流水记录
			AdDriver driver = iAdDriverService.getById(adWithdrawal.getDriverId());
			if (driver==null){
				return Result.error("没有找到司机信息!");
			}
//			BigDecimal amount = adWithdrawal.getAmount();
//			BigDecimal balance = driver.getBalance();
//			if (amount.compareTo(balance) > 0){
//				return Result.error("提现金额不能大于余额!");
//			}
//			BigDecimal subtract = balance.subtract(amount);
			AdTransaction adTransaction = new AdTransaction();
			adTransaction.setTransactionType(1);
			adTransaction.setDriverId(adWithdrawal.getDriverId());
			adTransaction.setAmount(adWithdrawal.getAmount());
			adTransaction.setBalance(driver.getBalance());
		}
		adWithdrawalService.updateById(adWithdrawal);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "提现申请表-通过id删除")
	@Operation(summary="提现申请表-通过id删除")
	@RequiresPermissions("ad:ad_withdrawal:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adWithdrawalService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "提现申请表-批量删除")
	@Operation(summary="提现申请表-批量删除")
	@RequiresPermissions("ad:ad_withdrawal:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adWithdrawalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "提现申请表-通过id查询")
	@Operation(summary="提现申请表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdWithdrawal> queryById(@RequestParam(name="id",required=true) String id) {
		AdWithdrawal adWithdrawal = adWithdrawalService.getById(id);
		if(adWithdrawal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adWithdrawal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adWithdrawal
    */
    @RequiresPermissions("ad:ad_withdrawal:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdWithdrawal adWithdrawal) {
        return super.exportXls(request, adWithdrawal, AdWithdrawal.class, "提现申请表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_withdrawal:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdWithdrawal.class);
    }

    /**
     * 小程序司机提现申请
     *
     * @param amount 提现金额
     * @return 处理结果
     */
    @Operation(summary="小程序-司机提现申请")
    @PostMapping(value = "/driverWithdraw")
    public Result<?> driverWithdraw(@RequestParam(name="amount", required=true) BigDecimal amount) {
        log.info("司机申请提现金额: {}", amount);
        try {
//			return Result.OK("提现申请提交成功，请等待审核");
            return adWithdrawalService.driverWithdraw(amount);
        } catch (Exception e) {
            log.error("司机提现申请处理失败", e);
            return Result.error("提现申请处理失败: " + e.getMessage());
        }
    }
}
