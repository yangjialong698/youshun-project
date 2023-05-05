package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.dto.FileDelDTO;
import com.ennova.pubinfopurchase.dto.OaBathRejectsDeleteDTO;
import com.ennova.pubinfopurchase.service.OaRejectsService;
import com.ennova.pubinfopurchase.vo.FileVO;
import com.ennova.pubinfopurchase.vo.OaPressRejectsVO;
import com.ennova.pubinfopurchase.vo.OaRejectsVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/rejects")
public class OaRejectsController {

    private final OaRejectsService oaRejectsService;


    @ApiOperation(value = "oa不合格品处理单 - 新增不良品创建单(保存)")
    @PostMapping("/insertOrUpdateRejects")
    public Callback insertOrUpdateRejects(@RequestBody @Validated OaRejectsVO oaRejectsVO) {
        return oaRejectsService.insertOrUpdateRejects(oaRejectsVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单信息首页分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "workCenter", value = "工作中心"),
            @ApiImplicitParam(name = "userName", value = "创建人"),
            @ApiImplicitParam(name = "schedule", value = "进度"),
            @ApiImplicitParam(name = "headline", value = "标题"),
            @ApiImplicitParam(name = "serialNumber", value = "流水号")
    })
    @GetMapping("/selectRejectsInfo")
    public Callback<PageInfo<OaRejectsVO>> selectRejectsInfo(@RequestParam(defaultValue = "1") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam("startTime") String startTime,
                                                             @RequestParam("endTime") String endTime,
                                                             @RequestParam("workCenter") String workCenter,
                                                             @RequestParam("userName") String userName,
                                                             @RequestParam("schedule") String schedule,
                                                             @RequestParam("headline") String headline) {
        return oaRejectsService.selectRejectsInfo(page, pageSize, startTime, endTime, workCenter, userName, schedule, headline);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单信息首页分页列表 - 高级搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "workCenter", value = "工作中心"),
            @ApiImplicitParam(name = "userName", value = "创建人"),
            @ApiImplicitParam(name = "opinionUser", value = "会签人"),
            @ApiImplicitParam(name = "transactor", value = "最新步骤未办理人"),
            @ApiImplicitParam(name = "schedule", value = "进度"),
            @ApiImplicitParam(name = "headline", value = "标题"),
            @ApiImplicitParam(name = "serialNumber", value = "流水号")
    })
    @GetMapping("/selectRejectsInfos")
    public Callback<PageInfo<OaRejectsVO>> selectRejectsInfos(@RequestParam(defaultValue = "1") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam("startTime") String startTime,
                                                             @RequestParam("endTime") String endTime,
                                                             @RequestParam("workCenter") String workCenter,
                                                              @RequestParam("userName") String userName,
                                                              @RequestParam("opinionUser") String opinionUser,
                                                              @RequestParam("transactor") String transactor,
                                                             @RequestParam("schedule") String schedule,
                                                             @RequestParam("headline") String headline) {
        return oaRejectsService.selectRejectsInfos(page, pageSize, startTime, endTime, workCenter, userName, opinionUser, transactor, schedule, headline);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单详情信息")
    @GetMapping("/selectRejectsInfoDetail")
    public Callback<OaRejectsVO> selectRejectsInfoDetail(Integer id) {
        return oaRejectsService.selectRejectsInfoDetail(id);
    }

    @ApiOperation(value = "oa不合格品处理单 - 回退")
    
    @PostMapping("/backRejects")
    public Callback backRejects(Integer id, Integer setpStaus, String backReason) {
        return oaRejectsService.backRejects(id, setpStaus, backReason);
    }

    @ApiOperation(value = "oa不合格品处理单 - 批量催办")
    @PostMapping("/pressRejects")
    public Callback pressRejects(@RequestBody @Validated OaPressRejectsVO oaPressRejectsVO) throws MessagingException {
        return oaRejectsService.pressRejects(oaPressRejectsVO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 批量删除")
    @PostMapping("/batchRejectsDelete")
    public Callback batchRejectsDelete(@RequestBody @Validated OaBathRejectsDeleteDTO oaBathRejectsDeleteDTO) {
        return oaRejectsService.batchRejectsDelete(oaBathRejectsDeleteDTO);
    }

    @ApiOperation(value = "oa不合格品处理单 -  不良原因分析报告文件上传")
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file) {
        return oaRejectsService.uploadFile(file);
    }

    @ApiOperation(value = "oa不合格品处理单 - 不良原因分析报告文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO) {
        return oaRejectsService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "oa不合格品处理单 - 批量删除状态")
    @GetMapping("/selectBatchDeletes")
    public Callback selectBatchDeletes() {
        return oaRejectsService.selectBatchDeletes();
    }

    @ApiOperation(value = "oa不合格品处理单 - 新增处理明细")
    @PostMapping("/insertProcessingDetails")
    public Callback insertProcessingDetails(@RequestBody OaRejectsVO oaRejectsVO) {
        return oaRejectsService.insertProcessingDetails(oaRejectsVO);
    }
}
