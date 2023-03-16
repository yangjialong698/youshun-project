package com.ennova.pubinfopurchase.controller;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfopurchase.service.CgMaterialSupplyService;
import com.ennova.pubinfopurchase.vo.CgMaterialSupplyVO;
import com.ennova.pubinfopurchase.vo.PrdInfoVO;
import com.ennova.pubinfopurchase.vo.SupplierInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/1
 */
@Slf4j
@Api(tags = "公共信息平台-采购供需")
@RestController
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@RequestMapping("/material")
public class CgMaterialSupplyController {

    private final CgMaterialSupplyService cgMaterialSupplyService;

    @ApiOperation(value = "根据品号查品名")
    @GetMapping("/getPrdName")
    public Callback<List<PrdInfoVO>> getPrdName(String productNo){
        return cgMaterialSupplyService.getPrdName(productNo);
    }

    @ApiOperation(value = "根据供应商品号查询供应商详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supplierNo", value = "供应商品号")
    })
    @GetMapping("/getSupplierInfo")
    public Callback<List<SupplierInfoVO>> getSupplierInfo(String supplierNo) {
        return cgMaterialSupplyService.getSupplierInfo(supplierNo);
    }

    @ApiOperation(value = "采购供需 - 发布和修改采购供需信息")
    @PostMapping("/insertOrUpdate")
    public Callback insertOrUpdate(@RequestBody @Validated CgMaterialSupplyVO cgMaterialSupplyVO) {
        return cgMaterialSupplyService.insertOrUpdate(cgMaterialSupplyVO);
    }

    @ApiOperation(value = "采购供需 - 采购供需信息首页分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "开始页"),
            @ApiImplicitParam(name = "pageSize", value = "显示条数")
    })
    @GetMapping("/selectMaterialSupply")
    public Callback<BaseVO<CgMaterialSupplyVO>> selectMaterialSupply(@RequestParam(defaultValue = "1") Integer page,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return cgMaterialSupplyService.selectMaterialSupply(page, pageSize);
    }

    @ApiOperation(value = "采购供需 - 采购供需信息查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购供需ID", required = true)
    })
    @GetMapping("/getDetail")
    public Callback<CgMaterialSupplyVO> getDetail(Integer id) {
        return cgMaterialSupplyService.getDetail(id);
    }

    @ApiOperation(value = "采购供需 - 删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "采购供需ID", required = true)
    })
    @GetMapping("/delete")
    public Callback delete(Integer id) {
        return cgMaterialSupplyService.delete(id);
    }

}
