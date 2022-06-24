package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.service.YsTeamService;
import com.ennova.pubinfotask.vo.EditYsTeamVO;
import com.ennova.pubinfotask.vo.ExecutorVO;
import com.ennova.pubinfotask.vo.YsTeamPageListVO;
import com.ennova.pubinfotask.vo.YsTeamVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiSort(value = 3)
@Api(tags = "公共信息平台- 团队管理")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/ysTeam")
public class YsTeamController {

    private final YsTeamService ysTeamService;

    /*自动加载用户部门，故注释掉*/
    //@ApiOperation(value = "团队管理 - 获取所有部门")
    //@ApiOperationSort(value = 1)
    //@GetMapping("/selectDeptAll")
    //public Callback<List<LinkedHashMap>> selectDeptAll(){
    //    return ysTeamService.selectDeptAll();
    //}

    @ApiOperation(value = "团队管理 - 查所有执行人角色的用户")
    @ApiOperationSort(value = 1)
    @GetMapping("/selectAllByRoleExecutor")
    public Callback<List<ExecutorVO>> selectAllByRoleExecutor(){
        return ysTeamService.selectAllByRoleExecutor();
    }

    @ApiOperation(value = "团队管理 - 新增或修改")
    @ApiOperationSort(value = 2)
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated YsTeamVO record) {
        return ysTeamService.insertOrUpdate(record);
    }

    @ApiOperation(value = "团队管理 - 获取一条,用于修改数据")
    @ApiOperationSort(value = 3)
    @GetMapping("/selectTeamOne")
    public Callback<EditYsTeamVO> selectTeamOne(Integer id){
        return ysTeamService.selectTeamOne(id);
    }

    @ApiOperation(value = "团队管理 - 删除")
    @ApiOperationSort(value = 4)
    @GetMapping("/deleteByPrimaryKey")
    public Callback deleteByPrimaryKey(Integer id) {
        return ysTeamService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "团队管理 - 列表")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "name", value = "团队成员名字")
    })
    @GetMapping("/selectAllPageList")
    public Callback<BaseVO<YsTeamPageListVO>> selectAllPageList(@RequestParam(defaultValue = "1") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer pageSize, Integer ysMasterTaskId, String name){
        return ysTeamService.selectAllPageList(page,pageSize,ysMasterTaskId,name);
    }


}
