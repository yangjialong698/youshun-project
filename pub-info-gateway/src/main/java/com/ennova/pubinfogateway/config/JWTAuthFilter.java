package com.ennova.pubinfogateway.config;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.entity.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * jwt 过滤器
 */
@Component
public class JWTAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private WhiteUrlList whitelistUrls;

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        ServerHttpRequest exchangeRequest = exchange.getRequest();
        String sourceIp = exchangeRequest.getHeaders().getFirst("X-Real-IP");
        exchangeRequest.mutate().header("ip",sourceIp);
        boolean isJump = false;
        if(whitelistUrls.getList().size() != 0) {
            for (String whiteUrl:whitelistUrls.getList()) {
                if(url.contains(whiteUrl)){
                    isJump = true;
                    break;
                }
            }
        }
        if(isJump) {
            return chain.filter(exchange);
        }
//        //忽略以下url请求
//        if(url.contains("/user/login") || url.contains("/user/loginWxStatus") || url.contains("/user/loginWx") || url.contains("/user/getWxUserPhone") || url.contains("/user/getWxUserInfo") || url.contains("/kanban") || url.contains("/upload") || url.contains("_import") || url.contains("_export")||url.contains("/getKanbanPermissions")){
//            return chain.filter(exchange);
//        }
        //忽略以下url请求
        if(url.contains("/user/login") || url.contains("/user/getUserByMobile") || url.contains("/user/checkCode")
                || url.contains("/user/resetPassword") || url.contains("/code/getSmsCode") || url.contains("/user/refreshToken")){
            return chain.filter(exchange);
        }

        //从请求头中取得token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(StringUtils.isEmpty(token)){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");


            Callback res = new Callback(401, "没有权限", null);
            byte[] responseByte = GsonUtil.toJson(res).getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer = response.bufferFactory().wrap(responseByte);
            return response.writeWith(Flux.just(buffer));
        }

        //请求中的token是否在redis中存在
        boolean verifyResult = JWTUtil.verify(token);
        if(!verifyResult){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

            Callback res = new Callback(1004, "不合法的token", null);

            byte[] responseByte = GsonUtil.toJson(res).getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer = response.bufferFactory().wrap(responseByte);
            return response.writeWith(Flux.just(buffer));
        }

        return chain.filter(exchange);
    }
}
