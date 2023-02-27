package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpPrdCost;
import java.util.List;

import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import com.ennova.pubinfoproduct.vo.ErpPerhourCostVO;
import com.ennova.pubinfoproduct.vo.ErpPrdNameVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ErpPrdCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpPrdCost record);

    int insertOrUpdate(ErpPrdCost record);

    int insertOrUpdateSelective(ErpPrdCost record);

    int insertSelective(ErpPrdCost record);

    ErpPrdCost selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpPrdCost record);

    int updateByPrimaryKey(ErpPrdCost record);

    int batchInsert(@Param("list") List<ErpPrdCost> list);

    List<String> getWorkCenterNoList();

    ErpPerhourCostVO getErpPerhourCost(@Param("workCenterNo")String workCenterNo);

    ErpPrdNameVO selectErpPrdNameVoByPrdno(@Param("prdNo")String prdNo);

}