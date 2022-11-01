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

    
    public int deleteByPrimaryKey(Integer id) {
        return erpQualifiedRateMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(ErpQualifiedRate record) {
        return erpQualifiedRateMapper.insert(record);
    }

    
    public int insertSelective(ErpQualifiedRate record) {
        return erpQualifiedRateMapper.insertSelective(record);
    }

    
    public ErpQualifiedRate selectByPrimaryKey(Integer id) {
        return erpQualifiedRateMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(ErpQualifiedRate record) {
        return erpQualifiedRateMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(ErpQualifiedRate record) {
        return erpQualifiedRateMapper.updateByPrimaryKey(record);
    }

    public Callback<List<ErpQualifiedRate>> erpQualifiedRate() {
        List<ErpQualifiedRate> erpQualifiedRates = erpQualifiedRateMapper.selectAllByToday();
        return Callback.success(erpQualifiedRates);
    }
}
