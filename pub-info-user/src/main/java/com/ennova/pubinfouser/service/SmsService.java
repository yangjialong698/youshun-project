package com.ennova.pubinfouser.service;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ennova.pubinfocommon.utils.RandomUtil;
import com.ennova.pubinfouser.config.AliSmsConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-22
 */

@Service
@Slf4j
public class SmsService {

    /**
     * 短信签名
     */
    @Value("${aliyun.sms.signName}")
    private String signName;

    @Autowired
    private AliSmsConfigure aliSmsConfigure;

    public String sendCode(String userTel, String templateCode) {
        try {
            String randomNum = RandomUtil.randomNum(6);
            JSONObject templateParam = new JSONObject();
            templateParam.put("code", randomNum);
            IAcsClient acsClient = this.getIAcsClient();
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(userTel);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时
            if (templateParam != null && templateParam.size() > 0) {
                request.setTemplateParam(templateParam.toJSONString());
            }
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse!=null && StringUtils.isNotEmpty(sendSmsResponse.getCode())){
                if (sendSmsResponse.getCode().equals("OK")){
                    return randomNum;
                }
            }
        }catch (ClientException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取IAcsClient
     * @throws ClientException
     */
    private IAcsClient getIAcsClient() throws ClientException {
        // 设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliSmsConfigure.getAccessKey(),
                aliSmsConfigure.getAccessSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", aliSmsConfigure.getProduct(),
                aliSmsConfigure.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return acsClient;
    }

}
