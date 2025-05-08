package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.ad.entity.AdMerchant;
import org.jeecg.modules.ad.mapper.AdMerchantMapper;
import org.jeecg.modules.ad.service.IAdMerchantService;
import org.jeecg.modules.ad.service.ICommonLoginUserService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.ad.utils.EntityNameCache;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
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
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private EntityNameCache entityNameCache;

    @Resource
    private ICommonLoginUserService commonLoginUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void AddMerchantAndUser(AdMerchant adMerchant) {
        QueryWrapper<AdMerchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AdMerchant::getPhone, adMerchant.getPhone());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("手机号码已经被录用!");
        }
        // 3. Save merchant
        this.save(adMerchant);
        // 1. Create sys_user
        commonLoginUserService.insertSysUser(adMerchant.getPhone(),adMerchant.getName(),adMerchant.getId(),CommonConstant.MERCHANT_USER_ROLE,CommonConstant.MERCHANT_VALUE);
    }

    @Override
    public String getCompanyNameById(String companyId) {
        if (companyId == null || companyId.trim().isEmpty()) {
            return "";
        }
        
        // 优先从缓存中获取
        String cachedName = entityNameCache.getCompanyName(companyId);
        if (cachedName != null) {
            return cachedName;
        }
        
        // 缓存未命中，从数据库查询
        AdMerchant merchant = this.getById(companyId);
        String companyName = merchant != null ? merchant.getName() : "";
        
        // 存入缓存
        entityNameCache.putCompanyName(companyId, companyName);
        
        return companyName;
    }
}
