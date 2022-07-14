package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.dto.PublishDTO;
import com.ennova.pubinfotask.service.YsBulletinService;
import com.ennova.pubinfotask.vo.YsBulletinVO;
import com.ennova.pubinfotask.vo.YsMessageVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiSort(value = 1)
@Api(tags = "公共信息平台-公告")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/bulletin")
public class YsBulletinController {

    private final YsBulletinService ysBulletinService;

    @ApiOperation(value = "公告 - 审核人")
    @ApiOperationSort(value = 1)
    @GetMapping("/getReviewerList")
    public Callback getReviewerList() {
        return ysBulletinService.getReviewerList();
    }

    @ApiOperation(value = "公告 - 新增")
    @ApiOperationSort(value = 2)
    @PostMapping("/insert")
    public Callback insert(@RequestBody @Validated PublishDTO publishDTO) {
        return ysBulletinService.insert(publishDTO);
    }

    @ApiOperation(value = "公告 - 修改")
    @ApiOperationSort(value = 3)
    @PostMapping("/update")
    public Callback update(@RequestBody @Validated PublishDTO publishDTO) {
        return ysBulletinService.update(publishDTO);
    }

    @ApiOperation(value = "公告 - 删除")
    @ApiOperationSort(value = 4)
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return ysBulletinService.delete(id);
    }

    @ApiOperation(value = "公告 - 列表")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "status", value = "状态   0:待审核 1:审核通过 2:审核不通过 - 驳回"),
            @ApiImplicitParam(name = "likeTitle", value = "公告名称或ID")
    })
    @GetMapping("/getBulletinList")
    public Callback<BaseVO<YsBulletinVO>> getBulletinList(@RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          Integer status, String likeTitle) {
        return ysBulletinService.getBulletinList(page, pageSize, status, likeTitle);
    }

    @ApiOperation(value = "公告 - 详情(根据id)")
    @ApiOperationSort(value = 6)
    @GetMapping("/getBulletinDetail")
    public Callback<YsBulletinVO> getBulletinDetail(Integer id) {
        return ysBulletinService.getBulletinDetail(id);
    }

    @ApiOperation(value = "公告 - 审核")
    @ApiOperationSort(value = 7)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告id"),
            @ApiImplicitParam(name = "status", value = "状态   0:待审核 1:审核通过 2:审核不通过 - 驳回"),
    })
    @GetMapping("/checkBulletin")
    public Callback checkBulletin(Integer id, Integer status) {
        return ysBulletinService.checkBulletin(id, status);
    }


    @ApiOperation(value = "消息 - 列表")
    @ApiOperationSort(value = 8)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "status", value = "状态 false未读, true已读"),
            @ApiImplicitParam(name = "likeTitle", value = "公告标题")
    })
    @GetMapping("/getMessageList")
    public Callback<BaseVO<YsMessageVO>> getMessageList(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        Boolean status, String likeTitle) {
        return ysBulletinService.getMessageList(page, pageSize, status, likeTitle);
    }

    @ApiOperation(value = "消息 - 公告ID，修改为已读状态")
    @ApiOperationSort(value = 9)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bulletinId", value = "公告ID")
    })
    @GetMapping("/updateMessageBulletin")
    public Callback updateMessageBulletin(Integer bulletinId) {
        return ysBulletinService.updateMessageBulletin(bulletinId);
    }

    @ApiOperation(value = "消息 - 消息ID，修改为已读状态")
    @ApiOperationSort(value = 10)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息ID")
    })
    @GetMapping("/updateMessage")
    public Callback updateMessage(Integer id) {
        return ysBulletinService.updateMessage(id);
    }

    @ApiOperation(value = "消息 - 获取未读总条数")
    @ApiOperationSort(value = 11)
    @GetMapping("/unreadMessageCount")
    public Callback unreadMessageCount(){
        return ysBulletinService.unreadMessageCount();
    }

    @ApiOperation(value = "公告不分页 - 列表")
    @ApiOperationSort(value = 12)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态   0:待审核 1:审核通过 2:审核不通过 - 驳回"),
            @ApiImplicitParam(name = "likeTitle", value = "公告名称或ID")
    })
    @GetMapping("/getAllBulletinList")
    public Callback<List<YsBulletinVO>> getAllBulletinList(Integer status) {
        return ysBulletinService.getAllBulletinList(status);
    }
}
