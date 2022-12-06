package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpSpecialException;
import com.ennova.pubinfoproduct.vo.ErpSpecialExceptionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ErpSpecialExceptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpSpecialException record);

    int insertSelective(ErpSpecialException record);

    ErpSpecialException selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpSpecialException record);

    int updateByPrimaryKey(ErpSpecialException record);

    int updateBatch(List<ErpSpecialException> list);

    int batchInsert(@Param("list") List<ErpSpecialException> list);

    List<ErpSpecialExceptionVO> selectSpecialExceptionLists();
}