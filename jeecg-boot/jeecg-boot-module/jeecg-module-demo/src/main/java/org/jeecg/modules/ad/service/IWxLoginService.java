package org.jeecg.modules.ad.service;

import java.util.Map;

public interface IWxLoginService {
    /**
     * 获取微信用户手机号
     * @param code 微信登录code
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> getPhoneNumber(String code);
} 