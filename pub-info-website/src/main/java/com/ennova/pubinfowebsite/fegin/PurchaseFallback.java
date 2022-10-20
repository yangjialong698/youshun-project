package com.ennova.pubinfowebsite.fegin;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfowebsite.vo.CgPurchaseInfoVO;

public class PurchaseFallback implements PurchaseClient {

    @Override
    public Callback<BaseVO<CgPurchaseInfoVO>> selectPurchaseInfo(Integer page, Integer pageSize, String name, Integer type) {
        return Callback.error("获取公共信息采购系统列表 - 失败");
    }
}
