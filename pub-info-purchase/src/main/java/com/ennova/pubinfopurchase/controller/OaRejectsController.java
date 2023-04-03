package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfopurchase.service.OaRejectsService;
import com.ennova.pubinfopurchase.vo.OaPressRejectsVO;
import com.ennova.pubinfopurchase.vo.OaRejectsVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * (project.oa_rejects)表控制层
 *
 * @author xxxxx
 */
@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/rejects")
public class OaRejectsController {

    private final OaRejectsService oaRejectsService;


    @ApiOperation(value = "oa不合格品处理单 - 新增不良品创建单(保存)")
    @PostMapping("/insertRejects")
    public Callback insertRejects(@RequestBody @Validated OaRejectsVO oaRejectsVO) {
        return oaRejectsService.insertRejects(oaRejectsVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 修改不良品创建单(修改)")
    @PostMapping("/updateRejects")
    public Callback updateRejects(@RequestBody @Validated OaRejectsVO oaRejectsVO) {
        return oaRejectsService.updateRejects(oaRejectsVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单信息首页分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "workCenter", value = "工作中心"),
            @ApiImplicitParam(name = "exigencyStatus", value = "紧急程度"),
            @ApiImplicitParam(name = "schedule", value = "进度"),
            @ApiImplicitParam(name = "headline", value = "标题"),
            @ApiImplicitParam(name = "serialNumber", value = "流水号")
    })
    @GetMapping("/selectRejectsInfo")
    public Callback<BaseVO<OaRejectsVO>> selectRejectsInfo(@RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam("startTime") String startTime,
                                                           @RequestParam("endTime") String endTime,
                                                           @RequestParam("workCenter") String workCenter,
                                                           @RequestParam("exigencyStatus") String exigencyStatus,
                                                           @RequestParam("schedule") String schedule,
                                                           @RequestParam("headline") String headline) {
        return oaRejectsService.selectRejectsInfo(page, pageSize, startTime, endTime, workCenter, exigencyStatus, schedule, headline);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单详情信息")
    @GetMapping("/selectRejectsInfoDetail")
    public Callback<OaRejectsVO> selectRejectsInfoDetail(Integer id) {
        return oaRejectsService.selectRejectsInfoDetail(id);
    }

    @ApiOperation(value = "oa不合格品处理单 - 回退")
    @PostMapping("/backRejects")
    public Callback backRejects(Integer id, Integer setpStaus) {
        return oaRejectsService.backRejects(id, setpStaus);
    }

    @ApiOperation(value = "oa不合格品处理单 - 批量催办")
    @PostMapping("/pressRejects")
    public Callback pressRejects(OaPressRejectsVO oaPressRejectsVO) throws MessagingException {
        return oaRejectsService.pressRejects(oaPressRejectsVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 批量删除")
    @PostMapping("/batchRejectsDelete")
    public Callback batchRejectsDelete(@ApiParam(name = "ids", value = "处理单id数组")
                                 @RequestParam("ids[]") Integer[] ids) {
        return oaRejectsService.batchRejectsDelete(ids);
    }


}
