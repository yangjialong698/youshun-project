package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YsMessageMapper {

    YsMessage selectByPrimaryKey(Integer id);


}