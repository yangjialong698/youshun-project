package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao extends BaseDao<RoleEntity> {

    List<RoleEntity> getRolesByName(@Param("roleName")String roleName);

    RoleEntity selectByRoleCode(@Param("roleCode")String roleCode);

    int insertSelective(RoleEntity record);

    RoleEntity getRoleById(@Param("id")Integer id);

    int updateByPrimaryKeySelective(RoleEntity record);

    List<RoleEntity> listRoles(@Param("company")Integer company, @Param("searchKey")String searchKey);

    List<RoleEntity> listRoleList(@Param("company")Integer company);
}
