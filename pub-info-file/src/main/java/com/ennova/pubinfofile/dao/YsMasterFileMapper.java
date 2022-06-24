package com.ennova.pubinfofile.dao;


import com.ennova.pubinfofile.entity.YsMasterFileEntity;
import com.ennova.pubinfofile.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface YsMasterFileMapper {

    //角色不是子任务管理查询所有文档
    List<FileDetailVO> getFileDetails(@Param("ysFileTypeId") Integer ysFileTypeId,
                                      @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                      @Param("ysSonTaskId")Integer ysSonTaskId,
                                      @Param("fileName")String fileName,
                                      @Param("userId")Integer userId,
                                      @Param("startTime")String startTime,
                                      @Param("endTime")String endTime);

    //查询我的文档
    List<FileDetailVO> getMyFileDetails(@Param("ysFileTypeId") Integer ysFileTypeId,
                                      @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                      @Param("ysSonTaskId")Integer ysSonTaskId,
                                      @Param("fileName")String fileName,
                                      @Param("userId")Integer userId);

    List<FileTypeCou> getFileTypeCou();

    Integer dayUpNum();

    Integer myFileNum(@Param("userId")Integer userId);

    Integer fileTotal();

    Integer mySugNum(@Param("userId")Integer userId);

    Integer mySugTotal();

    List<FileDownVO> downDetail(@Param("id")Integer id);

    //角色是子任务管理查询所有文档
    List<FileDetailVO> getFileDetailsForZrw(@Param("ysFileTypeId") Integer ysFileTypeId,
                                            @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                            @Param("ysSonTaskId")Integer ysSonTaskId,
                                            @Param("fileName")String fileName,
                                            @Param("userId")Integer userId,
                                            @Param("startTime")String startTime,
                                            @Param("endTime")String endTime);

//    List<FileDetailVO> getMyFileDetailsForZrw(@Param("ysFileTypeId") Integer ysFileTypeId,
//                                            @Param("ysMasterTaskId")Integer ysMasterTaskId,
//                                            @Param("ysSonTaskId")Integer ysSonTaskId,
//                                            @Param("fileName")String fileName,@Param("type")String type,
//                                            @Param("userId")Integer userId);

    ModifyFileVO selectDetailOne(@Param("id")Integer id);

    int updateByPrimaryKeySelective(YsMasterFileVO record);

    List<LinkedHashMap> selectMasterTask();

    List<LinkedHashMap> selectMasterTaskByZrw(@Param("userId")Integer userId);

    List<LinkedHashMap> selectSonTask(@Param("ysMasterTaskId") Integer ysMasterTaskId);

    int delete(Integer id);

    int insertSelective(YsMasterFileVO record);

    int deleteByPrimaryKey(Integer id);

    YsMasterFileVO selectByPrimaryKey(Integer id);

    List<LinkedHashMap> queryMasterTaskByUid(Integer userId);

    List<LinkedHashMap> queryMasterTask();

    List<FileDetailVO> getDetailsByMaskTaskIds(@Param("maskTaskIdList") List<Integer> maskTaskIdList,
                                                 @Param("ysFileTypeId") Integer ysFileTypeId,
                                                 @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                                 @Param("ysSonTaskId")Integer ysSonTaskId,
                                                 @Param("fileName")String fileName);

    List<LinkedHashMap> selectMaster(@Param("ysMasterTaskIdList") List<Integer> ysMasterTaskIdList);

    List<FileDetailVO> getDayRepsByRoleCode(@Param("filePrefix") String filePrefix ,
                                            @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                            @Param("fileName")String fileName,
                                            @Param("userId")Integer userId,
                                            @Param("subRoleCode")String subRoleCode,
                                            @Param("startTime")String startTime,
                                            @Param("endTime")String endTime);


    List<FileDetailVO> getDayRepAll(@Param("filePrefix") String filePrefix,
                                    @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                    @Param("fileName")String fileName,
                                    @Param("userId")Integer userId,
                                    @Param("startTime")String startTime,
                                    @Param("endTime")String endTime);

    List<FileDetailVO> getDayRepsBySelf(@Param("filePrefix") String filePrefix,
                                        @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                        @Param("fileName")String fileName,
                                        @Param("userId")Integer userId,
                                        @Param("startTime")String startTime,
                                        @Param("endTime")String endTime);

    List<FileDetailVO> getDayRepDetailsByStm(@Param("maskTaskIdList")List<Integer> maskTaskIdList,
                                             @Param("filePrefix") String filePrefix,
                                             @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                             @Param("fileName")String fileName,
                                             @Param("userId")Integer userId,
                                             @Param("startTime")String startTime,
                                             @Param("endTime")String endTime);
}
