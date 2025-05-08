package org.jeecg.modules.ad.service;

import org.jeecg.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ICommonLoginUserService {

    SysUser getLoginUserInfo(HttpServletRequest request);
    List<String> getRoleCode(SysUser sysUser);

    void insertSysUser(String phone,String realname,String relatedId,String roleId,String orgCode);
} 