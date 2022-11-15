package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.vo.ErpQualifiedRateVO;
import org.apache.ibatis.annotations.Param;import java.util.List;

public interface ErpQualifiedRateMapper {


    List<ErpQualifiedRateVO> selectAllByModuleNo(@Param("gxList")  List<String> gxList);
}