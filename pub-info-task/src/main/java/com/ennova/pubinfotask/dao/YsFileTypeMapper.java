package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsFileType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/26
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsFileTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsFileType record);

    int insertSelective(YsFileType record);

    YsFileType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsFileType record);

    int updateByPrimaryKey(YsFileType record);

    int updateBatch(List<YsFileType> list);

    int batchInsert(@Param("list") List<YsFileType> list);

    List<YsFileType> selectByNameList(@Param("name") String name);

    List<YsFileType> selectAll();

    List<YsFileType> selectAllByFilePrefixAndName(@Param("filePrefix")String filePrefix,@Param("name")String name);



}