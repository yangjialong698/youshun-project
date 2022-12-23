package com.ennova.pubinfoproduct.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfoproduct.entity.ErpException;
import com.ennova.pubinfoproduct.entity.ErpReworkRepair;
import com.ennova.pubinfoproduct.service.ErpReworkRepairService;
import com.ennova.pubinfoproduct.vo.ErpExceptionVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "返工返修信息")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RestController
@RequestMapping("/reworkrepair")
public class ErpReworkRepairController {

    @Autowired
    private ErpReworkRepairService erpReworkRepairService;

    @ApiOperation(value = "新增和修改返工返修信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody ErpReworkRepair erpReworkRepair) {
        return erpReworkRepairService.insertOrUpdate(erpReworkRepair);
    }

    @PostMapping("/deleteOne")
    @ApiOperation(value = "返工返修信息-删除")
    public Callback deleteOne(Integer id) {
        return erpReworkRepairService.deleteOne(id);
    }

    @ApiOperation(value = "根据返工返修ID查看返工返修详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "返工返修ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<ErpReworkRepair> getDetail(Integer id) {
        return erpReworkRepairService.getDeatil(id);
    }

    @ApiOperation(value = "查看返工返修列表")
    @GetMapping("/getReworkRepairList")
    public Callback<BaseVO<ErpReworkRepair>> getReworkRepairList(Integer page, Integer pageSize,String key) {
        return erpReworkRepairService.getReworkRepairList(page,pageSize,key);
    }
}
