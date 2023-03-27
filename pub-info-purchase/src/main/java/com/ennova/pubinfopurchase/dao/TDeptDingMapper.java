package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.TDeptDing;
import com.ennova.pubinfopurchase.vo.TDeptDingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TDeptDingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDeptDing record);

    int insertSelective(TDeptDing record);

    TDeptDing selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDeptDing record);

    int updateByPrimaryKey(TDeptDing record);

    int updateBatch(List<TDeptDing> list);

    int batchInsert(@Param("list") List<TDeptDing> list);

    List<TDeptDing> selectAllByParentId(@Param("parentId") Long parentId);

    List<TDeptDing> selectAllByParentIdAll(@Param("list") List<Long> parentIds);

    List<TDeptDingVO> findListByParentId(@Param("parentId")Long parentId);
}