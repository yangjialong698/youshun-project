package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.service.ErpTransferOrderService;
import com.ennova.pubinfoproduct.vo.ScrapVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/erpinputscrap")
@Slf4j
public class ErpInputScrapController {

    @Autowired
    private ErpTransferOrderService erpTransferOrderService;

    @ApiOperation(value = "近一个月入库量与报废率统计数据", tags = "近一个月入库量与报废率统计数据")
    @GetMapping("/erpInputScrap")
    public Callback<List<ScrapVO>> erpinputscrap(String moveOutNo) {

        return erpTransferOrderService.erpinputscrapNew(moveOutNo);
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试跑每个工作中心报废数据", tags = "测试")
    public void calMonthErpScrapInfo(String outNo) {
        erpTransferOrderService.calMonthErpScrapInfo(outNo);
    }
}
