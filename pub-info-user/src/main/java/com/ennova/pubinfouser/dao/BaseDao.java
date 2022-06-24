package com.ennova.pubinfouser.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseDao<T> {

    int insert(T t);

    int update(T t);

    int delete(int id);

    int status(@Param("id") int id, @Param("status") int status);

}
