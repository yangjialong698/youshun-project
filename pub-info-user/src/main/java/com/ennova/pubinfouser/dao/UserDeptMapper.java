package com.ennova.pubinfouser.dao;



import com.ennova.pubinfouser.entity.UserDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDeptMapper {

    int insertSelective(UserDept userDept);

    List<UserDept> selectByUserId(Integer id);

    int updateByPrimaryKeySelective(UserDept userDept);

    List<UserDept> selectByDeptId(Integer deptId);

    int deleteByUserId(Integer id);
}