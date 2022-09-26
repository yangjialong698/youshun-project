package com.ennova.pubinfowebsite.dao;

import com.ennova.pubinfowebsite.entity.GwMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GwMessageMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(GwMessage record);

    int insertSelective(GwMessage record);

    GwMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GwMessage record);

    int updateByPrimaryKey(GwMessage record);

}