package com.ennova.pubinfowebsite.controller;

import com.ennova.pubinfocommon.entity.Callback;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
@Api(tags = "公司官网")
@RestController
@RequestMapping("/website")
public class WebsiteController {

    @ApiOperation(value = "公司官网 - 测试")
    @GetMapping("/test")
    public Callback test() {
        return Callback.success("测试成功");
    }

    @GetMapping("/useRpcToCaiGou")
    public Callback getByRpc() throws IOException {
        URI uriFinal = null;
        try {
            uriFinal = new URIBuilder()
                    .setScheme("http")
//                    .setHost("123.60.73.204")
                    .setHost("pubinfo.ennova.com.cn/")
                    .setPath("/api//user/getUserByMobile")
                    .setParameter("mobile", "13816597315")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String content = null;
        // 1.创建httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2. 创建HttpGet
        HttpGet httpGetTest1 = new HttpGet(uriFinal);
        String resultFinal = null;
        try {
            // 3. 执行RPC
            CloseableHttpResponse response =  httpclient.execute(httpGetTest1);
            // 4.获取RPC响应结结果
            HttpEntity result = response.getEntity();
            resultFinal = EntityUtils.toString(result, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Callback.success(resultFinal);
    }

}
