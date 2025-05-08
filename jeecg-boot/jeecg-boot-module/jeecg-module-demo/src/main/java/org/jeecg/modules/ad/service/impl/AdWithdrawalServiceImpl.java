package org.jeecg.modules.ad.service.impl;

import org.jeecg.modules.ad.entity.AdWithdrawal;
import org.jeecg.modules.ad.entity.AdDriver;
import org.jeecg.modules.ad.mapper.AdWithdrawalMapper;
import org.jeecg.modules.ad.mapper.AdDriverMapper;
import org.jeecg.modules.ad.service.IAdWithdrawalService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.math.BigDecimal;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 提现申请表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdWithdrawalServiceImpl extends ServiceImpl<AdWithdrawalMapper, AdWithdrawal> implements IAdWithdrawalService {

    @Resource
    private AdDriverMapper adDriverMapper;

    /**
     * 司机提现申请
     * 实现提现逻辑：验证余额，创建提现记录，扣除司机余额
     * 使用事务确保数据一致性
     * 
     * @param amount 提现金额
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> driverWithdraw(BigDecimal amount) {
        // 参数验证
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("提现金额必须大于0");
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            throw new RuntimeException("未登录或登录已过期");
        }

        // 获取手机号
        String phone = loginUser.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("用户信息不完整，缺少手机号");
        }

        // 查询司机信息（单次数据库查询）
        LambdaQueryWrapper<AdDriver> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdDriver::getPhone, phone);
        AdDriver driver = adDriverMapper.selectOne(queryWrapper);
        
        if (driver == null) {
            throw new RuntimeException("未找到司机信息");
        }

        // 验证余额是否充足
        BigDecimal balance = driver.getBalance();
        if (balance == null || balance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，当前余额：" + (balance == null ? "0" : balance));
        }

        // 创建提现记录
        AdWithdrawal withdrawal = new AdWithdrawal();
        withdrawal.setDriverId(driver.getId());
        withdrawal.setAmount(amount);
        withdrawal.setStatus(0); // 设置状态为待处理
        withdrawal.setCreateTime(new Date());
        withdrawal.setCreateBy(loginUser.getUsername());
        
        // 设置银行信息（如果有）
//        withdrawal.setBankName(driver.getBankName());
//        withdrawal.setBankAccount(driver.getBankAccount());
//        withdrawal.setAccountName(driver.getName());

        // 保存提现记录
        boolean saved = this.save(withdrawal);
        if (!saved) {
            throw new RuntimeException("提现申请保存失败");
        }

        // 更新司机余额（扣除提现金额）
        driver.setBalance(balance.subtract(amount));
        int updated = adDriverMapper.updateById(driver);
        if (updated != 1) {
            throw new RuntimeException("更新司机余额失败");
        }

        return Result.OK("提现申请提交成功，请等待审核");
    }
}
