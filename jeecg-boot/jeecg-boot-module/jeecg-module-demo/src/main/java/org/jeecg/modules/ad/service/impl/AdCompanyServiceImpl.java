package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.ad.entity.AdCompany;
import org.jeecg.modules.ad.mapper.AdCompanyMapper;
import org.jeecg.modules.ad.service.IAdCompanyService;
import org.jeecg.modules.ad.service.ICommonLoginUserService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 广告公司
 * @Author: jeecg-boot
 * @Date:   2025-04-26
 * @Version: V1.0
 */
@Service
public class AdCompanyServiceImpl extends ServiceImpl<AdCompanyMapper, AdCompany> implements IAdCompanyService {

    @Resource
    private ICommonLoginUserService commonLoginUserService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCompanyAndUser(AdCompany adCompany) {
        QueryWrapper<AdCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AdCompany::getPhone, adCompany.getPhone());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("手机号码已经被录用!");
        }
        this.saveOrUpdate(adCompany);
        commonLoginUserService.insertSysUser(adCompany.getPhone(),adCompany.getName(),adCompany.getId(), CommonConstant.COMPANY_USER_ROLE, CommonConstant.COMPANY_VALUE);
    }
}
