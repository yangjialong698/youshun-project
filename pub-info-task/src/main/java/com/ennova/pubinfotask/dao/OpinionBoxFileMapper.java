package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.OpinionBoxFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpinionBoxFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpinionBoxFile record);

    int insertSelective(OpinionBoxFile record);

    OpinionBoxFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpinionBoxFile record);

    int updateByPrimaryKey(OpinionBoxFile record);

    int selectCountByFileMd5(String fileMd5);

    List<Integer> selectIdByIdList(@Param("fileMd5List") List<String> fileMd5List);

}