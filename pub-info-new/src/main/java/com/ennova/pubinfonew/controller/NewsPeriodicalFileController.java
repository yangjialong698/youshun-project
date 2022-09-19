package com.ennova.pubinfonew.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.dto.FileDto;
import com.ennova.pubinfonew.service.NewsPeriodicalFileService;
import com.ennova.pubinfonew.vo.FileVO;
import com.ennova.pubinfonew.vo.NewsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/11
 */
@Api(tags = "公共信息平台-期刊文件")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/newsPeriodicalFile")
public class NewsPeriodicalFileController {

    private final NewsPeriodicalFileService newsPeriodicalFileService;

    @ApiOperation(value = "期刊文件 - 选择文件")
    @PostMapping("/selectFile")
    public Callback<FileVO> selectFile(MultipartFile file){
        return newsPeriodicalFileService.selectFile(file);
    }

    @ApiOperation(value = "期刊文件 - 上传文件")
    @PostMapping("/upload")
    public Callback upload(@RequestBody FileDto fileDto){
        return newsPeriodicalFileService.upload(fileDto);
    }

    @ApiOperation(value = "期刊文件 - PDF下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        newsPeriodicalFileService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "期刊文件 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return newsPeriodicalFileService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "期刊文件 - 分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "PeriodicalNum", value = "报刊期数")
    })
    @GetMapping("/selectPeriodicalFile")
    public Callback<BaseVO<NewsVO>> selectPeriodicalFile(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer pageSize, Integer periodicalNum, Integer editionNum){
        return newsPeriodicalFileService.selectPeriodicalFile(page, pageSize, periodicalNum, editionNum);
    }

}
