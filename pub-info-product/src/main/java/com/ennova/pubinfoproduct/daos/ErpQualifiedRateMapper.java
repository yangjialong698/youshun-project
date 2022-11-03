package com.ennova.pubinfoproduct.daos;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.ennova.pubinfoproduct.entity.ErpQualifiedRate;

public interface ErpQualifiedRateMapper {

    List<ErpQualifiedRate> selectAllByModuleNo(@Param("moduleNo") String moduleNo);
}