package com.ennova.pubinfoproduct.dao;

import com.ennova.pubinfoproduct.vo.PrdCgBodyVO;
import com.ennova.pubinfoproduct.vo.PrdDetailHeadVO;
import com.ennova.pubinfoproduct.vo.PrdZzBodyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrdDetailMapper {

    PrdDetailHeadVO selectHeadByPrdNo(String prdNo);

    List<PrdZzBodyVO> selectZzBodyByPrdNo(String prdNo);

    List<PrdCgBodyVO> selectCgBodyByPrdNo(String prdNo);
}
