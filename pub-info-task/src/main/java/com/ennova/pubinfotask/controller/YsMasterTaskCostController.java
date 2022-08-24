package com.ennova.pubinfotask.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfotask.entity.YsMasterTaskCost;
import com.ennova.pubinfotask.service.YsMasterTaskCostService;
import com.ennova.pubinfotask.vo.BaseVO;
import com.ennova.pubinfotask.vo.CostBaseVO;
import com.ennova.pubinfotask.vo.YsMasterTaskCostVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "公共信息平台-成本")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/masterTaskCost")
public class YsMasterTaskCostController {

    private final YsMasterTaskCostService ysMasterTaskCostService;

    @ApiOperation(value = "主任务 - 查询主任务成本")
    @ApiOperationSort(value = 1)
    @PostMapping("/selectByYsMasterTaskCost")
    public Callback<CostBaseVO<YsMasterTaskCost>> selectByYsMasterTaskCost( @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                                            Integer ysMasterTaskId, String costDate) {
        return ysMasterTaskCostService.selectByYsMasterTaskId(page, pageSize, ysMasterTaskId, costDate);
    }

    @ApiOperation(value = "主任务 - 成本新增")
    @ApiOperationSort(value = 2)
    @PostMapping("/batchCostInsert")
    public Callback batchCostInsert(@RequestBody BaseVO<YsMasterTaskCostVO> baseVO) {
        return ysMasterTaskCostService.batchCostInsert(baseVO);
    }

    @ApiOperation(value = "主任务 - 成本删除")
    @ApiOperationSort(value = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "成本id", dataType = "int", paramType = "query")
    })
    @GetMapping("/deleteByYsMasterTaskId")
    public Callback deleteByPrimaryKey(Integer id) {
        return ysMasterTaskCostService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "主任务 - 更新")
    @ApiOperationSort(value = 4)
    @PostMapping("/updateCostBatch")
    public Callback updateCostBatch(@RequestBody BaseVO<YsMasterTaskCostVO> baseVO) {
        return ysMasterTaskCostService.updateCostBatch(baseVO);
    }





}
