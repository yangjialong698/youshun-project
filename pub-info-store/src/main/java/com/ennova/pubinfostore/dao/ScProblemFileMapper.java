package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.ScProblemFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScProblemFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScProblemFile record);

    int insertSelective(ScProblemFile record);

    ScProblemFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScProblemFile record);

    int updateByPrimaryKey(ScProblemFile record);

    int updateBatch(List<ScProblemFile> list);

    int batchInsert(@Param("list") List<ScProblemFile> list);

    List<ScProblemFile> selectFilesByProblemId(@Param("ProblemId") Integer ProblemId);

    List<ScProblemFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);
}