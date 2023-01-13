package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.TUserDing;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
}