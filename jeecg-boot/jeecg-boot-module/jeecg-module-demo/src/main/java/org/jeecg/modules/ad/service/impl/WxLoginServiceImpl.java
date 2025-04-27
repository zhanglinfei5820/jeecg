package org.jeecg.modules.ad.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ad.service.IWxLoginService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WxLoginServiceImpl implements IWxLoginService {

    @Value("${zy.wx.appid}")
    private String appId;

    @Value("${zy.wx.secret}")
    private String secret;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Map<String, Object> getPhoneNumber(String code) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 调用微信接口获取手机号
            String url = String.format("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s", getAccessToken());
            RestTemplate restTemplate = new RestTemplate();
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("code", code);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
            JSONObject jsonObject = JSON.parseObject(response.getBody());
            log.info("jsonObject:",jsonObject.toJSONString());
            if (jsonObject.getInteger("errcode") == 0) {
                JSONObject phoneInfo = jsonObject.getJSONObject("phone_info");
                String phoneNumber = phoneInfo.getString("phoneNumber");
                
                // 根据手机号查询用户
                SysUser sysUser = sysUserService.getUserByPhone(phoneNumber);
                if (sysUser != null) {
                    // 生成token
                    String token = generateToken(sysUser);
                    result.put("token", token);
                    result.put("userInfo", sysUser);
                } else {
                    result.put("error", "用户不存在，请联系管理员");
                }
            } else {
                result.put("error", "获取手机号失败：" + jsonObject.getString("errmsg"));
            }
        } catch (Exception e) {
            result.put("error", "系统错误：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取微信access_token
     */
    private String getAccessToken() {
        String accessToken = (String) redisUtil.get("wx_access_token");
        if (oConvertUtils.isEmpty(accessToken)) {
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, secret);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonObject = JSON.parseObject(response.getBody());
            
            if (jsonObject.containsKey("access_token")) {
                accessToken = jsonObject.getString("access_token");
                // 将access_token存入Redis，设置过期时间为7000秒（微信默认7200秒）
                redisUtil.set("wx_access_token", accessToken, 7000);
            }
        }
        return accessToken;
    }

    /**
     * 生成token
     */
    private String generateToken(SysUser sysUser) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(sysUser.getUsername());
        loginUser.setId(sysUser.getId());
        loginUser.setRealname(sysUser.getRealname());

        // 生成token
        String token = JwtUtil.sign(loginUser.getUsername(), loginUser.getPassword());

        // 设置token缓存
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, loginUser);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        
        return token;
    }
} 