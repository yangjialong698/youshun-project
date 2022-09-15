package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfotask.entity.YsLoginMonth;
import com.ennova.pubinfotask.service.ReportService;
import com.ennova.pubinfotask.vo.CostDetailVO;
import com.ennova.pubinfotask.vo.SortMasterRateVO;
import com.ennova.pubinfotask.vo.TdailySubmitCountVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公共信息平台-报表")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "报表 - 1 - 任务进度排名")
    @ApiOperationSort(value = 1)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "name", value = "主任务名称")
    })
    @GetMapping("/selectTaskSortList")
    public Callback<BaseVO<SortMasterRateVO>> selectTaskSortList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                                 String name){
        return reportService.selectTaskSortList(page, pageSize, name);
    }

    @ApiOperation(value = "报表 - 2 - 查上个月登录次数排名，前10条")
    @ApiOperationSort(value = 2)
    @GetMapping("/selectLastMothList")
    public Callback selectLastMothList(){
        return reportService.selectLastMothList();
    }

    @ApiOperation(value = "报表 - 2.1 - 所有用户登录次数排名明细 -（也可用于 2 首页登陆次数排名接口）")
    @ApiOperationSort(value = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "userName", value = "姓名"),
            @ApiImplicitParam(name = "loginDate", value = "登陆日期")
    })
    @GetMapping("/selectSortDetailList")
    public Callback<BaseVO<YsLoginMonth>> selectSortDetailList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                               String userName, String loginDate){
        return reportService.selectSortDetailList(page, pageSize, userName, loginDate);
    }

    @ApiOperation(value = "报表 - 3 - 成本")
    @ApiOperationSort(value = 4)
    @GetMapping("/selectCostList")
    public Callback selectCostList(){
        return reportService.selectCostList();
    }

    @ApiOperation(value = "报表 - 3.1 - 成本明细")
    @ApiOperationSort(value = 5)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "name", value = "主任务名")
    })
    @GetMapping("/selectCostDetailList")
    public Callback<BaseVO<CostDetailVO>> selectCostDetailList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                               String name){
        return reportService.selectCostDetailList(page, pageSize, name);
    }

    @ApiOperation(value = "报表 - 4 - 个人日报提交统计")
    @ApiOperationSort(value = 6)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "dayType", value = "0 ： 查询所有， 1 ： 查询 10 天以内，并排除当天  2 ： 查询 20 天以内，并排除当天  3 ： 查询 30 天以内，并排除当天"),
            @ApiImplicitParam(name = "userName", value = "姓名")
    })
    @GetMapping("/selectTdailySubmitCountList")
    public Callback<BaseVO<TdailySubmitCountVO>> selectTdailySubmitCountList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                                             Integer dayType, String userName){
        return reportService.selectTdailySubmitCountList(page, pageSize, dayType, userName);
    }


}
