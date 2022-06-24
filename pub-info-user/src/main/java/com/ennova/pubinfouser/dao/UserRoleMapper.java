package com.ennova.pubinfouser.dao;


import com.ennova.pubinfouser.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserRoleMapper {
//    int deleteByPrimaryKey(Integer id);
//
    int deleteByUserId(Integer userId);
//
//    int insert(UserRole record);
//
    int insertSelective(UserRole record);
//
//    UserRole selectByPrimaryKey(Integer id);
//
    List<UserRole> selectByRoleId(Integer roleId);

//
    List<UserRole> selectByUserId(Integer userId);

//
    int updateByPrimaryKeySelective(UserRole record);


// id);
//    int updateByPrimaryKey(UserRole record);
}