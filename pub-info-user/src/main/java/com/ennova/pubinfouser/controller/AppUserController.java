package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.service.AppUserService;
import com.ennova.pubinfouser.vo.AppUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "公共信息平台App-登录API")
@Slf4j
@RestController
@RequestMapping("/appController")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppUserController {

    private final AppUserService appUserService;

    @ApiOperation(value = "APP移动端接口-用户登录")
    @PostMapping("/appLoginAll")
    public Callback appLoginAll(String account, String password, String cid) {
        return appUserService.appLoginAll(account,password,cid);
    }

    @ApiOperation(value = "APP移动端接口-获取用户分页列表")
    @GetMapping("/listAppUsers")
    public Callback<List<AppUserVO>> listAppUsers() {
        return appUserService.listAppUsers();
    }

}
