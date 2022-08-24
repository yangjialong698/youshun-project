package com.ennova.pubinfodaily.dao;

import com.ennova.pubinfodaily.entity.TUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertOrUpdate(TUser record);

    int insertOrUpdateSelective(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    int batchInsert(@Param("list") List<TUser> list);

    List<TUser> selectByDepartment(@Param("deptIds")List<Long> deptIds);





}