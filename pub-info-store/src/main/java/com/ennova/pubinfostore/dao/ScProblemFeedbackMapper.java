package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.dto.UserDTO;import com.ennova.pubinfostore.entity.ScProblemFeedback;
import java.util.List;
import com.ennova.pubinfostore.vo.DutyDepartmentVO;import com.ennova.pubinfostore.vo.DutyPersonVO;import org.apache.ibatis.annotations.Param;

public interface ScProblemFeedbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScProblemFeedback record);

    int insertSelective(ScProblemFeedback record);

    ScProblemFeedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScProblemFeedback record);

    int updateByPrimaryKey(ScProblemFeedback record);

    int updateBatch(List<ScProblemFeedback> list);

    int batchInsert(@Param("list") List<ScProblemFeedback> list);

    List<DutyDepartmentVO> selectDutyDepartmentList();

    List<DutyPersonVO> selectDutyPersonList(@Param("departmentId") String departmentId);

    UserDTO selectById(@Param("id") Integer id);

    List<ScProblemFeedback> selectAllByBackStatusOrDutyPerson(@Param("searchKey") String searchKey);
}