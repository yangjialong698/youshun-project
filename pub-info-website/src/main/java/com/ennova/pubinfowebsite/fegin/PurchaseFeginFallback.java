package com.ennova.pubinfowebsite.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfowebsite.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/9/20
 */
public class PurchaseFeginFallback implements PurchaseFeginClient{
    @Override
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(String name) {
        return Callback.error("获取公共信息采购系统列表 - 失败");
    }
}
