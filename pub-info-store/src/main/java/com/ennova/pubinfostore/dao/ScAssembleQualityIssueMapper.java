package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.ScAssembleQualityIssue;
import com.ennova.pubinfostore.vo.AssembleUserVO;
import com.ennova.pubinfostore.vo.ScAssembleQualityIssueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScAssembleQualityIssueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScAssembleQualityIssue record);

    int insertSelective(ScAssembleQualityIssue record);

    ScAssembleQualityIssue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScAssembleQualityIssue record);

    int updateByPrimaryKey(ScAssembleQualityIssue record);

    int updateBatch(List<ScAssembleQualityIssue> list);

    int batchInsert(@Param("list") List<ScAssembleQualityIssue> list);

    List<ScAssembleQualityIssueVO> selectByProductNumberLike(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("productName") String productName);

    List<ScAssembleQualityIssueVO> assembleInfoListData(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("productName") String productName);

    AssembleUserVO selectAssembleUserById(Integer id);
}