package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.entity.ErpSpecialException;
import com.ennova.pubinfoproduct.service.ErpSpecialExceptionService;
import com.ennova.pubinfoproduct.vo.ErpExceptionCountVO;
import com.ennova.pubinfoproduct.vo.ErpSpecialExceptionVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "特别关注异常信息管理")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/specialException")
public class ErpSpecialExceptionController {

    private final ErpSpecialExceptionService erpSpecialExceptionService;

    @ApiOperation(value = "新增和修改特别关注异常信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated @ApiParam(value = "新增或修改请求参数", required = true)
                                           ErpSpecialExceptionVO erpSpecialExceptionVO) {
        return erpSpecialExceptionService.insertOrUpdate(erpSpecialExceptionVO);
    }

    @ApiOperation(value = "查看特别关注异常信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数")
    })
    @GetMapping("/exceptionList")
    public Callback<BaseVO<ErpSpecialExceptionVO>> opinionBoxList(@RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return erpSpecialExceptionService.exceptionList(page, pageSize);
    }

    @ApiOperation(value = "查看特别关注异常信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "异常信息详情ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<ErpSpecialException> getDetail(Integer id) {
        return erpSpecialExceptionService.getDetail(id);
    }

    @ApiOperation(value = "特别关注异常信息删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return erpSpecialExceptionService.delete(id);
    }


    @ApiOperation(value = "查看每日特别关注异常信息")
    @GetMapping("/selectExceptionCountList")
    public Callback<List<ErpExceptionCountVO>> selectExceptionCountList() {
        return erpSpecialExceptionService.selectExceptionCountList();
    }

}
