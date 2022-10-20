package com.ennova.pubinfowebsite.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "pub-info-purchase",fallback = PurchaseFallback.class)
public interface PurchaseClient {
    @ApiOperation(value = "获取公共信息采购系统 - 采购信息列表")
    @GetMapping("/purchase/selectPurchaseInfo")
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(@RequestParam("page") Integer page,
                                                                 @RequestParam("pageSize") Integer pageSize,
                                                                 @RequestParam("name") String name,
                                                                 @RequestParam("type") Integer type);
}
