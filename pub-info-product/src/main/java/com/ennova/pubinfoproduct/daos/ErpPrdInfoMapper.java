package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpPrdInfo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ErpPrdInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpPrdInfo record);

    int insertSelective(ErpPrdInfo record);

    ErpPrdInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpPrdInfo record);

    int updateByPrimaryKey(ErpPrdInfo record);

    int updateBatch(List<ErpPrdInfo> list);

    int batchInsert(@Param("list") List<ErpPrdInfo> list);

    List<ErpPrdInfo> selectByPrdNo(@Param("prdNo")String prdNo);
}