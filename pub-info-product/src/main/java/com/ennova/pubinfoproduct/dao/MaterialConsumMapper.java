package com.ennova.pubinfoproduct.dao;

import com.ennova.pubinfoproduct.vo.MaterialConsumVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialConsumMapper {
    List<MaterialConsumVO> listMaterialConsumByGd(@Param("startTime")String startTime, @Param("endTime")String endTime,
                                              @Param("module")String module, @Param("workOrderNo")String workOrderNo,
                                              @Param("consumKind")String consumKind, @Param("consumNo")String consumNo,
                                              @Param("prdName")String prdName, @Param("prdNo")String prdNo);

    List<MaterialConsumVO> listMaterialConsumByFy(@Param("startTime")String startTime, @Param("endTime")String endTime,
                                                  @Param("module")String module, @Param("workOrderNo")String workOrderNo,
                                                  @Param("consumKind")String consumKind, @Param("consumNo")String consumNo,
                                                  @Param("prdName")String prdName, @Param("prdNo")String prdNo);
}
