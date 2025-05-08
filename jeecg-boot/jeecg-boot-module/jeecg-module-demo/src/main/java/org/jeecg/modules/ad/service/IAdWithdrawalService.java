package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdWithdrawal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import java.math.BigDecimal;

/**
 * @Description: 提现申请表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdWithdrawalService extends IService<AdWithdrawal> {
    /**
     * 司机提现申请
     * @param amount 提现金额
     * @return 处理结果
     */
    Result<?> driverWithdraw(BigDecimal amount);
}
