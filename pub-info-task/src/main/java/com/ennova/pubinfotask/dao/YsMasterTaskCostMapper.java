package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsMasterTaskCost;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YsMasterTaskCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsMasterTaskCost record);

    int insertSelective(YsMasterTaskCost record);

    YsMasterTaskCost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsMasterTaskCost record);

    int updateByPrimaryKey(YsMasterTaskCost record);

    int updateBatch(List<YsMasterTaskCost> list);

    int batchInsert(@Param("list") List<YsMasterTaskCost> list);

    List<YsMasterTaskCost> selectByYsMasterTaskIdAndCostDate(@Param("ysMasterTaskId") Integer ysMasterTaskId, @Param("costDate") String costDate);

    List<YsMasterTaskCost> selectByYsMasterTaskId(@Param("ysMasterTaskId") Integer ysMasterTaskId);
}