package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {

    List<SortMasterRateVO> selectSortList(@Param("name") String name);

    List<VisitVO> selectVisitList();

    List<VisitVO> selectLastMothList();

    List<CostDetailVO> selectCostDetailList(@Param("name") String name);

    List<CostVO> selectCostList();

    List<TdailySubmitCountVO> selecTdailySubmitCount(@Param("dayNum") String dayNum, @Param("userName") String userName);
}
