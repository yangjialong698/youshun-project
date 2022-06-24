package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsFeedBack;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YsFeedBackMapper {

    int deleteByPrimaryKey(Integer id);


    int insert(YsFeedBack record);


    int insertSelective(YsFeedBack record);


    YsFeedBack selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(YsFeedBack record);


    int updateByPrimaryKey(YsFeedBack record);
}