package com.ennova.pubinfowebsite.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
@FeignClient(name = "PURCHASE-SERVICE", url = "http://pubinfo.ennova.com.cn", fallback = PurchaseFeginFallback.class)
public interface PurchaseFeginClient {

    @ApiOperation(value = "获取公共信息采购系统 - 采购信息列表")
    @GetMapping("/api//purchase/selectPurchaseInfo")
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(@RequestParam("page") Integer page,
                                                                 @RequestParam("pageSize") Integer pageSize,
                                                                 @RequestParam("name") String name,
                                                                 @RequestParam("type") Integer type);
}
