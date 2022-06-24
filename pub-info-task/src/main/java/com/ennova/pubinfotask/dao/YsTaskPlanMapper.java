package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.entity.YsTaskPlan;
import com.ennova.pubinfotask.vo.YsTaskPlanPageListVO;
import com.ennova.pubinfotask.vo.YsTaskPlanTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/23
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsTaskPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsTaskPlan record);

    int insertSelective(YsTaskPlan record);

    YsTaskPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsTaskPlan record);

    int updateByPrimaryKey(YsTaskPlan record);

    int updateByYsMasterTaskId(@Param("updated")YsTaskPlan updated);

    int updateBatch(List<YsTaskPlan> list);

    int batchInsert(@Param("list") List<YsTaskPlan> list);

    List<YsTaskPlan> selectByReceiveIdAndYsMasterTaskId(@Param("receiveId")Integer receiveId,@Param("ysMasterTaskId")Integer ysMasterTaskId);

    List<LinkedHashMap> selectMasterTaskAll(@Param("list") Set<Integer> list);

    List<YsTaskPlanPageListVO> selectTaskPlanList(@Param("ysMasterTaskId") Integer ysMasterTaskId, @Param("planStatus") Integer planStatus, @Param("likeName") String likeName,
                                                  @Param("receiveId") Integer receiveId);

    List<YsTaskPlanTreeVO> selectTaskPlanTree(@Param("dto")UserMasterDTO dto);

    List<YsTaskPlan> selectAllByReceiveIdAndYsMasterTaskIdAndName(@Param("receiveId") Integer receiveId, @Param("ysMasterTaskId") Integer ysMasterTaskId, @Param("name") String name, @Param("id") Integer id);


}