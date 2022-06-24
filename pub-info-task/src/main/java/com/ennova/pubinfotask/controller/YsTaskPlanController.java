package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.YsTaskPlan;
import com.ennova.pubinfotask.service.YsTaskPlanService;
import com.ennova.pubinfotask.vo.YsTaskPlanPageListVO;
import com.ennova.pubinfotask.vo.YsTaskPlanTreeVO;
import com.ennova.pubinfotask.vo.YsTaskPlanVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author shibingyang
 */

@ApiSort(value = 4)
@Api(tags = "公共信息平台- 任务计划")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/ysTaskPlan")
public class YsTaskPlanController {

    private final YsTaskPlanService ysTaskPlanService;


    @ApiOperation(value = "计划详情 - 查询所有任务名称")
    @ApiOperationSort(value = 1)
    @GetMapping("/selectMasterTaskAll")
    public Callback<List<LinkedHashMap>> selectMasterTaskAll(){
        return ysTaskPlanService.selectMasterTaskAll();
    }

    @ApiOperation(value = "计划详情 - 新增")
    @ApiOperationSort(value = 2)
    @PostMapping("/insert")
    public Callback insert(@RequestBody @Validated(YsTaskPlanVO.Save.class) YsTaskPlanVO ysTaskPlanVO){
        return ysTaskPlanService.insert(ysTaskPlanVO);
    }

    @ApiOperation(value = "计划详情 - 查询一条记录")
    @ApiOperationSort(value = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "计划详情ID" )
    })
    @GetMapping("/selectOneGroupChecked")
    public Callback<YsTaskPlan> selectOneGroupChecked(Integer id){
        return ysTaskPlanService.selectOneGroupChecked(id);
    }

    @ApiOperation(value = "计划详情 - 更新")
    @ApiOperationSort(value = 4)
    @PutMapping("/selectByPrimaryKey")
    public Callback updateByPrimaryKeySelective(@RequestBody @Validated(YsTaskPlanVO.Update.class) YsTaskPlanVO ysTaskPlanVO) {
        return ysTaskPlanService.updateByPrimaryKeySelective(ysTaskPlanVO);
    }

    @ApiOperation(value = "计划详情 - 列表")
    @ApiOperationSort(value = 9)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID"),
            @ApiImplicitParam(name = "planStatus", value = "计划状态"),
            @ApiImplicitParam(name = "likeName", value = "计划名称")
    })
    @GetMapping("/selectTaskPlanList")
    public Callback<BaseVO<YsTaskPlanPageListVO>> selectTaskPlanList(@RequestParam(defaultValue = "1") Integer page,
                                                                     @RequestParam(defaultValue = "10")Integer pageSize,
                                                                     Integer ysMasterTaskId, Integer planStatus, String likeName){
        return ysTaskPlanService.selectTaskPlanList(page,pageSize,ysMasterTaskId,planStatus,likeName);
    }

    @ApiOperation(value = "计划详情 - 首页 - 树")
    @ApiOperationSort(value = 10)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ysMasterTaskId", value = "主任务ID")
    })
    @GetMapping("/selectTaskPlanTree")
    public Callback<List<YsTaskPlanTreeVO>> selectTaskPlanTree(Integer ysMasterTaskId){
        return ysTaskPlanService.selectTaskPlanTree(ysMasterTaskId);
    }

    @ApiOperation(value = "汪工 - 是否存在用户: 0无")
    @GetMapping("/selectByHaveUserId")
    public Callback selectByHaveUserId(Integer userId){
        return ysTaskPlanService.selectByHaveUserId(userId);
    }

}
