package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsMasterTask;
import com.ennova.pubinfotask.vo.EditMasterTaskAndFileVO;
import com.ennova.pubinfotask.vo.MasterLeve1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/13
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsMasterTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsMasterTask record);

    int insertSelective(YsMasterTask record);

    YsMasterTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsMasterTask record);

    int updateByPrimaryKey(YsMasterTask record);

    int updateBatch(List<YsMasterTask> list);

    int batchInsert(@Param("list") List<YsMasterTask> list);

    Integer selectLastSerialNumber();

    List<LinkedHashMap> selectAll();

    EditMasterTaskAndFileVO selectTaskAndFileOne(@Param("id") Integer id);

    LinkedHashMap selectTaskDetailsOne(@Param("id") Integer id);

    LinkedHashMap selectTaskCount(@Param("receiveId") Integer receiveId);

    List<LinkedHashMap> selectBySubTaskManageAllUser(@Param("receiveId") Integer receiveId);

    List<LinkedHashMap> selectTaskMoveAllUser(@Param("userId") Integer userId);

    List<MasterLeve1> selectMasterLeve1(@Param("receiveId") Integer receiveId, @Param("name") String name, @Param("status") Integer status);

    List<YsMasterTask> selectByName(@Param("name")String name);

    List<YsMasterTask> selectByIdAndName(@Param("id")Integer id,@Param("name")String name);

    /**
     * 预计=sum(该任务的子任务的预计工时)
     **/
    Integer selectSumEstimateWork(@Param("ysMasterTaskId") Integer ysMasterTaskId);

    /**
     * 消耗=sum(该任务的子任务的报工工时)；
     **/
    Integer selectSumTotalConsume(@Param("ysMasterTaskId") Integer ysMasterTaskId);

    /**
     * 根据主任务ID，查询任务状态和负责人
     **/
    LinkedHashMap<String, Integer> selectStatusAndReceiveIdById(@Param("ysMasterTaskId") Integer ysMasterTaskId);

}