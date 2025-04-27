package org.jeecg.modules.ad.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.ad.service.IWxLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sys")
public class WxLoginController {

    @Autowired
    private IWxLoginService wxLoginService;

    @PostMapping("/vxLogin")
    public Result<Map<String, Object>> mLogin(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        Map<String, Object> result = wxLoginService.getPhoneNumber(code);
        return Result.OK(result);
    }
} 