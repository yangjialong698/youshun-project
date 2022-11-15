package com.ennova.pubinfouser.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ennova.pubinfouser.entity.TDingClock;
import com.ennova.pubinfouser.dao.TDingClockMapper;
@Service
public class TDingClockService{

    @Resource
    private TDingClockMapper tDingClockMapper;

    
    public int deleteByPrimaryKey(Integer id) {
        return tDingClockMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(TDingClock record) {
        return tDingClockMapper.insert(record);
    }

    
    public int insertSelective(TDingClock record) {
        return tDingClockMapper.insertSelective(record);
    }

    
    public TDingClock selectByPrimaryKey(Integer id) {
        return tDingClockMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(TDingClock record) {
        return tDingClockMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(TDingClock record) {
        return tDingClockMapper.updateByPrimaryKey(record);
    }

}
