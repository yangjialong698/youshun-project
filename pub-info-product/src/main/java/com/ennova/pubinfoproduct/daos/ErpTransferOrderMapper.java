package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpTransferOrder;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ErpTransferOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpTransferOrder record);

    int insertOrUpdate(ErpTransferOrder record);

    int insertOrUpdateSelective(ErpTransferOrder record);

    int insertSelective(ErpTransferOrder record);

    ErpTransferOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpTransferOrder record);

    int updateByPrimaryKey(ErpTransferOrder record);

    int batchInsert(@Param("list") List<ErpTransferOrder> list);

    List<ErpTransferOrder> selectAllByMoveOutNo(@Param("gxList") List<String> gxList);

    List<ErpTransferOrder> selByOmpNo(@Param("orderDate") String orderDate,
                                      @Param("workCenterNo") String workCenterNo,
                                      @Param("prdNo") String prdNo);
}