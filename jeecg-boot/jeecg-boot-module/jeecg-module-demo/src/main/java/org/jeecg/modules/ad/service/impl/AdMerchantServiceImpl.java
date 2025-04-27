package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.jeecg.modules.ad.entity.AdMerchant;
import org.jeecg.modules.ad.mapper.AdMerchantMapper;
import org.jeecg.modules.ad.service.IAdMerchantService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;

/**
 * @Description: 商户表（发布方）
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Service
public class AdMerchantServiceImpl extends ServiceImpl<AdMerchantMapper, AdMerchant> implements IAdMerchantService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserDepartMapper sysUserDepartMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void AddMerchantAndUser(AdMerchant adMerchant) {
        // 1. Create sys_user
        insertSysUser(adMerchant.getPhone(),CommonConstant.MERCHANT_USER_DEPT,CommonConstant.MERCHANT_VALUE);
        // 3. Save merchant
        this.save(adMerchant);
    }

    @Override
    public void insertSysUser(String phone,String deptId,String orgCode){
        SysUser sysUser = new SysUser();
        sysUser.setUsername(phone);
        sysUser.setRealname(phone);
        sysUser.setPassword(CommonConstant.PASSWORD);
        sysUser.setSalt(CommonConstant.SALT);
        sysUser.setEmail(phone + CommonConstant.EMAIL);
        sysUser.setPhone(phone);
        sysUser.setStatus(CommonConstant.INTEGER_VALUE_1);
        sysUser.setDelFlag(CommonConstant.INTEGER_VALUE_0);
        sysUser.setOrgCode(orgCode);
        sysUser.setActivitiSync(CommonConstant.INTEGER_VALUE_1);
        sysUser.setWorkNo(Long.toString(IdUtil.getSnowflakeNextId()));
        sysUser.setUserIdentity(CommonConstant.INTEGER_VALUE_1);
        // Save sys_user
        sysUserMapper.insert(sysUser);
        // 2. Create sys_user_depart relationship
        SysUserDepart userDepart = new SysUserDepart(sysUser.getId(), deptId);
        sysUserDepartMapper.insert(userDepart);
    }
}
