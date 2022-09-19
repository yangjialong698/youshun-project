package com.ennova.pubinfonew.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.service.NewsPeriodicalService;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻期刊表(NewPeriodical)
 *
 * @author：yangjialong
 * @date：2022/08/09
 */
@Api(tags = "公共信息平台-新闻期刊")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/newsPeriodical")
public class NewsPeriodicalController {

    private final NewsPeriodicalService newsPeriodicalService;

    @ApiOperation(value = "新闻期刊 - 发布和修改新闻期刊")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated NewsPeriodicalVO newsPeriodicalVO) {
        return newsPeriodicalService.insertOrUpdate(newsPeriodicalVO);
    }

    @ApiOperation(value = "新闻期刊 - 分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "periodicalNum", value = "期数（第几期）", dataType = "Integer"),
            @ApiImplicitParam(name = "editionNum", value = "版数（第几版）", dataType = "Integer"),
            @ApiImplicitParam(name = "editionTitle", value = "标题名称")
    })
    @GetMapping("/selectAllNewsPeriodical")
    public Callback<BaseVO<NewsPeriodicalVO>> selectAllPurchaseInfo(@RequestParam(defaultValue = "1") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    Integer periodicalNum, Integer editionNum, String editionTitle) {
        return newsPeriodicalService.selectAllNewPeriodical(page, pageSize, periodicalNum, editionNum, editionTitle);
    }

    @ApiOperation(value = "新闻期刊 - 删除")
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return newsPeriodicalService.delete(id);
    }

    @ApiOperation(value = "新闻期刊 - 期刊信息查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "期刊ID", required = true)
    })
    @GetMapping("/getNewsDetail")
    public Callback<NewsPeriodical> getNewsDetail(Integer id){
        return newsPeriodicalService.getNewsDetail(id);
    }

}
