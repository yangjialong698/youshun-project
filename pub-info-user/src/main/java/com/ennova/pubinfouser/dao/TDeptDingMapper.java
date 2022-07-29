package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.TDeptDing;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TDeptDingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDeptDing record);

    int insertOrUpdate(TDeptDing record);

    int insertOrUpdateSelective(TDeptDing record);

    int insertSelective(TDeptDing record);

    TDeptDing selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDeptDing record);

    int updateByPrimaryKey(TDeptDing record);

    int batchInsert(@Param("list") List<TDeptDing> list);
}