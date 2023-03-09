package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.WorkTimeRemind;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkTimeRemindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkTimeRemind record);

    int insertOrUpdate(WorkTimeRemind record);

    int insertOrUpdateSelective(WorkTimeRemind record);

    int insertSelective(WorkTimeRemind record);

    WorkTimeRemind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkTimeRemind record);

    int updateByPrimaryKey(WorkTimeRemind record);

    int batchInsert(@Param("list") List<WorkTimeRemind> list);
}