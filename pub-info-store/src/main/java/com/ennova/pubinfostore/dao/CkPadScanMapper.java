package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.CkPdaScan;
import com.ennova.pubinfostore.vo.CkPdaScanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CkPadScanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CkPdaScan record);

    int insertSelective(CkPdaScan record);

    CkPdaScan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CkPdaScan record);

    int updateByPrimaryKey(CkPdaScan record);

    List<CkPdaScanVO> selectPdaInfo(@Param("barCode") String barCode);
}