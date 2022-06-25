package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsDayRep;
import com.ennova.pubinfofile.vo.DayRepDetailVO;
import com.ennova.pubinfofile.vo.FileDetailVO;
import com.ennova.pubinfofile.vo.FileDownVO;
import com.ennova.pubinfofile.vo.YsDayRepVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsDayRepMapper {


    int deleteByPrimaryKey(Integer id);


    int insert(YsDayRep record);


    int insertSelective(YsDayRep record);


    YsDayRep selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(YsDayRep record);


    int updateByPrimaryKey(YsDayRep record);

    YsDayRepVO selectDetailOne(@Param("id")Integer id);

    List<DayRepDetailVO> getDayRepsByRoleCode(
                                            @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                            @Param("fileName")String fileName,
                                            @Param("userId")Integer userId,
                                            @Param("subRoleCode")String subRoleCode,
                                            @Param("startTime")String startTime,
                                            @Param("endTime")String endTime);

    List<DayRepDetailVO> getDayRepDetailsByStm(
                                             @Param("maskTaskIdList")List<Integer> maskTaskIdList,
                                             @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                             @Param("fileName")String fileName,
                                             @Param("userId")Integer userId,
                                             @Param("startTime")String startTime,
                                             @Param("endTime")String endTime);

    List<DayRepDetailVO> getDayRepsBySelf(
                                        @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                        @Param("fileName")String fileName,
                                        @Param("userId")Integer userId,
                                        @Param("startTime")String startTime,
                                        @Param("endTime")String endTime);

    List<DayRepDetailVO> getDayRepAll(
                                      @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                      @Param("fileName")String fileName,
                                      @Param("userId")Integer userId,
                                      @Param("startTime")String startTime,
                                      @Param("endTime")String endTime);

    int delete(Integer id);

    List<FileDownVO> fileDetail(@Param("id")Integer id);

    List<DayRepDetailVO> getDayRepDetails(@Param("ysMasterTaskId")Integer ysMasterTaskId,
                                          @Param("fileName")String fileName,
                                          @Param("userId")Integer userId,
                                          @Param("startTime")String startTime,
                                          @Param("endTime")String endTime);
}