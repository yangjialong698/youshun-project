package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfostore.service.PcService;
import com.ennova.pubinfostore.vo.ScProblemFeedbackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/12/26
 */
@Api(tags = "公共信息平台Pc-呼叫系统数据看板")
@Slf4j
@RestController
@RequestMapping("/pc")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class PcController {

    private final PcService pcService;

    @ApiOperation(value = "Pc端接口 - 呼叫系统数据看板列表")
    @GetMapping("/getDateBoardList")
    public Callback<BaseVO<ScProblemFeedbackVO>> getDateBoardList(Integer page, Integer pageSize) {
        return pcService.getDateBoardList(page,pageSize);
    }

    @ApiOperation(value = "Pc端接口 - 呼叫历史记录列表")
    @GetMapping("/getHistoryDateBoardList")
    public Callback<List<ScProblemFeedbackVO>> getHistoryDateBoardList() {
        return pcService.getHistoryDateBoardList();
    }

    @ApiOperation(value = "Pc端接口 - 呼叫系统数据看板问题状态")
    @GetMapping("/getMyHandleProblemsStatus")
    public Callback<ScProblemFeedbackVO> getProblemsStatus() {
        return pcService.getProblemsStatus();
    }

}
