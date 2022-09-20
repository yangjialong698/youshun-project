package com.ennova.pubinfowebsite.controller;

import com.ennova.pubinfocommon.entity.Callback;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
