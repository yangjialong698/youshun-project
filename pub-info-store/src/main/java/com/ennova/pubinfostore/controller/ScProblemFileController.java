package com.ennova.pubinfostore.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfostore.dto.FileDelDTO;
import com.ennova.pubinfostore.service.ScProblemFileService;
import com.ennova.pubinfostore.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "公共信息平台App-问题反馈文件")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/scProblemFile")
public class ScProblemFileController {

    private final ScProblemFileService scProblemFileService;

    // 文件上传
    @ApiOperation(value = "问题反馈选择文件-图片")
    @ApiOperationSort(value = 1)
    @PostMapping("/selectImageFile")
    public Callback<FileVO> selectImageFile(MultipartFile file){
        return scProblemFileService.selectImageFile(file);
    }

    // 文件上传
    /*@ApiOperation(value = "问题反馈选择文件-视频")
    @ApiOperationSort(value = 2)
    @PostMapping("/selectVideoFile")
    public Callback<FileVO> selectVideoFile(MultipartFile file){
        return scProblemFileService.selectVideoFile(file);
    }*/

    @ApiOperation(value = "问题反馈文件 - 附件下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        scProblemFileService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "问题反馈文件 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return scProblemFileService.deleteFile(fileDelDTO);
    }
}
