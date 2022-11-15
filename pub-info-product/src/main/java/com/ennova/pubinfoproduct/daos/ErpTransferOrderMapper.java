package com.ennova.pubinfoproduct.daos;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.ennova.pubinfoproduct.entity.ErpTransferOrder;

public interface ErpTransferOrderMapper {

    List<ErpTransferOrder> selectAllByMoveOutNo(@Param("gxList") List<String> gxList);
}