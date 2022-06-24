package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsSlaveFileEntity;
import com.ennova.pubinfofile.vo.FileVO;
import com.ennova.pubinfofile.vo.YsSlaveFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsSlaveFileMapper {

    int insertSelective(YsSlaveFileVO fileVO);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    int deleteByPrimaryKey(Integer id);

    int selectByFileMasterId(@Param("fileMasterId") Integer fileMasterId);

    int updateByPrimaryKeySelective(YsSlaveFileVO ysSlaveFile);

    int deleteSlaveFiles(Integer id);

    List<YsSlaveFileEntity> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer id);
}