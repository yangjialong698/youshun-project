package com.ennova.pubinfostore.dao;
import org.apache.ibatis.annotations.Param;

import com.ennova.pubinfostore.entity.PreAssemble;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PreAssembleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PreAssemble record);

    PreAssemble selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PreAssemble record);

    int deleteByYsScheduleAssembleId(@Param("ysScheduleAssembleId")Integer ysScheduleAssembleId);


}