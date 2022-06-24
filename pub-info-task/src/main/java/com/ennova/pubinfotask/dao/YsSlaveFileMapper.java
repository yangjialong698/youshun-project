package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsSlaveFile;
import com.ennova.pubinfotask.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/28
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsSlaveFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsSlaveFile record);

    int insertSelective(YsSlaveFile record);

    YsSlaveFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsSlaveFile record);

    int updateByPrimaryKey(YsSlaveFile record);

    int updateBatch(List<YsSlaveFile> list);

    int batchInsert(@Param("list") List<YsSlaveFile> list);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    List<FileVO> selectByFileMasterIdlist(@Param("fileMasterId") Integer fileMasterId);

    List<YsSlaveFile> selectAllByFileMasterId(@Param("fileMasterId") Integer fileMasterId);

    List<YsSlaveFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5,@Param("userId")Integer userId);


}