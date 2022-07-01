package com.ennova.pubinfofile.dao;

import com.ennova.pubinfofile.entity.YsSugComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YsSugCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsSugComment record);

    int insertSelective(YsSugComment record);

    YsSugComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsSugComment record);

    int updateByPrimaryKey(YsSugComment record);
}