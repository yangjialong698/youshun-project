package com.ennova.pubinfofile.service.feign;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "pub-info-user",fallback = PubInfoUserFallback.class)
public interface PubInfoUserClient {

    @ApiOperation(value = "用户管理-根据用户id获取用户信息",  tags = "用户API")
    @GetMapping("/user/getUserById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true),
    })
    public Callback<UserVO> getUserById(@RequestParam("id")Integer id);

}
