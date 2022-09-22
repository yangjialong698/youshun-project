package com.ennova.pubinfonew.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfonew.dto.FileDelDTO;
import com.ennova.pubinfonew.service.NewsPeriodicalHotPictureService;
import com.ennova.pubinfonew.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/22
 */
@Api(tags = "公共信息平台-期刊热区图片")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/newsPeriodicalHotPicture")
public class NewsPeriodicalHotPictureController {

    private final NewsPeriodicalHotPictureService newsPeriodicalPictureService;

    @ApiOperation(value = "期刊热区图片 - 选择文件")
    @PostMapping("/selectHotPicture")
    public Callback<FileVO> selectHotPicture(MultipartFile file){
        return newsPeriodicalPictureService.selectHotPicture(file);
    }

    @ApiOperation(value = "期刊热区图片 - 文件删除")
    @PostMapping("/deleteHotPicture")
    public Callback deleteHotPicture(@RequestBody FileDelDTO fileDelDTO){
        return newsPeriodicalPictureService.deleteHotPicture(fileDelDTO);
    }

}
