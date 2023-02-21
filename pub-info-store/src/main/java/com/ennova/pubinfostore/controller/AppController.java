package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfostore.config.LocalLock;
import com.ennova.pubinfostore.entity.ScProblemFeedback;
import com.ennova.pubinfostore.service.AppService;
import com.ennova.pubinfostore.vo.SaveCountVO;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "公共信息平台App-问题反馈")
@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AppController {

    private final AppService appService;

  /*  @ApiOperation(value = "APP移动端接口-单个用户消息推送")
    @PostMapping("/pushToSingleByCid")
    public Callback<ApiResult> pushToSingleByCid(@RequestBody AppNotice appNotice) throws InterruptedException{
        return appService.pushToSingleByCid(appNotice);
    }

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
    }*/

    @ApiOperation(value = "APP移动端接口 - 问题反馈")
    @PostMapping("/pushFeedback")
    @LocalLock(key = "book:arg[0]")
    public Callback pushFeedback(@RequestBody @Validated @ApiParam(value = "新增问题反馈请求参数", required = true)
                                         ScProblemFeedbackVO scProblemFeedbackVO) throws InterruptedException {
        return appService.pushFeedback(scProblemFeedbackVO);
    }

    /*@ApiOperation(value = "APP移动端接口 - 责任部门")
    @GetMapping("/selectDutyDepartmentList")
    public Callback selectDutyDepartmentList() {
        return appService.selectDutyDepartmentList();
    }

    @ApiOperation(value = "APP移动端接口 - 责任人")
    @GetMapping("/selectDutyPersonList")
    public Callback selectDutyPersonList(String departmentId) {
        return appService.selectDutyPersonList(departmentId);
    }*/

    @ApiOperation(value = "APP移动端接口 - 首页查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "详情ID", required = true)
    })
    @GetMapping("/getDetails")
    public Callback<ScProblemFeedbackVO> getDetails(Integer id) {
        return appService.getDetails(id);
    }

    @ApiOperation(value = "APP移动端接口 - 我反馈的查看详情")
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

    @ApiOperation(value = "APP移动端接口 - 我反馈的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "问题描述/责任人", required = false),
    })
    @GetMapping("/getMyProblemFeedbackList")
    public Callback<BaseVO<ScProblemFeedback>> getMyProblemFeedbackList(@RequestParam(defaultValue = "1") Integer page,
                                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                                        String searchKey) {
        return appService.getMyProblemFeedbackList(page,pageSize,searchKey);
    }

    @ApiOperation(value = "APP移动端接口 - 我反馈的问题状态")
    @GetMapping("/getMyProblemsStatus")
    public Callback<ScProblemFeedbackVO> getMyProblemsStatus() {
        return appService.getMyProblemsStatus();
    }

    @ApiOperation(value = "APP移动端接口 - 我经办的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "问题描述/反馈人", required = false),
    })
    @GetMapping("/getMyHandleProblemList")
    public Callback<BaseVO<ScProblemFeedback>> getMyHandleProblemList(@RequestParam(defaultValue = "1") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      String searchKey) {
        return appService.getMyHandleProblemList(page,pageSize,searchKey);
    }

    @ApiOperation(value = "APP移动端接口 - 我经办的问题状态")
    @GetMapping("/getMyHandleProblemsStatus")
    public Callback<ScProblemFeedbackVO> getMyHandleProblemsStatus() {
        return appService.getMyHandleProblemsStatus();
    }

    @ApiOperation(value = "APP移动端接口 - 我经办的查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "详情ID", required = true)
    })
    @GetMapping("/getMyHandleDetail")
    public Callback<ScProblemFeedbackVO> getMyHandleDetail(Integer id) {
        return appService.getMyHandleDetail(id);
    }

    @ApiOperation(value = "APP移动端接口 - 解决问题")
    @PostMapping("/solveProblem")
    public Callback solveProblem(@RequestBody @Validated @ApiParam(value = "解决问题请求参数", required = true)
                                         ScProblemFeedbackVO scProblemFeedbackVO) {
        return appService.solveProblem(scProblemFeedbackVO);
    }

    @ApiOperation(value = "APP移动端接口 - 反馈处理 - 确认解决")
    @PostMapping("/solveProblemFeedback")
    public Callback solveProblemFeedback(@RequestBody @Validated @ApiParam(value = "解决问题请求参数", required = true)
                                         ScProblemFeedbackVO scProblemFeedbackVO) {
        return appService.solveProblemFeedback(scProblemFeedbackVO);
    }

    @ApiOperation(value = "APP移动端接口 - 反馈处理 - 未解决")
    @PostMapping("/NoSolveProblemFeedback")
    public Callback NoSolveProblemFeedback(@RequestBody @Validated @ApiParam(value = "解决问题请求参数", required = true)
                                                 ScProblemFeedbackVO scProblemFeedbackVO) {
        return appService.NoSolveProblemFeedback(scProblemFeedbackVO);
    }

    @ApiOperation(value = "APP移动端接口 - 问题反馈app主页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "反馈状态/责任人", required = false),
    })
    @GetMapping("/getSfbDetailList")
    public Callback<BaseVO<ScProblemFeedbackVO>> getSfbDetailList(Integer page, Integer pageSize, String searchKey) {
        return appService.getSfbDetailList(page,pageSize,searchKey);
    }

    @ApiOperation(value = "APP移动端接口 - 我的统计我反馈的问题数量")
    @GetMapping("/countMyBack")
    public Callback<SaveCountVO> countMyBack() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return appService.countMyBack(req);
    }

    @ApiOperation(value = "APP移动端接口 - 我的统计我经办的问题数量")
    @GetMapping("/countMyJb")
    public Callback<SaveCountVO> countMyJb() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return appService.countMyJb(req);
    }
}
