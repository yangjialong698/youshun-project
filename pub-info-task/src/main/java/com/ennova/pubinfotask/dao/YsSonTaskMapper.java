package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.dto.MasterTeamGroupDTO;
import com.ennova.pubinfotask.dto.UserMasterDTO;
import com.ennova.pubinfotask.dto.WorkTimeResidueDTO;
import com.ennova.pubinfotask.entity.YsSonTask;
import com.ennova.pubinfotask.vo.EditSontTaskAndFileVO;
import com.ennova.pubinfotask.vo.WorkTimeResidueVO;
import com.ennova.pubinfotask.vo.YsSonTaskPageListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/20
 * @Description: com.ennova.pubinfotask.dao
 * @Version: 1.0
 */
@Mapper
public interface YsSonTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsSonTask record);

    int insertSelective(YsSonTask record);

    YsSonTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsSonTask record);

    int updateByPrimaryKey(YsSonTask record);

    int updateBatch(List<YsSonTask> list);

    int batchInsert(@Param("list") List<YsSonTask> list);

    int updateSonTaskByMasterId(@Param("receiveId") Integer receiveId, @Param("id") Integer id);

    List<LinkedHashMap> selectMasterNameByReceiveId(@Param("userId") Integer userId);

    List<LinkedHashMap> selectMasterNameByExecutor(@Param("executorId") Integer executorId);

    List<LinkedHashMap> selectMasterNameNotCloseByReceiveId(@Param("list") Set<Integer> list);

    List<LinkedHashMap> selectMasterNameNotCloseByExecutor(@Param("executorId") Integer executorId);

    int updateByYsMasterTaskId(@Param("updated") YsSonTask updated);

    List<YsSonTask> selectAllByYsMasterTaskIdAndYsTeamId(@Param("ysMasterTaskId") Integer ysMasterTaskId,
                                                         @Param("ysTeamId") Integer ysTeamId);

    List<YsSonTaskPageListVO> selectSonTaskPagelist(@Param("status") Integer status,
                                                    @Param("teamUserId") Integer teamUserId,
                                                    @Param("sonTaskName") String sonTaskName,
                                                    @Param("dto") UserMasterDTO dto,
                                                    //@Param("masterTaskName") String masterTaskName
                                                    @Param("masterTaskId") Integer masterTaskId
    );

    EditSontTaskAndFileVO selectSonTaskOne(@Param("sonTaskId") Integer sonTaskId);

    WorkTimeResidueVO selectEstimateHourByMasterTaskId(@Param("masterTaskId") Integer masterTaskId);

    List<YsSonTask> selectAllByYsTeamId(@Param("ysTeamId")Integer ysTeamId);

    List<WorkTimeResidueDTO> selectWorkTimeResidueDTOByMasterId(@Param("receiveId") Integer receiveId, @Param("masterTaskId") Integer masterTaskId);
    
    List<MasterTeamGroupDTO> selectGroupTeamIdByMasterTaskId(@Param("ysMasterTaskId") Integer ysMasterTaskId);
}