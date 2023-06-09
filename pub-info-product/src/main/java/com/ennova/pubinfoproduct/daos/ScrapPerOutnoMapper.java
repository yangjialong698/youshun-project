package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ScrapPerOutno;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScrapPerOutnoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScrapPerOutno record);

    int insertOrUpdate(ScrapPerOutno record);

    int insertOrUpdateSelective(ScrapPerOutno record);

    int insertSelective(ScrapPerOutno record);

    ScrapPerOutno selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScrapPerOutno record);

    int updateByPrimaryKey(ScrapPerOutno record);

    int updateBatch(List<ScrapPerOutno> list);

    int updateBatchSelective(List<ScrapPerOutno> list);

    int batchInsert(@Param("list") List<ScrapPerOutno> list);

    List<ScrapPerOutno> selectByOutNos(@Param("list")List<String> gxList);

    List<ScrapPerOutno> selectByMoveOutNoAndOrderDate(@Param("moveOutNo")String moveOutNo,@Param("orderDate")String orderDate);


}