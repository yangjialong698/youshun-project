package com.ennova.pubinfostore.service.feign;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.vo.AppUserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(value = "pub-info-user",fallback = PubInfoUserFallback.class)
public interface PubInfoUserClient {

    @ApiOperation(value = "APP移动端接口-获取用户分页列表",  tags = "用户API")
    @GetMapping("/appController/listAppUsers")
    public Callback<List<AppUserVO>> listAppUsers();

}
