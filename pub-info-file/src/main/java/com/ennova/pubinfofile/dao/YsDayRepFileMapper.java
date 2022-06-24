package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsDayRepFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsDayRepFileMapper {


    int deleteByPrimaryKey(Integer id);


    int insert(YsDayRepFile record);


    int insertSelective(YsDayRepFile record);


    YsDayRepFile selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(YsDayRepFile record);


    int updateByPrimaryKey(YsDayRepFile record);

    List<YsDayRepFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer id);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    int selectByDayRepId(@Param("dayRepId") Integer dayRepId);

    int deleteYsDayRepFile(Integer id);
}