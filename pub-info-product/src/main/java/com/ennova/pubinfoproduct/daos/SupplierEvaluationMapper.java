package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.SupplierEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierEvaluationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierEvaluation record);

    int insertSelective(SupplierEvaluation record);

    SupplierEvaluation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierEvaluation record);

    int updateByPrimaryKey(SupplierEvaluation record);

    int insertList(@Param("list")List<SupplierEvaluation> list);

//    int selectByEvaluationTime(@Param("evaluationTime")Date evaluationTime);

//    int deleteByEvaluationTime(@Param("evaluationTime")Date evaluationTime);

    List<SupplierEvaluation> selectBySort(@Param("year")String year,@Param("month")String month);

    SupplierEvaluation selectBySupplierNoAndTime(@Param("supplierNo") String supplierNo, @Param("year") int year, @Param("month") int month);

    SupplierEvaluation selectBySupplierNoAndEvaluationTime(@Param("supplierNo") String supplierNo, @Param("year") int year, @Param("month") int month);

}