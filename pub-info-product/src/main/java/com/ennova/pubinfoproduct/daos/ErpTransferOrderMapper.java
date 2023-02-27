package com.ennova.pubinfoproduct.daos;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
@Mapper
public interface ErpTransferOrderMapper {

    List<ErpTransferOrder> selectAllByMoveOutNo(@Param("gxList") List<String> gxList);

    List<ErpTransferOrder> selByOrderDateMoveOutNoAndProductNo( @Param("orderDate")String orderDate,
                                                                @Param("workCenterNo")String workCenterNo,
                                                                @Param("prdNo")String prdNo);
}