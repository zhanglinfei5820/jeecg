package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdTransaction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.Vo.ReportProcessVo;
import org.jeecg.modules.ad.entity.Vo.AdTransactionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdTransactionService extends IService<AdTransaction> {
    /**
     * 广告上报处理
     */
    String reportProcess(ReportProcessVo reportProcessVo);
    
    /**
     * 获取司机交易记录
     * 根据司机ID查询交易记录，并附带广告名称
     * 
     * @param driverId 司机ID
     * @param transactionType 交易类型(0维护金,1提现)，可为null表示查询全部
     * @param yearMonth 年月(格式：yyyy-MM)，可为null表示查询全部
     * @return 交易记录列表（带广告名称）
     */
    List<AdTransactionVO> getDriverTransactions(String driverId, Integer transactionType, String yearMonth);
}
