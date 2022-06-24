package com.ennova.pubinfouser.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-22
 */
@Configuration
@Data
public class AliSmsConfigure {

    /**
     * 短信API产品名称（短信产品名固定，无需修改）
     */

    @Value("${aliyun.sms.product}")
    private String product;

    /**
     * 短信API产品域名（接口地址固定，无需修改）
     */

    @Value("${aliyun.sms.domain}")
    private String domain;

    @Value("${aliyun.sms.accessKey}")
    private String accessKey;

    @Value("${aliyun.sms.accessSecret}")
    private String accessSecret;
}