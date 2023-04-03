package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.dao.PubAppVersionMapper;
import com.ennova.pubinfouser.entity.PubAppVersion;
import com.ennova.pubinfouser.service.AppUserService;
import com.ennova.pubinfouser.vo.AppUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共信息平台App-登录API")
@Slf4j
@RestController
@RequestMapping("/appController")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    private PubAppVersionMapper pubAppVersionMapper;

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



    @ApiOperation(value = "查询APP-版本")
    @GetMapping("/getAppVersion")
    public Callback<Boolean> getAppVersion(@RequestParam String appVersion){
        PubAppVersion version = pubAppVersionMapper.selectByAppVersion(appVersion,3);
        return Callback.success(version !=null);
    }
    @ApiOperation(value = "查询APP-安卓版本")
    @GetMapping("/getAppAndroidVersion")
    public Callback<String> getAppAndroidVersion(){
        PubAppVersion version = pubAppVersionMapper.selectByAppVersionType(1);
        return Callback.success(version.getAppVersion());
    }
    @ApiOperation(value = "查询APP-IOS版本")
    @GetMapping("/getAppIosVersion")
    public Callback<String> getAppIosVersion(){
        PubAppVersion version = pubAppVersionMapper.selectByAppVersionType(2);
        return Callback.success(version.getAppVersion());
    }

}
