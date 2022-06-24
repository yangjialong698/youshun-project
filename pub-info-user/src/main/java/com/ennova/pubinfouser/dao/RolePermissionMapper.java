package com.ennova.pubinfouser.dao;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface RolePermissionMapper {
//    int deleteByPrimaryKey(Integer id);
//
    int deleteByRoleId(Integer roleId);
//
//    int insert(RolePermissionEntity record);
//
//    int insertSelective(RolePermissionEntity record);
//
//    RolePermissionEntity selectByPrimaryKey(Integer id);
//
//    List<RolePermissionEntity> selectByRoleId(Integer roleId);
//
//    List<RolePermissionEntity> selectByRoleIdAndPlatform(@Param("roleId") Integer roleId, @Param("platform") Integer platform);
//
//    int updateByPrimaryKeySelective(RolePermissionEntity record);
//
//    int updateByPrimaryKey(RolePermissionEntity record);
}