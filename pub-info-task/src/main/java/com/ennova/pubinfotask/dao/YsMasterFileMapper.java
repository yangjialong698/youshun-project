package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsMasterFile;
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
public interface YsMasterFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsMasterFile record);

    int insertSelective(YsMasterFile record);

    YsMasterFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsMasterFile record);

    int updateByPrimaryKey(YsMasterFile record);

    int updateBatch(List<YsMasterFile> list);

    int batchInsert(@Param("list") List<YsMasterFile> list);

    //MasterFileDTO selectByYsMasterTaskId(@Param("ysMasterTaskId") Integer ysMasterTaskId);

    List<YsMasterFile> selectAllByYsMasterTaskId(@Param("ysMasterTaskId")Integer ysMasterTaskId);

    /**
     * 查看主任务是否有日报上传记录
     **/
    Integer selectHasRbById(@Param("ysMasterTaskId") Integer ysMasterTaskId);

    /**
     * 查看主任务是否有日报上传记录
     **/
//    Integer selectHasJyjyById(@Param("ysMasterTaskId") Integer ysMasterTaskId);


    // 是否存在经验建议
    Integer selectExpSugByYsMasterTaskId(@Param("ysMasterTaskId") Integer ysMasterTaskId);



    List<YsMasterFile> selectAllByYsSonTaskId(@Param("ysSonTaskId")Integer ysSonTaskId);

    List<YsMasterFile> selectByYsFileTypeId(@Param("ysFileTypeId")Integer ysFileTypeId);



}