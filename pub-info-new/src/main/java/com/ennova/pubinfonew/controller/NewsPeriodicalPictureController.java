package com.ennova.pubinfonew.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.dto.FileDto;
import com.ennova.pubinfonew.service.NewsPeriodicalPictureService;
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
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/16
 */
@Api(tags = "公共信息平台-期刊图片")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/newsPeriodicalPicture")
public class NewsPeriodicalPictureController {

    private final NewsPeriodicalPictureService newsPeriodicalPictureService;

    @ApiOperation(value = "期刊图片 - 选择文件")
    @PostMapping("/selectFile")
    public Callback<FileVO> selectFile(MultipartFile file){
        return newsPeriodicalPictureService.selectFile(file);
    }

    @ApiOperation(value = "期刊图片 - 选择期数")
    @GetMapping("/getPeriodicalNum")
    public Callback<List<Integer>> getPeriodicalNum(){
        return newsPeriodicalPictureService.getPeriodicalNum();
    }

    @ApiOperation(value = "期刊图片 - 选择版数")
    @GetMapping("/getEditionNum")
    public Callback<List<Integer>> getEditionNum(Integer periodicalNum){
        return newsPeriodicalPictureService.getEditionNum(periodicalNum);
    }

    @ApiOperation(value = "期刊图片 - 根据期数和版数判断上下期和上下版")
    @GetMapping("/getPeriodicalAndEditionNum")
    public Callback<NewsVO> getPeriodicalAndEditionNum(Integer periodicalNum, Integer editionNum){
        return newsPeriodicalPictureService.getPeriodicalAndEditionNum(periodicalNum, editionNum);
    }

    @ApiOperation(value = "期刊图片 - 上传文件")
    @PostMapping("/upload")
    public Callback upload(@RequestBody FileDto fileDto){
        return newsPeriodicalPictureService.upload(fileDto);
    }

    @ApiOperation(value = "期刊图片 - PDF下载")
    @GetMapping("/netDownLoadFile")
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        newsPeriodicalPictureService.netDownLoadFile(netAddress, filename, response);
    }

    @ApiOperation(value = "期刊图片 - 文件删除")
    @PostMapping("/deleteFile")
    public Callback deleteFile(@RequestBody FileDelDTO fileDelDTO){
        return newsPeriodicalPictureService.deleteFile(fileDelDTO);
    }

    @ApiOperation(value = "期刊图片 - 分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "PeriodicalNum", value = "报刊期数")
    })
    @GetMapping("/selectPeriodicalPicture")
    public Callback<BaseVO<NewsVO>> selectPeriodicalPicture(@RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize, Integer periodicalNum, Integer editionNum){
        return newsPeriodicalPictureService.selectPeriodicalPicture(page, pageSize, periodicalNum, editionNum);
    }

    @ApiOperation(value = "期刊图片 - 修改按钮判断条件")
    @GetMapping("/selectPeriodicalChangeStatus")
    public Callback<NewsVO> selectPeriodicalChangeStatus(Integer periodicalNum, Integer editionNum){
        return newsPeriodicalPictureService.selectPeriodicalChangeStatus(periodicalNum, editionNum);
    }
}
