package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsSuject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsSujectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsSuject record);

    int insertSelective(YsSuject record);

    YsSuject selectByPrimaryKey(Integer id);

    YsSuject selectById(@Param("id")Integer id);

    List<YsSuject> selectBySuject(@Param("suject")String suject);

    List<YsSuject> selectBySujectList(@Param("suject")String suject);

    List<YsSuject> selectExist(@Param("suject") String suject, @Param("id") Integer id);

    int updateByPrimaryKeySelective(YsSuject record);

    int updateByPrimaryKey(YsSuject record);
}