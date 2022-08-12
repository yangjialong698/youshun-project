package com.ennova.pubinfodaily.dao;

import com.ennova.pubinfodaily.entity.YsDailyRepFile;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YsDailyRepFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsDailyRepFile record);

    int insertOrUpdate(YsDailyRepFile record);

    int insertOrUpdateSelective(YsDailyRepFile record);

    int insertSelective(YsDailyRepFile record);

    YsDailyRepFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsDailyRepFile record);

    int updateByPrimaryKey(YsDailyRepFile record);

    int updateBatch(List<YsDailyRepFile> list);

    int updateBatchSelective(List<YsDailyRepFile> list);

    int batchInsert(@Param("list") List<YsDailyRepFile> list);

    int deleteYsDailyRepFile(@Param("id")Integer id);

    List<YsDailyRepFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer id);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    int selectByDailyRepId(@Param("dailyRepId")Integer dailyRepId);
}