package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.service.ProduceService;
import com.ennova.pubinfoproduct.vo.ProduceDetailVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produce")
@Slf4j
public class ProduceController {
    @Autowired
    private ProduceService produceService;

    @ApiOperation(value = "生产流转信息", tags = "生产流转信息")
    @GetMapping("/listProduce")
    public Callback<List<ProduceDetailVO>> selectProduceList(String startTime, String endTime, String processNo, String workOrderNo) {

        return produceService.selectProduceList(startTime,endTime,processNo,workOrderNo);
    }
}

