package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import com.ennova.pubinfoproduct.service.ErpScrapLossService;
import com.ennova.pubinfoproduct.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Api(tags = "报废损失计算")
@RestController
@RequestMapping("/erpscraploss")
public class ErpScrapLossController {
    @Autowired
    private ErpScrapLossService erpScrapLossService;

    @ApiOperation(value = "报废损失计算-查询所有的工作中心列表")
    @GetMapping("/getworkcenternolist")
    public Callback<List<String>> getWorkCenterNoList() {
        return Callback.success(erpScrapLossService.getWorkCenterNoList());
    }

    @ApiOperation(value = "报废损失计算-模糊查询品号列表")
    @GetMapping("/getPrdNos")
    public Callback<List<String>> getPrdNos(String key) {
        return Callback.success(erpScrapLossService.getPrdNos(key));
    }

    @ApiOperation(value = "报废损失计算-根据工作中心查询平均小时成本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workCenterNo", value = "工作中心", required = true)
    })
    @GetMapping("/geterpperhourcost")
    public Callback<ErpPerhourCostVO> getErpPerhourCost(String workCenterNo) {
        ErpPerhourCostVO erpPerhourCostVO = erpScrapLossService.getErpPerhourCost(workCenterNo);
        return Callback.success(erpPerhourCostVO);
    }

    @ApiOperation(value = "报废损失计算-根据品号查询品名+单件材料费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdNo", value = "品号", required = true)
    })
    @GetMapping("/getErpPrdByPrdno")
    public Callback<ErpPrdNameVO> getErpPrdByPrdno( @RequestParam("workCenterNo")String workCenterNo,@RequestParam("prdNo")String prdNo) {
        return erpScrapLossService.getErpPrdByPrdno(workCenterNo,prdNo);
    }

    @ApiOperation(value = "报废损失计算 - 新增和修改报废损失")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated ErpScrapLossVO erpScrapLossVO) {
        return erpScrapLossService.insertOrUpdate(erpScrapLossVO);
    }

    @ApiOperation(value = "报废损失计算 - 根据id删除报废损失")
    @DeleteMapping("/delete")
    public Callback delete(Integer id) {
        return erpScrapLossService.delete(id);
    }

    @ApiOperation(value = "报废损失计算 - 报废损失查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报废损失id", required = true)
    })
    @GetMapping("/getDetailById")
    public Callback<ErpScrapLoss> getDetailById(Integer id){
        return erpScrapLossService.getDetailById(id);
    }

    @ApiOperation(value = "报废损失计算 - 查询客述台账信息分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数"),
            @ApiImplicitParam(name = "keyTime", value = "搜索月份"),
            @ApiImplicitParam(name = "workCenterNo", value = "工作中心"),
            @ApiImplicitParam(name = "prdNo", value = "品号"),
    })
    @GetMapping("/selectErpScrapLossList")
    public Callback<BaseVO<ErpScrapLoss>> selectErpScrapLossList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam("keyTime") String keyTime,
            @RequestParam("workCenterNo") String workCenterNo,
            @RequestParam("prdNo") String prdNo) {
        return erpScrapLossService.selectErpScrapLossList(page, pageSize, keyTime, workCenterNo, prdNo);
    }

    @PostMapping("/test")
    @ApiOperation(value = "测试", tags = "测试")
    public void updatTempTdept() {
        erpScrapLossService.calculateScrapInfo();
    }
}
