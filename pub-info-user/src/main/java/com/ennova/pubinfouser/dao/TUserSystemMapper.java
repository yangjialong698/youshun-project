package com.ennova.pubinfouser.dao;
import com.ennova.pubinfouser.vo.TUserSysVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.ennova.pubinfouser.entity.TUserSystem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TUserSystemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserSystem record);

    int insertSelective(TUserSystem record);

    TUserSystem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserSystem record);

    int updateByPrimaryKey(TUserSystem record);

    List<TUserSystem> queryByUserId(@Param("userId")Integer userId);

    List<TUserSysVO> selectAll();



}