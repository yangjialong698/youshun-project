package com.ennova.pubinfowebsite.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfowebsite.fegin.PurchaseFeginClient;
import com.ennova.pubinfowebsite.service.WebsiteService;
import com.ennova.pubinfowebsite.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;
import com.ennova.pubinfowebsite.vo.GwMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
@Api(tags = "公司官网")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/website")
public class WebsiteController {

    private final PurchaseFeginClient client;
    private final WebsiteService websiteService;

    @ApiOperation(value = "公司官网 - 获取公共信息采购系统 - 采购信息列表")
    @GetMapping("/getPurchaseInfo")
    public Callback<BaseVO<CgPurchaseInfoVO>> getPurchaseInfo(Integer page,Integer pageSize,String name) {

        BaseVO<CgPurchaseInfoVO> data = client.selectPurchaseInfo(page, pageSize, name).getData();

        return Callback.success(data);
    }

    @ApiOperation(value = "公司官网 - 在线留言")
    @PostMapping("/onlineMessage")
    public Callback onlineMessage(@RequestBody @Validated GwMessageVO gwMessageVO){
        return websiteService.onlineMessage(gwMessageVO);
    }

    @GetMapping("/useRpcToCaiGou")
    public Callback getByRpc(String name) throws IOException {
        URI uriFinal = null;
        try {
            uriFinal = new URIBuilder()
                    .setScheme("http")
//                    .setHost("123.60.73.204")
                    .setHost("pubinfo.ennova.com.cn/")
                    .setPath("/api//purchase/selectPurchaseInfo")
                    .setParameter("name", name)
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
