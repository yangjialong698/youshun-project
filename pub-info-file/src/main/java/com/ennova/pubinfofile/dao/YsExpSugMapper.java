package com.ennova.pubinfofile.dao;

import com.ennova.pubinfofile.entity.YsExpSug;
import com.ennova.pubinfofile.vo.ExpSugDetailVO;
import com.ennova.pubinfofile.vo.FileDownVO;
import com.ennova.pubinfofile.vo.YsExpSugVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface YsExpSugMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsExpSug record);

    int insertSelective(YsExpSug record);

    YsExpSug selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsExpSug record);

    int updateByPrimaryKey(YsExpSug record);

    int delete(Integer id);

    YsExpSugVO selectDetailOne(@Param("id")Integer id);

    List<FileDownVO> fileDetail(@Param("id")Integer id);

    List<ExpSugDetailVO> getFileDetailsForZrw(@Param("ysMasterTaskId")Integer ysMasterTaskId,
                                              @Param("fileName")String fileName,
                                              @Param("userId")Integer userId);

    List<ExpSugDetailVO> getDetailsByMaskTaskIds(@Param("maskTaskIdList") List<Integer> maskTaskIdList,
                                                 @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                                 @Param("fileName")String fileName);

    List<ExpSugDetailVO> getFileDetails( @Param("ysMasterTaskId")Integer ysMasterTaskId,
                                         @Param("fileName")String fileName,
                                         @Param("userId")Integer userId);

    List<LinkedHashMap> queryMasterTask();

}