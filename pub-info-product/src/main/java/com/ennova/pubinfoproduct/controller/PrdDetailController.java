package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.service.PrdDetailService;
import com.ennova.pubinfoproduct.vo.PrdDetailVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/prddetail")
@Slf4j
public class PrdDetailController {
    @Autowired
    private PrdDetailService prdDetailService;

    @ApiOperation(value = "根据货品号查询详情",tags = "货品API")
    @GetMapping("/selectPrdDetail")
    public Callback<PrdDetailVO> selectPrdDetail(String prdNo){

        return prdDetailService.selectPrdDetail(prdNo);
    }
}
