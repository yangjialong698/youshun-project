package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.LoginLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoginLog record);

    int insertSelective(LoginLog record);

    LoginLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoginLog record);

    int updateByPrimaryKey(LoginLog record);

    int updateBatch(List<LoginLog> list);

    int batchInsert(@Param("list") List<LoginLog> list);

    Integer getTotalVisit();
}