package com.ennova.pubinfofile.dao;

import com.ennova.pubinfofile.entity.YsExpSugFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsExpSugFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsExpSugFile record);

    int insertSelective(YsExpSugFile record);

    YsExpSugFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsExpSugFile record);

    int updateByPrimaryKey(YsExpSugFile record);

    int deleteExpSugFile(Integer id);

    List<YsExpSugFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer id);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    int selectByExpSugId(@Param("expSugId")Integer expSugId);
}