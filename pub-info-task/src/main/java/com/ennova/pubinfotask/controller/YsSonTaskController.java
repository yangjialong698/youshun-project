package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.YsWorkTime;
import com.ennova.pubinfotask.service.YsSonTaskService;
import com.ennova.pubinfotask.vo.*;
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

@Api(tags = "公共信息平台-子任务")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/ysSonTask")
public class YsSonTaskController {

    private final YsSonTaskService ysSonTaskService;

    @ApiOperation(value = "子任务 - 获取主任务名称(非子任务管理角色,查所有)")
    @ApiOperationSort(value = 1)
    @GetMapping("/selectMasterNameByReceiveId")
    public Callback<List<LinkedHashMap>> selectMasterNameByReceiveId(){
        return ysSonTaskService.selectMasterNameByReceiveId();
    }


    @ApiOperation(value = "子任务 - 获取主任务名称(不含已关闭状态)")
    @ApiOperationSort(value = 1)
    @GetMapping("/selectMasterNameNotCloseByReceiveId")
    public Callback<List<LinkedHashMap>> selectMasterNameNotCloseByReceiveId(){
        return ysSonTaskService.selectMasterNameNotCloseByReceiveId();
    }

    @ApiOperation(value = "子任务 - 当前用户的所有团队成员")
    @ApiOperationSort(value = 2)
    @GetMapping("/selectAllByUserId")
    public Callback<List<EditYsTeamVO>> selectAllByUserId(Integer masterTaskId){
        return ysSonTaskService.selectAllByUserId(masterTaskId);
    }

    @ApiOperation(value = "子任务 - 新增子任务")
    @ApiOperationSort(value = 3)
    @PostMapping("/insertSonTask")
    public Callback insertSonTask(@RequestBody @Validated(YsSonTaskVO.Save.class) YsSonTaskVO record) {
        return ysSonTaskService.insertSonTask(record);
    }

    @ApiOperation(value = "子任务 - 更新子任务")
    @ApiOperationSort(value = 4)
    @PostMapping("/updateSonTask")
    public Callback updateSonTask(@RequestBody @Validated(YsSonTaskVO.Update.class) YsSonTaskVO record) {
        return ysSonTaskService.updateSonTask(record);
    }

    @ApiOperation(value = "子任务 - 子任务详情")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "起始页", required = false),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = false ),
            @ApiImplicitParam(name = "status", value = "状态", required = false ),
            @ApiImplicitParam(name = "teamUserId", value = "执行人", required = false ),
            @ApiImplicitParam(name = "sonTaskName", value = "子任务名称 ( 模糊 ) ", required = false ),
            @ApiImplicitParam(name = "masterTaskId", value = "主任务ID ", required = false )
            //@ApiImplicitParam(name = "masterTaskName", value = "主任务名称 ", required = false )
    })
    @GetMapping("/selectSonTaskPagelist")
    public Callback<BaseVO<YsSonTaskPageListVO>> selectSonTaskPagelist(@RequestParam(defaultValue = "1") Integer page,
                                                                        @RequestParam(defaultValue = "10")Integer pageSize, Integer status,
                                                                        Integer teamUserId, String sonTaskName, Integer masterTaskId){
        return ysSonTaskService.selectSonTaskPagelist(page,pageSize,status,teamUserId,sonTaskName,masterTaskId);
    }

    @ApiOperation(value = "子任务 - 完成")
    @ApiOperationSort(value = 6)
    @GetMapping("/updateSonTaskStatus")
    public Callback updateSonTaskStatus(Integer sonTaskId){
        return ysSonTaskService.updateSonTaskStatus(sonTaskId);
    }

    //@ApiOperation(value = "子任务 - 子任务报工(新增或修改)")
    //@ApiOperationSort(value = 7)
    //@PostMapping("/batchInsertOrUpdateWorkTime")
    //public Callback batchInsertOrUpdateWorkTime(@RequestBody @Validated WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO){
    //    return ysSonTaskService.batchInsertOrUpdateWorkTime(workTimeAddAndUpdateVO);
    //}

    @ApiOperation(value = "子任务 - 子任务报工(新增)")
    @ApiOperationSort(value = 7)
    @PostMapping("/saveWorkTime")
    public Callback saveWorkTime(@RequestBody @Validated WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO){
        return ysSonTaskService.saveWorkTime(workTimeAddAndUpdateVO);
    }

       @ApiOperation(value = "子任务 - 子任务报工(修改)")
    @ApiOperationSort(value = 8)
    @PostMapping("/updateWorkTime")
    public Callback updateWorkTime(@RequestBody @Validated WorkTimeAddAndUpdateVO workTimeAddAndUpdateVO){
        return ysSonTaskService.updateWorkTime(workTimeAddAndUpdateVO);
    }

    @ApiOperation(value = "子任务 - 根据子任务ID - 查所有报工记录")
    @ApiOperationSort(value = 8)
    @GetMapping("/selectAllByYsSonTaskId")
    public Callback<List<YsWorkTime>> selectAllByYsSonTaskId(Integer ysSonTaskId){
        return ysSonTaskService.selectAllByYsSonTaskId(ysSonTaskId);
    }

