package com.ennova.pubinfodaily.dao;

import com.ennova.pubinfodaily.entity.YsDailyFeedBack;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YsDailyFeedBackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsDailyFeedBack record);

    int insertOrUpdate(YsDailyFeedBack record);

    int insertOrUpdateSelective(YsDailyFeedBack record);

    int insertSelective(YsDailyFeedBack record);

    YsDailyFeedBack selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsDailyFeedBack record);

    int updateByPrimaryKey(YsDailyFeedBack record);

    int updateBatch(List<YsDailyFeedBack> list);

    int updateBatchSelective(List<YsDailyFeedBack> list);

    int batchInsert(@Param("list") List<YsDailyFeedBack> list);
}