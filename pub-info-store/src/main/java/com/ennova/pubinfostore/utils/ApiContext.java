package com.ennova.pubinfostore.utils;

import com.getui.push.v2.sdk.GtApiConfiguration;

public class ApiContext {

    public GtApiConfiguration configuration;
    public String cid;

    public static ApiContext build() {
        ApiContext context = new ApiContext();
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        context.configuration = apiConfiguration;

        apiConfiguration.setAppId("0ZLLyVuNNt6PK5dVI2Jh25");
        apiConfiguration.setAppKey("FDkpgz6eODAfpon9sPsfR8");
        apiConfiguration.setMasterSecret("1jyhZ1TZ9h75mKDHmkBLO");
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        context.cid = "580286687075119744";

        return context;
    }
}
