package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.entity.ErpInputScrap;
import com.ennova.pubinfoproduct.entity.ErpQualifiedRate;
import com.ennova.pubinfoproduct.service.ErpQualifiedRateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/erpqualifiedrate")
@Slf4j
public class ErpQualifiedRateController {

    @Autowired
    private ErpQualifiedRateService erpQualifiedRateService;

    @ApiOperation(value = "合格率报表", tags = "合格率报表")
    @GetMapping("/erpQualifiedRate")
    public Callback<List<ErpQualifiedRate>> erpQualifiedRate() {

        return erpQualifiedRateService.erpQualifiedRate();
    }
}
