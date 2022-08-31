package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.UserEntity;
import com.ennova.pubinfouser.vo.PerDeptNumVO;
import com.ennova.pubinfouser.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao extends BaseDao<UserEntity> {

    UserVO getUserInfoByMobile(@Param("mobile") String mobile);

//    int insertSelective(UserEntity userEntity);
    int insert(UserEntity userEntity);

    UserVO getUserById(@Param("id")Integer id);

    List<UserVO> listUsers(@Param("company")Integer company,@Param("roleId")Integer roleId,@Param("department")Integer department,  @Param("searchKey")String searchKey);

    List<UserVO> listManagerUsers();

    List<PerDeptNumVO> getCouPerDept();

    UserVO getUserByMobile(String mobile);

    List<UserVO> getUserInfoByUname(String username);

    UserVO getUserInfoByJobNum(@Param("jobNum")String jobNum);

    int deleteUser(@Param("jobNum") String jobNum);

    List<UserEntity> selectLeave();

    void updateAllDept();
}
