package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.AdTransaction;
import org.jeecg.modules.ad.entity.Vo.ReportProcessVo;
import org.jeecg.modules.ad.mapper.AdTransactionMapper;
import org.jeecg.modules.ad.service.IAdDriverService;
import org.jeecg.modules.ad.service.IAdPublishDetailService;
import org.jeecg.modules.ad.service.IAdReportService;
import org.jeecg.modules.ad.service.IAdTransactionService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdTransactionServiceImpl extends ServiceImpl<AdTransactionMapper, AdTransaction> implements IAdTransactionService {

    @Resource
    AdTransactionMapper adTransactionMapper;
    @Resource
    IAdPublishDetailService adPublishDetailService;
    @Resource
    IAdReportService adReportService;
    @Resource
    IAdDriverService adDriverService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reportProcess(ReportProcessVo reportProcessVo) {
        String result = "处理";
        //先查询reportId有没有生成流水
        QueryWrapper<AdTransaction> transactionQueryWrapper = new QueryWrapper<>();
        transactionQueryWrapper.lambda().eq(AdTransaction::getReportId, reportProcessVo.getReportId());
        List<AdTransaction> transactionList = this.list(transactionQueryWrapper);
        if (transactionList != null && !transactionList.isEmpty()) {
            return result + "失败，上报id已有流水！";
        }

        //根据发布明细id找到发布明细
        AdPublishDetail publishDetail = adPublishDetailService.getById(reportProcessVo.getPublishDetailId());
        if (publishDetail == null) {
            return result + "失败,没有找到发布明细！";
        }

        if (publishDetail.getStatus() != 1) { // 假设1是有效状态
            return result + "失败，发布明细状态异常！";
        }

        // 获取司机信息
        AdDriver driver = adDriverService.getById(reportProcessVo.getDriverId());
        if (driver == null) {
            return result + "失败，未找到司机信息！";
        }

        //没有流水新增流水
        AdTransaction adTransaction = new AdTransaction();
        adTransaction.setReportId(reportProcessVo.getReportId());
        adTransaction.setDriverId(reportProcessVo.getDriverId());
        adTransaction.setStatus(CommonConstant.STATUS_SUCCESS);
        adTransaction.setType(CommonConstant.TRANSACTION_TYPE_INCOME);
        adTransaction.setAmount(publishDetail.getPrice());
        adTransaction.setRelatedId(reportProcessVo.getPublishDetailId());
        adTransaction.setTransactionType(reportProcessVo.getTransactionType());
        
        // 设置交易后余额
        BigDecimal currentBalance = driver.getBalance();
        BigDecimal newBalance = currentBalance.add(publishDetail.getPrice());
        adTransaction.setBalance(newBalance);
        
        // 更新司机余额
        driver.setBalance(newBalance);
        adDriverService.updateById(driver);
        
        this.save(adTransaction);
        //更新上报记录信息
        UpdateWrapper<AdReport> adReportUpdateWrapper = new UpdateWrapper<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUsername();
        adReportUpdateWrapper.lambda()
                .eq(AdReport::getId, reportProcessVo.getReportId())
                .eq(AdReport::getStatus, CommonConstant.INTEGER_VALUE_0)
                .set(AdReport::getProcessTime, DateTime.now())
                .set(AdReport::getProcessResult, reportProcessVo.getProcessResult())
                .set(AdReport::getProcessBy, userName)
                .set(AdReport::getStatus, reportProcessVo.getStatus());
        adReportService.update(adReportUpdateWrapper);
        return result + "成功!";
    }
}
