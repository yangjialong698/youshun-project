package com.ennova.pubinfofile.dao;

import com.ennova.pubinfofile.entity.YsSonTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface YsSonTaskMapper {

    List<YsSonTask> selectAllByYsTeamIds(@Param("teamIds") List<Integer> teamIds);
}