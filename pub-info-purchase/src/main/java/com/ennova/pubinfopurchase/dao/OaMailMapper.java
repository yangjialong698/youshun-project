package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.Mail;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OaMailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mail record);

    int insertSelective(Mail record);

    Mail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Mail record);

    int updateByPrimaryKey(Mail record);

    List<Mail> selectAll();

    Mail selectByName(@Param("name")String name);
}