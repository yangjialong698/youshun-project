package com.ennova.pubinfoproduct.service;

import com.ennova.pubinfocommon.entity.Callback;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfoproduct.entity.ErpInputScrap;
import com.ennova.pubinfoproduct.daos.ErpInputScrapMapper;

import java.util.List;

@Service
public class ErpInputScrapService{

    @Resource
    private ErpInputScrapMapper erpInputScrapMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return erpInputScrapMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(ErpInputScrap record) {
        return erpInputScrapMapper.insert(record);
    }

    
    public int insertSelective(ErpInputScrap record) {
        return erpInputScrapMapper.insertSelective(record);
    }

    
    public ErpInputScrap selectByPrimaryKey(Integer id) {
        return erpInputScrapMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(ErpInputScrap record) {
        return erpInputScrapMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(ErpInputScrap record) {
        return erpInputScrapMapper.updateByPrimaryKey(record);
    }

    public Callback<List<ErpInputScrap>> erpinputscrap() {
        List<ErpInputScrap> erpInputScraps = erpInputScrapMapper.selectAll();
        return Callback.success(erpInputScraps);
    }
}
