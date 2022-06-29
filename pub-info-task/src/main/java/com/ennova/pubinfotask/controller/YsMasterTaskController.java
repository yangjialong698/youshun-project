package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.dto.FileDelDTO;
import com.ennova.pubinfotask.service.YsMasterTaskService;
import com.ennova.pubinfotask.vo.EditMasterTaskAndFileVO;
import com.ennova.pubinfotask.vo.FileVO;
import com.ennova.pubinfotask.vo.MasterLeve1;
import com.ennova.pubinfotask.vo.YsMasterTaskVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author shibingyang
 */
@ApiSort(value = 2)
@Api(tags = "公共信息平台-主任务")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/masterTask")
public class YsMasterTaskController {

    private final YsMasterTaskService ysMasterTaskService;

    @ApiOperation(value = "主任务(子任务) - 文件上传")
    @ApiOperationSort(value = 1)
    //@Resubmit(delaySeconds = 10)
    @PostMapping("/upload")
    public Callback<FileVO> upload(MultipartFile file) {
        return ysMasterTaskService.uploadFile(file);
    }

    @ApiOperation(value = "主任务(子任务) - 文件删除")
    @ApiOperationSort(value = 2)
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO) {
        return ysMasterTaskService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "主任务 - 新建和修改任务")
    @ApiOperationSort(value = 3)
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated YsMasterTaskVO masterTaskVO){
        return ysMasterTaskService.insertOrUpdate(masterTaskVO);
    }

    @ApiOperation(value = "主任务 - 待发布列表")
    @ApiOperationSort(value = 4)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数")
    })
    @GetMapping("/selectAllByUserId")
    public Callback<BaseVO<LinkedHashMap>> selectAll(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize){
        return ysMasterTaskService.selectAll(page,pageSize);
    }

    @ApiOperation(value = "主任务 - 待发布列表 - 发布")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/updateById")
    public Callback updateById(Integer id){
        return ysMasterTaskService.updateById(id);
    }

    @ApiOperation(value = "主任务 - 获取一条主任务信息,用于修改主任务")
    @ApiOperationSort(value = 6)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/selectTaskAndFileOne")
    public Callback<EditMasterTaskAndFileVO> selectTaskAndFileOne(Integer id){
        return ysMasterTaskService.selectTaskAndFileOne(id);
    }

    @ApiOperation(value = "主任务 - 认领")
    @ApiOperationSort(value = 7)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/receiveTask")
    public Callback receiveTask(Integer id){
        return ysMasterTaskService.receiveTask(id);
    }

    // 主任务哪几种状态下，可以选择完成？？？？
    @ApiOperation(value = "主任务 - 完成")
    @ApiOperationSort(value = 8)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/completeTask")
    public Callback completeTask(Integer id){
        return ysMasterTaskService.completeTask(id);
    }

    @ApiOperation(value = "主任务 - 关闭")
    @ApiOperationSort(value = 9)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/closeTask")
    public Callback closeTask(Integer id){
        return ysMasterTaskService.closeTask(id);
    }

    @ApiOperation(value = "主任务 - 首页 - 负责人")
    @ApiOperationSort(value = 10)
    @GetMapping("/selectBySubTaskManageAllUser")
    public Callback<List<LinkedHashMap>> selectBySubTaskManageAllUser(){
        return ysMasterTaskService.selectBySubTaskManageAllUser();
    }

    @ApiOperation(value = "主任务 - 移交 - 用户")
    @ApiOperationSort(value = 10)
    @GetMapping("/selectTaskMoveAllUser")
    public Callback<List<LinkedHashMap>> selectTaskMoveAllUser(){
        return ysMasterTaskService.selectTaskMoveAllUser();
    }

    @ApiOperation(value = "主任务 - 移交")
    @ApiOperationSort(value = 11)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID", required = false),
            @ApiImplicitParam(name = "diverUserId", value = "要移交的用户ID", required = false ),
            @ApiImplicitParam(name = "handOver", value = "移交日期", required = false )
    })
    @GetMapping("/taskHandOver")
    public Callback taskHandOver(Integer id, Integer diverUserId,String handOver){
        return ysMasterTaskService.taskHandOver(id,diverUserId,handOver);
    }

    @ApiOperation(value = "主任务 - 任务详情")
    @ApiOperationSort(value = 12)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" )
    })
    @GetMapping("/selectTaskDetailsOne")
    public Callback<LinkedHashMap> selectTaskDetailsOne(Integer id){
        return ysMasterTaskService.selectTaskDetailsOne(id);
    }

    @ApiOperation(value = "主任务 - 主任务首页 - 获取任务状态的条数")
    @ApiOperationSort(value = 13)
    @GetMapping("/selectTaskCount")
    public Callback<LinkedHashMap> selectTaskCount(){
        return ysMasterTaskService.selectTaskCount();
    }


    @ApiOperation(value = "主任务 - 主任务首页")
    @ApiOperationSort(value = 14)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "receiveId", value = "认领人ID"),
            @ApiImplicitParam(name = "name", value = "主任务名称"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @GetMapping("/selectMasterLeve1")
    public Callback<BaseVO<MasterLeve1>> selectMasterLeve1(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize, Integer receiveId, String name, Integer status){
        return ysMasterTaskService.selectMasterLeve1(page,pageSize,receiveId,name,status);
    }

    @ApiOperation(value = "主任务 - 进度")
    @ApiOperationSort(value = 15)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主任务ID" ),
            @ApiImplicitParam(name = "rate", value = "进度")
    })
    @GetMapping("/updateRateById")
    public Callback updateRateById(Integer id, Integer rate) {
        return ysMasterTaskService.updateRateById(id,rate);
    }


}
