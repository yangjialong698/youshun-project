package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsDivertTask;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsDivertTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsDivertTask record);

    int insertSelective(YsDivertTask record);

    YsDivertTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsDivertTask record);

    int updateByPrimaryKey(YsDivertTask record);

    int updateBatch(List<YsDivertTask> list);

    int batchInsert(@Param("list") List<YsDivertTask> list);
}