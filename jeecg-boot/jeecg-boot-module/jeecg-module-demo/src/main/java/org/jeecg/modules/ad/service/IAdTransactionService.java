package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdTransaction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.ad.entity.Vo.ReportProcessVo;

/**
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdTransactionService extends IService<AdTransaction> {
    String reportProcess(ReportProcessVo reportProcessVo);
}
