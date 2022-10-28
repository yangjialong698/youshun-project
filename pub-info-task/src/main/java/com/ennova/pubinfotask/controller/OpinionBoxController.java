package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.GwMessage;
import com.ennova.pubinfotask.service.OpinionBoxService;
import com.ennova.pubinfotask.vo.BaseOpinionBoxVO;
import com.ennova.pubinfotask.vo.FileVO;
import com.ennova.pubinfotask.vo.OpinionBoxVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "公共信息平台-意见箱")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/opinionBox")
public class OpinionBoxController {

    private final OpinionBoxService opinionBoxService;

    // 文件上传
     @ApiOperation(value = "文件上传")
     @ApiOperationSort(value = 1)
     @PostMapping("/uploadFile")
     public Callback<FileVO> uploadFile(MultipartFile file){
         return opinionBoxService.uploadFile(file);
     }

     // 文件删除
    @ApiOperation(value = "文件删除")
    @ApiOperationSort(value = 2)
    @GetMapping("/deleteFile")
    public Callback deleteFile(Integer id, String filePath){
        return opinionBoxService.deleteFile(id, filePath);
    }


     // 新增意见箱
    @ApiOperation(value = "新增意见箱")
    @ApiOperationSort(value = 2)
    @PostMapping("/addOpinionBox")
    public Callback addOpinionBox(@RequestBody @Validated BaseOpinionBoxVO baseOpinionBoxVO) {
        return opinionBoxService.addOpinionBox(baseOpinionBoxVO);
    }

    // 删除意见箱
    @ApiOperation(value = "删除意见箱")
    @ApiOperationSort(value = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "意见箱ID")
    })
    @GetMapping("/deleteOpinionBox")
    public Callback deleteOpinionBox(Integer id) {
        return opinionBoxService.deleteOpinionBox(id);
    }

    // 查询意见箱列表
    @ApiOperation(value = "查询意见箱列表")
    @ApiOperationSort(value = 4)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页条数")
    })
    @GetMapping("/opinionBoxList")
    public Callback<BaseVO<OpinionBoxVO>> opinionBoxList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return opinionBoxService.opinionBoxList(page, size);
    }

    // 在线留言列表
    @ApiOperation(value = "在线留言列表")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
            @ApiImplicitParam(name = "content", value = "留言内容")
    })
    @GetMapping("/messageList")
    public Callback<BaseVO<GwMessage>> messageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String content) {
        return opinionBoxService.messageList(page, size,content);
    }


}
