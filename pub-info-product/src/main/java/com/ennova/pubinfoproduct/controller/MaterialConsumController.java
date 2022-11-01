package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.service.MaterialConsumService;
import com.ennova.pubinfoproduct.vo.MaterialConsumVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/materialconsum")
@Slf4j
public class MaterialConsumController {
    @Autowired
    private MaterialConsumService materialConsumService;

    @ApiOperation(value = "物料领用信息", tags = "物料领用信息")
    @GetMapping("/listMaterialConsum")
    public Callback<List<MaterialConsumVO>> listMaterialConsum(String startTime, String endTime, String module,
                                                               String workOrderNo,String consumKind,String consumNo,
                                                               String prdName,String prdNo) {

        return materialConsumService.listMaterialConsum(startTime,endTime,module,workOrderNo,consumKind,consumNo,prdName,prdNo);
    }

}
