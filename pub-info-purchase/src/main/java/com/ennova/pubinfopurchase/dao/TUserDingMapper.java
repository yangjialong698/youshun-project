package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.TUserDing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TUserDingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserDing record);

    int insertSelective(TUserDing record);

    TUserDing selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserDing record);

    int updateByPrimaryKey(TUserDing record);

    int updateBatch(List<TUserDing> list);

    int batchInsert(@Param("list") List<TUserDing> list);

    TUserDing selectByUserId(@Param("userId") String userId);

    List<TUserDing> selectByDepartment(@Param("department")String department);

    List<TUserDing> selectByUsernameList(@Param("list")List<String> list);




}