//    @ApiOperation(value = "子任务 - 删除多个或单个子任务报工记录")
//    @ApiOperationSort(value = 9)
//    @PostMapping("/batchDeleteByPrimaryKey")
//    public Callback batchDeleteByPrimaryKey(@RequestBody List<Integer> ids){
//        return ysSonTaskService.batchDeleteByPrimaryKey(ids);
//    }

    @ApiOperation(value = "子任务 - 单个子任务报工记录")
    @ApiOperationSort(value = 9)
    @GetMapping("/batchDeleteByPrimaryKey")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "删除报工id", required = true)
    })
    public Callback batchDeleteByPrimaryKey(Integer id){
        return ysSonTaskService.batchDeleteByPrimaryKey(id);
    }

    @ApiOperation(value = "子任务首页 - 团队管理 - 职位占比")
    @ApiOperationSort(value = 10)
    @GetMapping("/selectJobTitleGroup")
    public Callback<List<LinkedHashMap>> selectJobTitleGroup(Integer masterTaskId){
        return ysSonTaskService.selectJobTitleGroup(masterTaskId);
    }

    //@ApiOperation(value = "子任务首页 - 团队管理 - 工时占比")
    //@ApiOperationSort(value = 11)
    //@GetMapping("/selectTeamWokeTimeGroup")
    //public Callback<List<WorkTimeChartVO>> selectTeamWokeTimeGroup(Integer masterTaskId){
    //    return ysSonTaskService.selectTeamWokeTimeGroup(masterTaskId);
    //}

    @ApiOperation(value = "子任务首页 - 子任务修改 - 查询一条记录")
    @ApiOperationSort(value = 12)
    @GetMapping("/selectSonTaskOne")
    public Callback<EditSontTaskAndFileVO> selectSonTaskOne(Integer sonTaskId){
        return ysSonTaskService.selectSonTaskOne(sonTaskId);
    }

    @ApiOperation(value = "子任务 - 获取主任务剩余工时和结束日期")
    @ApiOperationSort(value = 13)
    @GetMapping("/selectMasterWorkTimeResidueByReceiveId")
    public Callback<WorkTimeResidueVO> selectMasterWorkTimeResidueByMasterTaskId(Integer masterTaskId){
        return ysSonTaskService.selectMasterWorkTimeResidueByMasterTaskId(masterTaskId);
    }

    @ApiOperation(value = "子任务 - 根据主任务ID,获取执行人子任务占比")
    @ApiOperationSort(value = 14)
    @GetMapping("/selectGroupTeamIdByMasterTaskId")
    public Callback selectGroupTeamIdByMasterTaskId(Integer ysMasterTaskId){
        return ysSonTaskService.selectGroupTeamIdByMasterTaskId(ysMasterTaskId);
    }

    @ApiOperation(value = "子任务 - 状态：开始 -> 进行中")
    @ApiOperationSort(value = 15)
    @GetMapping("/updateStatusBySonTaskId")
    public Callback updateStatusBySonTaskId(Integer sonTaskId) {
        return ysSonTaskService.updateStatusBySonTaskId(sonTaskId);
    }

    @ApiOperation(value = "主任务 - 进度")
    @ApiOperationSort(value = 16)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" ),
            @ApiImplicitParam(name = "rate", value = "进度")
    })
    @GetMapping("/updateRateById")
    public Callback updateRateById(Integer id, Integer rate) {
        return ysSonTaskService.updateRateById(id,rate);
    }

}
