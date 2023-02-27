package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpScrapLoss;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ErpScrapLossMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpScrapLoss record);

    int insertOrUpdate(ErpScrapLoss record);

    int insertOrUpdateSelective(ErpScrapLoss record);

    int insertSelective(ErpScrapLoss record);

    ErpScrapLoss selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpScrapLoss record);

    int updateByPrimaryKey(ErpScrapLoss record);

    int batchInsert(@Param("list") List<ErpScrapLoss> list);

    int updateDelFlag(Integer id);

    List<ErpScrapLoss> selectErpScrapLossList(@Param("keyTime") String keyTime,
                                              @Param("workCenterNo") String workCenterNo,
                                              @Param("prdNo") String prdNo);

    List<ErpScrapLoss> selectNullInfo();

    ErpScrapLoss selByOmpNo(@Param("orderDate")String orderDate,
                                                     @Param("workCenterNo")String workCenterNo,
                                                     @Param("prdNo")String prdNo);
}