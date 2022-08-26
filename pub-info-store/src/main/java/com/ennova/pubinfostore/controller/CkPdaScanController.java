package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfostore.service.CkPdaScanService;
import com.ennova.pubinfostore.vo.CkPdaScanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/1
 */
@Api(tags = "公共信息平台-PDA扫描")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/pda")
public class CkPdaScanController {

    private final CkPdaScanService ckPdaScanService;

    @ApiOperation(value = "PDA扫描 - 新增条形码比对入库")
    @PostMapping("/insert")
    public Callback insert(@RequestBody @Validated CkPdaScanVO ckPdaScanVO){
        return ckPdaScanService.insert(ckPdaScanVO);
    }

    @ApiOperation(value = "PDA扫描 - 条形码比对列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "barCode", value = "条码1/条码2")
    })
    @GetMapping("/selectPdaInfo")
    public Callback<BaseVO<CkPdaScanVO>> selectPdaInfo(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, String barCode, String startTime, String endTime){
        return ckPdaScanService.selectPdaInfo(page, pageSize, barCode, startTime, endTime);
    }

}
