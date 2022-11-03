package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfoproduct.entity.ErpQualifiedRate;
import com.ennova.pubinfoproduct.daos.ErpQualifiedRateMapper;

import java.util.List;

@Service
public class ErpQualifiedRateService{

    @Resource
    private ErpQualifiedRateMapper erpQualifiedRateMapper;


    public Callback<List<ErpQualifiedRate>> erpQualifiedRate(String moduleNo) {
        List<ErpQualifiedRate> erpQualifiedRates = erpQualifiedRateMapper.selectAllByModuleNo(moduleNo);
        return Callback.success(erpQualifiedRates);
    }
}
