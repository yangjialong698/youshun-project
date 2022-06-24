package com.ennova.pubinfouser.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Component
@FeignClient(value = "pub-info-task",fallback = PubInforTaskFallback.class)
public interface PubInfoTaskClient {

    @ApiOperation(value = "汪工 - 是否存在用户: 0无")
    @GetMapping("/ysTaskPlan/selectByHaveUserId")
    public Callback selectByHaveUserId(@RequestParam("userId")Integer userId);
}
