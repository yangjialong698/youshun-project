package com.ennova.pubinfoproduct.dao;

import com.ennova.pubinfoproduct.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrdDetailMapper {

    PrdDetailHeadVO selectHeadByPrdNo(String prdNo);

    List<PrdCgBodyVO> selectCgBodyByPrdNo(String prdNo);

    List<PrdZzBodyVO> selectZzBodyByPrdNo(String prdNo);

    List<PrdInfoVO> selectPrdInfo(String prdNo);

    List<StockDetailVO> selectStockInfo(String prdNo);

    List<StockDetailVO> selectFinishPrdInfo(String prdNo);
}
