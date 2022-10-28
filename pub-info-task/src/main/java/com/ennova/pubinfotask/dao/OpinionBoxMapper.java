package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.OpinionBox;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpinionBoxMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpinionBox record);

    int insertSelective(OpinionBox record);

    OpinionBox selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpinionBox record);

    int updateByPrimaryKey(OpinionBox record);

    List<OpinionBox> selectList();




}