package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfouser.service.DingDingService;
import com.ennova.pubinfouser.vo.DingDeptVO;
import com.ennova.pubinfouser.vo.DingUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "钉钉API")
@RequestMapping("/dingding")
@RestController
@Slf4j
public class DingDingController {
    @Autowired
    private DingDingService dingDingService;

    @ApiOperation(value = "钉钉-获取最后一级部门ID列表", tags = "钉钉API")
    @GetMapping("/listDeptIds")
    public Callback<List<Long>> listDeptIds() {
        return dingDingService.listDeptIds();
    }

    @ApiOperation(value = "钉钉-获取所有部门ID列表", tags = "钉钉API")
    @GetMapping("/listDeptAllIds")
    public Callback<List<Long>> listDeptAllIds() {
        return dingDingService.listDeptAllIds();
    }

    @ApiOperation(value = "钉钉-根据部门集合获取钉钉所有用户详情", tags = "钉钉API")
    @GetMapping("/userDetails")
    public Callback<List<DingUserVO>> userDetails() {
        return dingDingService.userDetails();
    }

    @ApiOperation(value = "钉钉-根据部门集合获取钉钉所有部门详情", tags = "钉钉API")
    @GetMapping("/deptDetails")
    public Callback<List<DingDeptVO>> deptDetails() {
        return dingDingService.deptDetails();
    }
}