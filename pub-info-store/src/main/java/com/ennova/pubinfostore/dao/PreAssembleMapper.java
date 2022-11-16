package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.PreAssemble;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface PreAssembleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PreAssemble record);

    int insertSelective(PreAssemble record);

    PreAssemble selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreAssemble record);

    int updateByPrimaryKey(PreAssemble record);

    int deleteByYsScheduleAssembleId(@Param("ysScheduleAssembleId") Integer ysScheduleAssembleId);

    List<PreAssemble> selectByYsScheduleAssembleId(@Param("ysScheduleAssembleId") Integer ysScheduleAssembleId);
}