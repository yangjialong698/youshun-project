package com.ennova.pubinfodaily.dao;

import com.ennova.pubinfodaily.entity.TDept;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDept record);

    int insertOrUpdate(TDept record);

    int insertOrUpdateSelective(TDept record);

    int insertSelective(TDept record);

    TDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDept record);

    int updateByPrimaryKey(TDept record);

    int updateBatch(List<TDept> list);

    int updateBatchSelective(List<TDept> list);

    int batchInsert(@Param("list") List<TDept> list);

    List<TDept> selectByCheckId(@Param("checkId")String checkId);

    List<TDept> selectByManageId(@Param("manageId")String manageId);




}