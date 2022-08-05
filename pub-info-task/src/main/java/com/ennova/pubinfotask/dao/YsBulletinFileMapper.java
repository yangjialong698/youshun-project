package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsBulletinFile;
import java.util.List;

import com.ennova.pubinfotask.entity.YsSlaveFile;
import com.ennova.pubinfotask.vo.FileVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YsBulletinFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsBulletinFile record);

    int insertSelective(YsBulletinFile record);

    YsBulletinFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsBulletinFile record);

    int updateByPrimaryKey(YsBulletinFile record);

    int updateBatch(List<YsBulletinFile> list);

    int batchInsert(@Param("list") List<YsBulletinFile> list);

    List<YsBulletinFile> selectByFileMasterId(@Param("fileMasterId")Integer fileMasterId);

    List<YsBulletinFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    Integer selectByFileMd5(@Param("fileMd5")String fileMd5);



}