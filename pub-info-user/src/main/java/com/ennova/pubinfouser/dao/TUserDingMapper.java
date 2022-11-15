package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.TUserDing;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TUserDingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserDing record);

    int insertOrUpdate(TUserDing record);

    int insertOrUpdateSelective(TUserDing record);

    int insertSelective(TUserDing record);

    TUserDing selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserDing record);

    int updateByPrimaryKey(TUserDing record);

    int batchInsert(@Param("list") List<TUserDing> list);

    void deleteAll();

    List<TUserDing> selectEntry();

    List<String> selectAllUserId();



}