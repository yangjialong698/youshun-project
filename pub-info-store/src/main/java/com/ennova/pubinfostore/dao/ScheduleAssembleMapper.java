package com.ennova.pubinfostore.dao;

import com.ennova.pubinfostore.entity.ScheduleAssemble;
import com.ennova.pubinfostore.vo.AssembleUserVO;
import com.ennova.pubinfostore.vo.ScheduleAssembleListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleAssembleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ScheduleAssemble record);

    ScheduleAssemble selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ScheduleAssemble record);

    List<AssembleUserVO> selectAssembleUserList(@Param("userName") String userName);

    List<AssembleUserVO> selectUserByIds(@Param("ids") List<Integer> ids);

    List<ScheduleAssembleListVO> selectPreAssembleList(@Param("deliveryDate") String deliveryDate, @Param("searchKey") String searchKey, @Param("id") Integer id);
}