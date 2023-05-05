package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfopurchase.service.OaOperationLogService;
import com.ennova.pubinfopurchase.vo.OaOperationLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "公共信息平台-oa不合格品处理单-日志")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/operationLog")
public class OaOperationLogController {

    private final OaOperationLogService oaOperationLogService;

    @ApiOperation(value = "oa不合格品处理单 - 不良品创建单详情信息 - 日志")
    @GetMapping("/selectOperationLogDetail")
    public Callback<List<OaOperationLogVO>> selectOperationLogDetail(Integer id) {
        return oaOperationLogService.selectOperationLogDetail(id);
    }


}
