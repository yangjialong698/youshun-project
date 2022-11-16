package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfostore.service.ScheduleAssembleService;
import com.ennova.pubinfostore.vo.ScheduleAssembleListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "公共信息平台-排产装配")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/scheduleAssemble")
public class ScheduleAssembleController {

    private final ScheduleAssembleService scheduleAssembleService;

    @ApiOperation(value = "排产装配 - 装配人员名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "姓名")
    })
    @GetMapping("/selectAssembleUserList")
    public Callback selectAssembleUserList(String userName) {
        return scheduleAssembleService.selectAssembleUserList(userName);
    }

    @ApiOperation(value = "排产装配 - 新增")
    @PostMapping("/insert")
    public Callback insert(@RequestBody ScheduleAssembleListVO scheduleAssembleVO) {
        return scheduleAssembleService.insert(scheduleAssembleVO);
    }

    @ApiOperation(value = "排产装配 - 删除")
    @GetMapping("/delete/{id}")
    public Callback delete(@PathVariable("id") Integer id) {
        return scheduleAssembleService.delete(id);
    }

    @ApiOperation(value = "排产装配 - 根据id查询")
    @PostMapping("/selectById")
    public Callback<ScheduleAssembleListVO> selectById(Integer id) {
        return scheduleAssembleService.selectById(id);
    }

    @ApiOperation(value = "排产装配 - 修改")
    @PostMapping("/update")
    public Callback update(@RequestBody ScheduleAssembleListVO scheduleAssembleVO) {
        return scheduleAssembleService.update(scheduleAssembleVO);
    }

    @ApiOperation(value = "排产装配 - 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "deliveryDate", value = "排产日期"),
            @ApiImplicitParam(name = "searchKey", value = "装配人或工单号")
    })
    @GetMapping("/selectPdaInfo")
    public Callback<BaseVO<ScheduleAssembleListVO>> selectPreAssembleList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, String deliveryDate, String searchKey) {
        return scheduleAssembleService.selectPreAssembleList(page, pageSize, deliveryDate, searchKey);
    }

    @ApiOperation(value = "排产装配 - 导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deliveryDate", value = "排产日期"),
            @ApiImplicitParam(name = "searchKey", value = "装配人或工单号")
    })
    @GetMapping("/export")
    public void exportData(HttpServletResponse response, String deliveryDate, String searchKey) {
        scheduleAssembleService.exportData(response, deliveryDate, searchKey);
    }


}
