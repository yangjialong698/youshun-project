package com.ennova.pubinfouser.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfouser.dto.BaseDTO;
import com.ennova.pubinfouser.dto.RoleDTO;
import com.ennova.pubinfouser.entity.TDingClock;
import com.ennova.pubinfouser.service.DingDingService;
import com.ennova.pubinfouser.vo.DingDeptVO;
import com.ennova.pubinfouser.vo.DingUserVO;
import com.ennova.pubinfouser.vo.TDingClockVO;
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
    @GetMapping("/lastDeptIds")
    public Callback<List<Long>> lastDeptIds() {
        return dingDingService.lastDeptIds();
    }

    @ApiOperation(value = "钉钉-获取所有部门ID列表", tags = "钉钉API")
    @GetMapping("/listDeptAllIds")
    public Callback<List<Long>> listDeptAllIds() {
        return dingDingService.listDeptAllIds();
    }

    @ApiOperation(value = "钉钉-根据部门集合获取钉钉所有用户详情", tags = "钉钉API")
    @GetMapping("/userDetails")
    public void userDetails() {
        dingDingService.userDetails();
    }

    @PostMapping("/updatTempTdept")
    @ApiOperation(value = "测试", tags = "测试")
    public void updatTempTdept() {
         dingDingService.updatTuser();
    }

    @ApiOperation(value = "钉钉-根据部门集合获取钉钉所有部门详情", tags = "钉钉API")
    @GetMapping("/deptDetails")
    public Callback<List<DingDeptVO>> deptDetails() {
        return dingDingService.deptDetails();
    }

    @ApiOperation(value = "钉钉-根据用户ID获取打卡记录", tags = "钉钉API")
    @GetMapping("/queryClockList")
    public Callback<BaseVO<TDingClock>> queryClockList(Integer page, Integer pageSize, String userIds, String checkDateFrom, String checkDateTo) {
        return dingDingService.queryClockList(page,pageSize,userIds,checkDateFrom,checkDateTo);
    }

    @ApiOperation(value = "钉钉-测试定时任务打卡记录", tags = "钉钉API")
    @GetMapping("/listClock")
    public Callback<List<TDingClock>> listClock(String userIds, String checkDateFrom, String checkDateTo) {
        return dingDingService.listClock(userIds,checkDateFrom,checkDateTo);
    }
}
