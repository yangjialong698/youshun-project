package com.ennova.pubinfouser.dao;
import com.ennova.pubinfouser.entity.PermissionEntity;

import com.ennova.pubinfouser.entity.RolePermissionEntity;
import com.ennova.pubinfouser.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionDao {

    List<PermissionVO> getPermissions();

    List<RolePermissionEntity> getRolePermissions(int roleId);

    List<PermissionVO> getSelectPermissions();

    int delRolePermission(Integer roleId);

    int addRolePermission(@Param("roleId") int roleId, @Param("list") List<Integer> list);

    List<PermissionEntity> queryAllBySystemNo(@Param("systemNo")String systemNo);


}
