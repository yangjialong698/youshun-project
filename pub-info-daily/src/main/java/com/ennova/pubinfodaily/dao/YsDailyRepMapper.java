package com.ennova.pubinfodaily.dao;

import com.ennova.pubinfodaily.entity.YsDailyRep;
import java.util.List;

import com.ennova.pubinfodaily.vo.DailyRepDetailVO;
import com.ennova.pubinfodaily.vo.FileDownVO;
import com.ennova.pubinfodaily.vo.YsDailyRepVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YsDailyRepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsDailyRep record);

    int insertOrUpdate(YsDailyRep record);

    int insertOrUpdateSelective(YsDailyRep record);

    int insertSelective(YsDailyRep record);

    YsDailyRep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsDailyRep record);

    int updateByPrimaryKey(YsDailyRep record);

    int updateBatch(List<YsDailyRep> list);

    int updateBatchSelective(List<YsDailyRep> list);

    int batchInsert(@Param("list") List<YsDailyRep> list);

    int delete(@Param("id")Integer id);

    YsDailyRepVO selectDetailOne(@Param("id")Integer id);

    List<FileDownVO> fileDetail(Integer id);

    List<DailyRepDetailVO> getDayRepAll(@Param("fileName")String fileName,
                                        @Param("userId")Integer userId,
                                        @Param("startTime")String startTime,
                                        @Param("endTime")String endTime);

}