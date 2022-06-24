package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.dto.UserDTO;
import com.ennova.pubinfotask.vo.CurrentUserVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/29
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface UserMapper {

    CurrentUserVO selectCurrentUser(@Param("userId") Integer userId);

    // 查用户是否在于表中
    Integer selectByHaveUserId(@Param("userId") Integer userId);

    // 查询所有用户
    List<CurrentUserVO> selectAllUser();

    UserDTO selectById(@Param("id") Integer id);
}
