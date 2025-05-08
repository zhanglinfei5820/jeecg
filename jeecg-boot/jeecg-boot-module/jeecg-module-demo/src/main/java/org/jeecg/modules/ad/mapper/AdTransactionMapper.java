package org.jeecg.modules.ad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ad.entity.AdTransaction;
import org.jeecg.modules.ad.entity.Vo.AdTransactionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface AdTransactionMapper extends BaseMapper<AdTransaction> {
    /**
     * 获取司机交易记录
     * @param driverId 司机ID
     * @param transactionType 交易类型(0维护金,1提现)，可为null表示查询全部
     * @param yearMonth 年月(格式：yyyy-MM)，可为null表示查询全部
     * @return 交易记录列表（带广告名称）
     */
    List<AdTransactionVO> getDriverTransactions(
        @Param("driverId") String driverId, 
        @Param("transactionType") Integer transactionType,
        @Param("yearMonth") String yearMonth);
}
