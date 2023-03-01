package com.ennova.pubinfoproduct.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.daos.ErpTransferOrderMapper;
import com.ennova.pubinfoproduct.entity.ErpPrdCost;
import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
import com.ennova.pubinfoproduct.service.ErpScrapLossService;
import com.ennova.pubinfoproduct.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "报废损失计算")
@RestController
@RequestMapping("/erpscraploss")
public class ErpScrapLossController {
    @Autowired
    private ErpScrapLossService erpScrapLossService;

    @Autowired
    private ErpTransferOrderMapper erpTransferOrderMapper;

    @ApiOperation(value = "报废损失计算-查询所有的工作中心")
    @GetMapping("/getworkcenternolist")
    public Callback<List<String>> getWorkCenterNoList() {
        return Callback.success(erpScrapLossService.getWorkCenterNoList());
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

    @ApiOperation(value = "报废损失计算-根据品号查询品名材料油辅料")
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

    //计算报废数量+报废金额
    @Scheduled(cron = " 0 0 23 * * ?")
    public void calculateScrapInfo() {
        //定时跑批查询无报废数量+报废金额的列表
        List<ErpScrapLoss> erpScrapLossList = erpScrapLossService.selectHisInfoList();
        if (CollectionUtil.isNotEmpty(erpScrapLossList)) {
            erpScrapLossList.forEach(e -> {
                String orderDate = e.getOrderDate();//单据日期
                String workCenterNo = e.getWorkCenterNo();//工作中心
                String prdNo = e.getPrdNo();//产品品号
                Double hourCost = e.getHourCost();//平均小时成本含社保
                Double prdPerCost = e.getPrdPerCost();//单件材料费
                Double workHours = e.getWorkHours();//工时
                Double toolOil = e.getToolOil();//单件刀具油辅料
                List<ErpTransferOrder> erpTransferOrderList = erpTransferOrderMapper.selByOmpNo(orderDate, workCenterNo, prdNo);
                if (CollectionUtil.isNotEmpty(erpTransferOrderList)) {
                    Integer scrapNumTotal = erpTransferOrderList.stream().mapToInt(ErpTransferOrder::getScrapNum).sum();//总报废数量
                    Double scrapCostTotal = 0.0;
                    for (ErpTransferOrder efo : erpTransferOrderList) {
                        //单件人工 = 平均小时成本含社保*工时/合格数量
                        Double perPerson = hourCost * workHours / efo.getAcceptanceNum();
                        //报废金额 = 报废数量*(单件人工+单件材料费+单件刀具油辅料)
                        Double scrapCost = efo.getScrapNum() * (perPerson + prdPerCost + toolOil);
                        efo.setScrapCost(scrapCost);
                        erpTransferOrderMapper.updateByPrimaryKey(efo);
                        scrapCostTotal += scrapCost;
                    }
                    e.setScrapNum(scrapNumTotal);
                    e.setScrapCost(scrapCostTotal);
                    //更新报废数量+报废金额
                    erpScrapLossService.updateByPrimaryKeySelective(e);
                }
            });
        }
    }
}
