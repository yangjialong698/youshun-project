package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.TDeptDing;
import java.util.List;

import com.ennova.pubinfostore.vo.TDeptDingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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