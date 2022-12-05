package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.entity.AppNotice;
import com.ennova.pubinfostore.service.AppService;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.getui.push.v2.sdk.common.ApiResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公共信息平台App-问题反馈")
@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppController {

    private final AppService appService;

    /*@ApiOperation(value = "APP移动端接口-单个用户消息推送")
    @PostMapping("/pushToSingleByCid")
    public Callback<ApiResult> pushToSingleByCid(@RequestBody AppNotice appNotice) throws InterruptedException{
        return appService.pushToSingleByCid(appNotice);
    }*/

    @ApiOperation(value = "APP移动端接口-所有用户消息推送")
    @PostMapping("/pushAll")
    public Callback pushAll(@RequestBody AppNotice appNotice) {
        return appService.pushAll(appNotice);
    }

    @ApiOperation(value = "APP移动端接口-消息推送-异步")
    @PostMapping("/pushToSingleByCidAsync")
    @Async
    public Callback<ApiResult> pushToSingleByCidAsync(@RequestBody AppNotice appNotice) throws InterruptedException {
        return appService.pushToSingleByCidAsync(appNotice);
    }

    @ApiOperation(value = "APP移动端接口-批量选择用户消息推送")
    @PostMapping("/alarmNotification")
    public Callback<ApiResult> alarmNotification(String title, String content, Integer userId) {
        return appService.alarmNotification(title, content, userId);
    }

    @ApiOperation(value = "APP移动端接口 - 问题反馈")
    @PostMapping("/pushFeedback")
    public Callback pushFeedback(@RequestBody @Validated @ApiParam(value = "新增问题反馈请求参数", required = true)
                                         ScProblemFeedbackVO scProblemFeedbackVO) throws InterruptedException, JsonProcessingException {
        return appService.pushFeedback(scProblemFeedbackVO);
    }

    @ApiOperation(value = "APP移动端接口 - 责任部门")
    @GetMapping("/selectDutyDepartmentList")
    public Callback selectDutyDepartmentList() {
        return appService.selectDutyDepartmentList();
    }

    @ApiOperation(value = "APP移动端接口 - 责任人")
    @GetMapping("/selectDutyPersonList")
    public Callback selectDutyPersonList(String departmentId) {
        return appService.selectDutyPersonList(departmentId);
    }

    @ApiOperation(value = "APP移动端接口 - 问题反馈查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "详情ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<ScProblemFeedbackVO> getDetail(Integer id) {
        return appService.getDetail(id);
    }

    @ApiOperation(value = "APP移动端接口 -问题反馈删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "详情ID", required = true)
    })
    @GetMapping("/deleteById")
    public Callback deleteById(Integer id){
        return appService.deleteById(id);
    }
}
