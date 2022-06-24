package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-22
 */

@Api(tags = "验证码API")
@RequestMapping("/code")
@RestController
@Slf4j
public class SmsController {

    @Value("${aliyun.sms.templateCode.resetPassword}")
    private String resetPasswordTemplateCode;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SmsService smsService;

    @ApiOperation(value = "重置密码短信验证码", tags = "发送短信API")
    @GetMapping("/getSmsCode")
    public Callback appletResetPassword(String userTel) {
        String code = smsService.sendCode(userTel, resetPasswordTemplateCode);
        if (StringUtils.isNotEmpty(code)) {
            redisTemplate.opsForValue().set("reset_password_sms_code:" + userTel, code, 300, TimeUnit.SECONDS);
            return Callback.success(true);
        }
        return Callback.success(false);
    }
}
