package com.ennova.pubinfostore.utils;

import com.getui.push.v2.sdk.GtApiConfiguration;

public class ApiContext {

    public GtApiConfiguration configuration;
    public String cid;

    public static ApiContext build() {
        ApiContext context = new ApiContext();
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        context.configuration = apiConfiguration;

        apiConfiguration.setAppId("m7IwMVPJ129ZG1uEjGyzs4");
        apiConfiguration.setAppKey("PxkdXE9VOhATUdj4wblu08");
        apiConfiguration.setMasterSecret("ei2nNz22ec6gaMYLgEF112");
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");

        return context;
    }
}
