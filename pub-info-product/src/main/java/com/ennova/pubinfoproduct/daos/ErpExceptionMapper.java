package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpException;
import com.ennova.pubinfoproduct.vo.ErpExceptionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ErpExceptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpException record);

    int insertSelective(ErpException record);

    ErpException selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpException record);

    int updateByPrimaryKey(ErpException record);

    int updateBatch(List<ErpException> list);

    int batchInsert(@Param("list") List<ErpException> list);

    List<ErpExceptionVO> selectBymuduleTypeLike(@Param("muduleType")Integer muduleType);

    List<ErpExceptionVO> selectExceptionCountList(@Param("dayNum") String dayNum);

}