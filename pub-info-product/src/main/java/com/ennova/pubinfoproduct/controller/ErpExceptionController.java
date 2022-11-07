package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.entity.ErpException;
import com.ennova.pubinfoproduct.service.ErpExceptionService;
import com.ennova.pubinfoproduct.vo.ErpExceptionCountVO;
import com.ennova.pubinfoproduct.vo.ErpExceptionVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "异常信息管理")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/exception")
public class ErpExceptionController {

    private final ErpExceptionService erpExceptionService;

    @ApiOperation(value = "新增和修改异常信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated @ApiParam(value = "新增或修改请求参数", required = true)
                                           ErpExceptionVO erpExceptionVO) {
        return erpExceptionService.insertOrUpdate(erpExceptionVO);
    }

    @ApiOperation(value = "查看异常信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(name = "muduleType", value = "模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配")
    })
    @GetMapping("/exceptionList")
    public Callback<BaseVO<ErpExceptionVO>> opinionBoxList(@RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam("muduleType") Integer muduleType) {
        return erpExceptionService.exceptionList(page, pageSize, muduleType);
    }

    @ApiOperation(value = "查看异常信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "异常信息详情ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<ErpException> getDetail(Integer id) {
        return erpExceptionService.getDetail(id);
    }

    @ApiOperation(value = "异常信息删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return erpExceptionService.delete(id);
    }

    @ApiOperation(value = "查看近三天异常信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dayType", value = "0 ： 查询所有， 1 ： 查询 3天以内", required = true, dataType = "Int")
    })
    @GetMapping("/selectExceptionCountMap")
    public Callback<Map<String, List<String>>> selectExceptionCountMap(@RequestParam("dayType")Integer dayType) {
        return erpExceptionService.selectExceptionCountMap(dayType);
    }

    @ApiOperation(value = "根据模块类型查看每日异常信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "muduleType", value = "模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配", required = true, dataType = "Int")
    })
    @GetMapping("/selectExceptionCounList")
    public Callback<BaseVO<ErpExceptionCountVO>> selectExceptionCounList(@RequestParam(defaultValue = "1") Integer page,
                                                                         @RequestParam(defaultValue = "10") Integer size,
                                                                         @RequestParam("muduleType") Integer muduleType) {
        return erpExceptionService.selectExceptionCounList(page, size, muduleType);
    }

}
