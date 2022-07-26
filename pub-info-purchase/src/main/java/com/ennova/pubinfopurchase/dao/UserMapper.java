package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.dto.UserDTO;
import com.ennova.pubinfopurchase.vo.CurrentUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */
@Mapper
public interface UserMapper {

    CurrentUserVO selectCurrentUser(@Param("userId") Integer userId);

    UserDTO selectById(@Param("id") Integer id);
}
