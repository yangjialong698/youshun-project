package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfostore.service.ScAssembleQualityIssueService;
import com.ennova.pubinfostore.vo.ScAssembleQualityIssueVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公共信息平台-装配每日现场质量异常登记")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/assemble")
public class ScAssembleQualityIssueController {

    private final ScAssembleQualityIssueService scAssembleQualityIssueService;

    @ApiOperation(value = "装配每日现场质量异常信息 - 新增和修改")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated @ApiParam(value = "新增或修改请求参数", required = true)
                                           ScAssembleQualityIssueVO scAssembleQualityIssueVO) {
        return scAssembleQualityIssueService.insertOrUpdate(scAssembleQualityIssueVO);
    }

    @ApiOperation(value = "装配每日现场质量异常信息列表 - 查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(name = "productNumber", value = "品号"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")

    })
    @GetMapping("/selectAssembleInfoList")
    public Callback<BaseVO<ScAssembleQualityIssueVO>> selectAssembleInfoList(@RequestParam(defaultValue = "1") Integer page,
                                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                                             @RequestParam("startTime") String startTime,
                                                                             @RequestParam("endTime") String endTime,
                                                                             @RequestParam("productNumber") String productName) {
        return scAssembleQualityIssueService.selectAssembleInfoList(page, pageSize, startTime, endTime, productName);
    }


    @ApiOperation(value = "装配每日现场质量异常登记 - 导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "productNumber", value = "品号")
    })
    @GetMapping("/assembleInfoExportData")
    public void assembleInfoExportData(String startTime, String endTime, String productName) {
        scAssembleQualityIssueService.assembleInfoExportData(startTime, endTime, productName);
    }

    @ApiOperation(value = "装配每日现场质量异常信息 - 删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return scAssembleQualityIssueService.delete(id);
    }

}
