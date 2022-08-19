package com.ennova.pubinfonew.dao;


import com.ennova.pubinfonew.vo.CurrentUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    CurrentUserVO selectCurrentUser(@Param("userId") Integer userId);

}
