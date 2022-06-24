package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface YsTeamMapper {

    List<YsTeam> selectAllByExecutorId(@Param("executorId")Integer executorId);

}