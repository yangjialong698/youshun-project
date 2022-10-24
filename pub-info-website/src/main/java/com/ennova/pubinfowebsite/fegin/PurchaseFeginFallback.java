package com.ennova.pubinfowebsite.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
public class PurchaseFeginFallback implements PurchaseFeginClient{

    @Override
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(@RequestParam("page") Integer page,
                                                                 @RequestParam("pageSize") Integer pageSize,
                                                                 @RequestParam("name") String name,
                                                                 @RequestParam("type") Integer type){
        return Callback.error("获取公共信息采购系统列表 - 失败");
    }
}
