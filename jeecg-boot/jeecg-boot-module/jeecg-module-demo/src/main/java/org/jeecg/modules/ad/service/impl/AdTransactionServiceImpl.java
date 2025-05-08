package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.entity.AdReport;
import org.jeecg.modules.ad.entity.AdTransaction;
import org.jeecg.modules.ad.entity.Vo.AdTransactionVO;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Slf4j
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
        UpdateWrapper<AdReport> adReportUpdateWrapper = new UpdateWrapper<>();
        if (reportProcessVo.getStatus()==3){
            addReport(reportProcessVo);
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

            if (publishDetail.getStatus() != 3) { // 假设1是有效状态
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
            adTransaction.setPercentage(publishDetail.getPercentage());
            // 更新司机余额
//            driver.setBalance(newBalance);
//            adDriverService.updateById(driver);

            this.save(adTransaction);
            //更新上报记录信息
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String userName = user.getUsername();
            adReportUpdateWrapper.lambda()
                    .eq(AdReport::getId, reportProcessVo.getReportId())
                    .set(AdReport::getProcessTime, DateTime.now())
                    .set(AdReport::getProcessResult, reportProcessVo.getProcessResult())
                    .set(AdReport::getProcessBy, userName)
                    .set(AdReport::getStatus, reportProcessVo.getStatus());
        }else {
            adReportUpdateWrapper.lambda()
                    .eq(AdReport::getId, reportProcessVo.getReportId())
                    .set(AdReport::getProcessTime, DateTime.now())
                    .set(AdReport::getProcessResult, reportProcessVo.getProcessResult())
                    .set(AdReport::getStatus, reportProcessVo.getStatus());
        }
        adReportService.update(adReportUpdateWrapper);

        return result + "成功!";
    }

    private void addReport(ReportProcessVo reportProcessVo) {
        // 检查状态是否为3，如果是，需要创建或更新待上报数据
        if (reportProcessVo != null && reportProcessVo.getStatus() != null && reportProcessVo.getStatus() == 3) {
            AdReport report = adReportService.getById(reportProcessVo.getReportId());
            log.info("检测到状态为3的上报记录，将进行待上报数据处理");
            Integer currentMaintenanceCount = report.getMaintenanceCount();
            if (currentMaintenanceCount == null) {
                currentMaintenanceCount = 0;
            }
            report.setMaintenanceCount(currentMaintenanceCount+1);
            // 获取必要的参数
            String publishDetailId = report.getPublishDetailId();
            String driverId = report.getDriverId();

            if (publishDetailId == null || driverId == null) {
                log.error("缺少必要参数：publishDetailId或driverId为空");
                throw new RuntimeException("缺少必要参数：广告发布明细ID或司机ID");
            }

            // 使用单次查询检查是否存在待上报的数据
            QueryWrapper<AdReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("publish_detail_id", publishDetailId)
                    .eq("driver_id", driverId)
                    .eq("status", 0);

            AdReport pendingReport = adReportService.getOne(queryWrapper);

            // 获取当前report的report_time用于计算新的上报时间
            Date currentReportTime = report.getReportTime();
            if (currentReportTime == null) {
                currentReportTime = new Date(); // 如果为空，默认使用当前时间
            }

            // 计算新的上报时间：当前report_time往后推一个月
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentReportTime);
            calendar.add(Calendar.MONTH, 1);
            Date newReportTime = calendar.getTime();

            if (pendingReport != null) {
                log.info("找到待上报数据，将更新现有记录：{}", pendingReport.getId());


                // 更新记录
                boolean updateResult = adReportService.updateById(pendingReport);
                if (!updateResult) {
                    log.error("更新待上报数据失败");
                    throw new RuntimeException("更新待上报数据失败");
                }

                log.info("成功更新待上报数据, ID: {}, 新上报时间: {}", pendingReport.getId(), newReportTime);
            } else {
                log.info("未找到待上报数据，将创建新记录");

                // 创建新的AdReport对象
                AdReport newReport = new AdReport();
                newReport.setPublishDetailId(publishDetailId);
                newReport.setDriverId(driverId);
                newReport.setReportTime(newReportTime);
                newReport.setReportType(1); // 设置报告类型为1
                newReport.setStatus(0);     // 设置状态为0（待上报）
                // 更新维护次数
                newReport.setMaintenanceCount(currentMaintenanceCount + 1);
                // 保存新记录
                boolean saveResult = adReportService.save(newReport);
                if (!saveResult) {
                    log.error("创建新的待上报数据失败");
                    throw new RuntimeException("创建新的待上报数据失败");
                }

                log.info("成功创建新的待上报数据, ID: {}, 上报时间: {}", newReport.getId(), newReportTime);
            }
        }
    }

    /**
     * 获取司机交易记录
     * 直接调用mapper的自定义查询方法，减少数据库访问次数
     * 
     * @param driverId 司机ID
     * @param transactionType 交易类型(0维护金,1提现)，可为null表示查询全部
     * @param yearMonth 年月(格式：yyyy-MM)，可为null表示查询全部
     * @return 交易记录列表（带广告名称）
     */
    @Override
    public List<AdTransactionVO> getDriverTransactions(String driverId, Integer transactionType, String yearMonth) {
        // 调用自定义mapper方法，一次查询获取所有需要的数据
        return this.baseMapper.getDriverTransactions(driverId, transactionType, yearMonth);
    }
}
