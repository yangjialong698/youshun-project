package com.ennova.pubinfotask.dao;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.ennova.pubinfotask.entity.YsLoginMonth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YsLoginMonthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsLoginMonth record);

    int insertSelective(YsLoginMonth record);

    YsLoginMonth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsLoginMonth record);

    int updateByPrimaryKey(YsLoginMonth record);

    List<YsLoginMonth> selectAllByUserNameAndLoginDate(@Param("userName")String userName,@Param("loginDate")String loginDate);

    List<YsLoginMonth> selectByCountNumAndLoginDateAndUserIdAndUserName(YsLoginMonth record);

}