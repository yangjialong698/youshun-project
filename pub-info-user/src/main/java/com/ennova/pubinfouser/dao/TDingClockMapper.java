package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.TDingClock;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TDingClockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDingClock record);

    int insertSelective(TDingClock record);

    TDingClock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDingClock record);

    int updateByPrimaryKey(TDingClock record);

    int updateBatch(List<TDingClock> list);

    int batchInsert(@Param("list") List<TDingClock> list);

    List<TDingClock> selectByUserIdsAndDayTime(@Param("userIdList")List<String> userIdList,
                                               @Param("checkDateFrom")String checkDateFrom,
                                               @Param("checkDateTo")String checkDateTo);
}