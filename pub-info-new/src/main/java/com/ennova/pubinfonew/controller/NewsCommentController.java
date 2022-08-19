package com.ennova.pubinfonew.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfonew.dto.NewsCommentDto;
import com.ennova.pubinfonew.service.NewsCommentService;
import com.ennova.pubinfonew.vo.NewsCommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/10
 */
@Api(tags = "公共信息平台-新闻评论")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/newsComment")
public class NewsCommentController {

    private final NewsCommentService newsCommentService;

    @ApiOperation(value = "新闻评论 - 发布评论")
    @PostMapping("/publishComment")
    public Callback publishComment(@RequestBody @Validated NewsCommentDto newsCommentDto){
        return newsCommentService.publishComment(newsCommentDto);
    }

    @ApiOperation(value = "新闻评论 - 置顶")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论id")
    })
    @GetMapping("/stickComment")
    public Callback stickComment(Integer id) {
        return newsCommentService.stickComment(id);
    }

    @ApiOperation(value = "新闻评论 - 取消置顶")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论id")
    })
    @GetMapping("/noStickComment")
    public Callback noStickComment(Integer id) {
        return newsCommentService.noStickComment(id);
    }

    @ApiOperation(value = "新闻评论 - 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "editionTitle", value = "标题名称")
    })
    @GetMapping("/getCommentList")
    public Callback<BaseVO<NewsCommentVO>> getCommentList(@RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          String editionTitle) {
        return newsCommentService.getCommentList(page, pageSize, editionTitle);
    }

    @ApiOperation(value = "新闻评论 - 删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论id")
    })
    @GetMapping("/deleteComment")
    public Callback deleteComment(Integer id) {
        return newsCommentService.deleteComment(id);
    }
}
