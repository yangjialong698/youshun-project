package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.entity.ErpInputScrap;
import com.ennova.pubinfoproduct.service.ErpInputScrapService;
import com.ennova.pubinfoproduct.vo.MaterialConsumVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/erpinputscrap")
@Slf4j
public class ErpInputScrapController {

    @Autowired
    private ErpInputScrapService erpInputScrapService;

    @ApiOperation(value = "入库报废曲线图", tags = "入库报废曲线图")
    @GetMapping("/erpInputScrap")
    public Callback<List<ErpInputScrap>> erpinputscrap() {

        return erpInputScrapService.erpinputscrap();
    }
}
