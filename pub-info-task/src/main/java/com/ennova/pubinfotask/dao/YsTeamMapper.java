package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.entity.YsTeam;
import com.ennova.pubinfotask.vo.EditYsTeamVO;
import com.ennova.pubinfotask.vo.ExecutorVO;
import com.ennova.pubinfotask.vo.YsTeamPageListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/20
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsTeamMapper<selectByExecutorId> {
    int deleteByPrimaryKey(Integer id);

    int insert(YsTeam record);

    int insertSelective(YsTeam record);

    YsTeam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsTeam record);

    int updateByPrimaryKey(YsTeam record);

    int updateBatch(List<YsTeam> list);

    int batchInsert(@Param("list") List<YsTeam> list);

    EditYsTeamVO selectAllById(@Param("id")Integer id);

    List<YsTeamPageListVO> selectAllPageList(@Param("name") String name, @Param("dto") UserMasterDTO dto);

    List<EditYsTeamVO> selectAllByUserId(@Param("userId") Integer userId, @Param("masterTaskId") Integer masterTaskId);

    List<EditYsTeamVO> selectAllByUserIdAndExecutorId(@Param("userId") Integer userId, @Param("masterTaskId") Integer masterTaskId, @Param("executorId") Integer executorId);

    List<LinkedHashMap> selectTeamGroup(@Param("dto")UserMasterDTO dto);

    List<ExecutorVO> selectAllByRoleExecutor();

    List<YsTeam> selectByUserIdAndYsMasterTaskId(@Param("userId")Integer userId,@Param("ysMasterTaskId")Integer ysMasterTaskId);

    List<YsTeam> selectAllByYsMasterTaskIdAndExecutorId(@Param("executorId")Integer executorId, @Param("ysMasterTaskId")Integer ysMasterTaskId);

    String selectUserNameByTeamId(@Param("teamId") Integer teamId);




    //List<YsTeam> selectAllByExecutorId(@Param("executorId")Integer executorId);
    //List<LinkedHashMap> selectDeptAll();


    //List<TeamDTO> selectTeamDtoByMasterId(@Param("receiveId") Integer receiveId, @Param("masterTaskId") Integer masterTaskId);

    //int selectByUserIdSumTotalConsume(@Param("userId") Integer userId, @Param("masterTaskId") Integer masterTaskId);

    //List<YsTeam> selectAllByName(@Param("name") String name);
}