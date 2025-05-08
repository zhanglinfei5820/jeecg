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
import javax.validation.Valid;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.entity.AdInspection;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdTransaction;
import org.jeecg.modules.ad.entity.Vo.ReportProcessVo;
import org.jeecg.modules.ad.service.IAdPublishDetailService;
import org.jeecg.modules.ad.service.IAdTransactionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.ad.service.ICommonLoginUserService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
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
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.Vo.AdTransactionVO;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

 /**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Tag(name="流水记录表")
@RestController
@RequestMapping("/ad/adTransaction")
@Slf4j
public class AdTransactionController extends JeecgController<AdTransaction, IAdTransactionService> {
	@Autowired
	private IAdTransactionService adTransactionService;
	@Resource
	private ICommonLoginUserService commonLoginUserService;
	@Resource
	private IAdPublishDetailService iAdPublishDetailService;

	/**
	 * 分页列表查询
	 *
	 * @param adTransaction
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "流水记录表-分页列表查询")
	@Operation(summary="流水记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AdTransaction>> queryPageList(AdTransaction adTransaction,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		SysUser loginUser = commonLoginUserService.getLoginUserInfo(req);
		if (loginUser == null) {
			return Result.error("用户未登录");
		}
        QueryWrapper<AdTransaction> queryWrapper = QueryGenerator.initQueryWrapper(adTransaction, req.getParameterMap());
		if (!CommonConstant.ADMIN.equals(loginUser.getUsername())) {
			List<String> roleCodes = commonLoginUserService.getRoleCode(loginUser);
			if (roleCodes.contains(CommonConstant.ROLE_CODE_ADCOMPANY)) {
				QueryWrapper<AdPublishDetail> queryAdPulishWrapper = new QueryWrapper<>();
				queryAdPulishWrapper.eq("company_id", loginUser.getRelatedId());
				List<AdPublishDetail> adPublishDetailList = iAdPublishDetailService.list(queryAdPulishWrapper);
				List<String> publishDetailIdList = adPublishDetailList.stream().map(AdPublishDetail::getId).collect(Collectors.toList());
				if (publishDetailIdList.isEmpty()) {
					return Result.OK(new Page<>());
				}
				queryWrapper.lambda().in(AdTransaction::getRelatedId,publishDetailIdList);
			}
		}
		Page<AdTransaction> page = new Page<AdTransaction>(pageNo, pageSize);
		IPage<AdTransaction> pageList = adTransactionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param adTransaction
	 * @return
	 */
	@AutoLog(value = "流水记录表-添加")
	@Operation(summary="流水记录表-添加")
	@RequiresPermissions("ad:ad_transaction:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AdTransaction adTransaction) {
		adTransactionService.save(adTransaction);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param adTransaction
	 * @return
	 */
	@AutoLog(value = "流水记录表-编辑")
	@Operation(summary="流水记录表-编辑")
	@RequiresPermissions("ad:ad_transaction:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AdTransaction adTransaction) {
		adTransactionService.updateById(adTransaction);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流水记录表-通过id删除")
	@Operation(summary="流水记录表-通过id删除")
	@RequiresPermissions("ad:ad_transaction:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		adTransactionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "流水记录表-批量删除")
	@Operation(summary="流水记录表-批量删除")
	@RequiresPermissions("ad:ad_transaction:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adTransactionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "流水记录表-通过id查询")
	@Operation(summary="流水记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AdTransaction> queryById(@RequestParam(name="id",required=true) String id) {
		AdTransaction adTransaction = adTransactionService.getById(id);
		if(adTransaction==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(adTransaction);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param adTransaction
    */
    @RequiresPermissions("ad:ad_transaction:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdTransaction adTransaction) {
        return super.exportXls(request, adTransaction, AdTransaction.class, "流水记录表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ad:ad_transaction:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AdTransaction.class);
    }

	 /**
	  *   广告上报处理
	  *
	  * @param reportProcessVo
	  * @return
	  */
	 @AutoLog(value = "广告上报处理")
	 @Operation(summary="广告上报处理")
//	 @RequiresPermissions("ad:ad_transaction:reportProcess")
	 @PostMapping(value = "/reportProcess")
	 public Result<String> reportProcess(@Valid @RequestBody ReportProcessVo reportProcessVo) {
		 return Result.OK(adTransactionService.reportProcess(reportProcessVo));
	 }

    /**
     * 小程序-司机流水记录查询
     * 根据当前登录用户获取司机ID，查询交易记录并按月份分组
     * 返回最近6个月的交易记录（当月和前5个月）
     *
     * @param transactionType 交易类型(0维护金,1提现)，不传表示查询全部
     * @param yearMonth 指定年月(格式：yyyy-MM)，不传则查询最近6个月
     * @return 按月份分组的流水记录
     */
    @Operation(summary="小程序-司机流水记录查询")
    @GetMapping(value = "/driverTransactions")
    public Result<Map<String, List<AdTransactionVO>>> driverTransactions(
            @RequestParam(name="transactionType", required=false) Integer transactionType,
            @RequestParam(name="yearMonth", required=false) String yearMonth,HttpServletRequest request) {
        try {
			SysUser loginUserInfo = commonLoginUserService.getLoginUserInfo(request);
			if (loginUserInfo == null) {
				return Result.error("无效的token或用户未登录");
			}
//            // 获取当前登录用户
//			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//            if (sysUser == null) {
//                return Result.error("未登录或登录已过期");
//            }
            
            // 获取关联的司机ID
            String driverId = loginUserInfo.getRelatedId();
            if (driverId == null || driverId.trim().isEmpty()) {
                return Result.error("用户未关联司机信息");
            }
            Map<String, List<AdTransactionVO>> resultMap = new LinkedHashMap<>();
            
            // 如果指定了年月，则只查询该月的数据
            if (yearMonth != null && !yearMonth.trim().isEmpty()) {
                List<AdTransactionVO> transactions = adTransactionService.getDriverTransactions(driverId, transactionType, yearMonth);
                resultMap.put(yearMonth, transactions);
                return Result.OK(resultMap);
            }
            
            // 未指定年月，查询最近6个月数据
            // 获取当前月份
            Calendar calendar = Calendar.getInstance();
            
            // 处理当前月和前5个月
            for (int i = 0; i < 6; i++) {
                // 计算月份（当前月和前5个月）
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -i);
                
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Calendar月份从0开始
                
                String monthKey = year + "-" + (month < 10 ? "0" + month : month);
                
                // 查询当月数据
                List<AdTransactionVO> monthlyTransactions = adTransactionService.getDriverTransactions(
                    driverId, transactionType, monthKey);
                
                resultMap.put(monthKey, monthlyTransactions);
            }
            
            return Result.OK(resultMap);
        } catch (Exception e) {
            log.error("查询司机流水记录失败", e);
            return Result.error("查询流水记录失败: " + e.getMessage());
        }
    }
}
