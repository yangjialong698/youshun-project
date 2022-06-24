package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.dto.YsWorkTimeDTO;
import com.ennova.pubinfotask.entity.YsWorkTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/5
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsWorkTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsWorkTime record);

    int insertSelective(YsWorkTime record);

    YsWorkTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsWorkTime record);

    int updateByPrimaryKey(YsWorkTime record);

    int updateBatch(List<YsWorkTime> list);

    int batchInsert(@Param("list") List<YsWorkTime> list);

    List<YsWorkTime> selectAllByYsSonTaskId(@Param("ysSonTaskId")Integer ysSonTaskId);

    int batchDeleteByPrimaryKey(@Param("list") List<Integer> ids);

    YsWorkTime selectOneByYsSonTaskId(@Param("ysSonTaskId")Integer ysSonTaskId);

    List<YsWorkTimeDTO> selectAllByYsSonTaskIdInAndExecutorIdIn(@Param("ysSonTaskIdCollection")Collection<Integer> ysSonTaskIdCollection, @Param("executorIdCollection")Collection<Integer> executorIdCollection);


}