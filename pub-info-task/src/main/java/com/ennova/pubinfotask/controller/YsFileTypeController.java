package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.YsFileType;
import com.ennova.pubinfotask.service.YsFileTypeService;
import com.ennova.pubinfotask.vo.YsFileTypeVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Author: shibingyang
 * @Description:  文件类型
 * @Date: 2022/4/22 16:13
 * @Parms:
 * @ReturnType:
 */
@ApiSort(value = 1)
@Api(tags = "公共信息平台-文件类型")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/ysFileType")
public class YsFileTypeController {


    private final YsFileTypeService ysFileTypeService;

    @ApiOperation(value = "文件类型 - 新建或更新")
    @ApiOperationSort(value = 1)
    @PostMapping("/insertSelective")
    public Callback insertOrUpdate(@RequestBody @Validated YsFileTypeVO vo) {
        return ysFileTypeService.insertOrUpdate(vo);
    }

    @ApiOperation(value = "文件类型 - 删除")
    @ApiOperationSort(value = 2)
    @GetMapping("/updateByPrimaryKey")
    public Callback updateByPrimaryKey(Integer id) {
        return ysFileTypeService.updateByPrimaryKey(id);
    }

    @ApiOperation(value = "文件类型 - 列表查询")
    @ApiOperationSort(value = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数" ),
            @ApiImplicitParam(name = "name", value = "类型名称" , required = false)
    })
    @GetMapping("/selectByNameList")
    public Callback<BaseVO<YsFileType>> selectByNameList(@RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer pageSize, String name){
        return ysFileTypeService.selectByNameList(page,pageSize,name);
    }

    @ApiOperation(value = "文件类型 - 无分页, 查所有")
    @ApiOperationSort(value = 4)
    @GetMapping("/selectAll")
    public Callback<List<YsFileType>> selectAll(){
        return ysFileTypeService.selectAll();
    }

}
