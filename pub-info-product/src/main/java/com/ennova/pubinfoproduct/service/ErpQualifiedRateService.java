package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfoproduct.vo.ErpQualifiedRateVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfoproduct.entity.ErpQualifiedRate;
import com.ennova.pubinfoproduct.daos.ErpQualifiedRateMapper;

import java.util.List;

@Service
public class ErpQualifiedRateService {

    @Resource
    private ErpQualifiedRateMapper erpQualifiedRateMapper;


    public Callback<List<ErpQualifiedRateVO>> erpQualifiedRate(String moduleNo) {
        List<ErpQualifiedRateVO> erpQualifiedRates = erpQualifiedRateMapper.selectAllByModuleNo(moduleNo);
        return Callback.success(erpQualifiedRates);
    }

}

