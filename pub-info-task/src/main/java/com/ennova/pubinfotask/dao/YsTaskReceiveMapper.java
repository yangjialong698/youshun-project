package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsTaskReceive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsTaskReceiveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsTaskReceive record);

    int insertSelective(YsTaskReceive record);

    YsTaskReceive selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsTaskReceive record);

    int updateByPrimaryKey(YsTaskReceive record);

    int updateBatch(List<YsTaskReceive> list);

    int batchInsert(@Param("list") List<YsTaskReceive> list);

    int updateTaskReceiveByMastId(@Param("receiveId") Integer receiveId, @Param("masterTaskId") Integer masterTaskId);

    List<YsTaskReceive> selectByYsMasterTaskId(@Param("ysMasterTaskId")Integer ysMasterTaskId);


}