package org.jeecg.modules.ad.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.service.ICommonLoginUserService;
import org.jeecg.modules.ad.service.IWxLoginService;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConnomLoginUserServiceImpl implements ICommonLoginUserService {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUser getLoginUserInfo(HttpServletRequest request) {
        String token = request.getHeader("X-Access-Token");
        if (oConvertUtils.isEmpty(token)) {
            throw new RuntimeException("未获取到token");
        }
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new RuntimeException("无效的token或用户未登录");
        }
        if (CommonConstant.ADMIN.equals(username)){
            return sysUserService.getUserByName(username);
        }
        return sysUserService.getUserByPhone(username);
    }

    @Override
    public List<String> getRoleCode(SysUser sysUser) {
        return sysUserRoleMapper.getRoleCodeByUserId(sysUser.getId());
    }

    @Override
    public void insertSysUser(String phone, String realname,String relatedId, String roleId, String orgCode) {
        SysUser sysUser = new SysUser();
        sysUser.setCreateTime(new Date());//设置创建时间
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(phone, CommonConstant.PASSWORD, salt);
        sysUser.setPassword(passwordEncode);
        sysUser.setUsername(phone);
        sysUser.setRealname(realname);
        sysUser.setEmail(phone + org.jeecg.modules.ad.utils.CommonConstant.EMAIL);
        sysUser.setPhone(phone);
        sysUser.setStatus(org.jeecg.modules.ad.utils.CommonConstant.INTEGER_VALUE_1);
        sysUser.setDelFlag(org.jeecg.modules.ad.utils.CommonConstant.INTEGER_VALUE_0);
        sysUser.setOrgCode(orgCode);
        sysUser.setActivitiSync(org.jeecg.modules.ad.utils.CommonConstant.INTEGER_VALUE_1);
        sysUser.setWorkNo(Long.toString(IdUtil.getSnowflakeNextId()));
        sysUser.setUserIdentity(CommonConstant.INTEGER_VALUE_1);
        sysUser.setRelatedId(relatedId);
        // Save sys_user
        sysUserService.save(sysUser);
        // 2. Create sys_user_depart relationship
        SysUserRole sysUserRole = new SysUserRole(sysUser.getId(), roleId);
        sysUserRoleMapper.insert(sysUserRole);
    }
}