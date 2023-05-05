package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaRejects;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/30
 */
@Mapper
public interface OaRejectsMapper {
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(OaRejects record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaRejects record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    OaRejects selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaRejects record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaRejects record);

    int updateBatch(List<OaRejects> list);

    int batchInsert(@Param("list") List<OaRejects> list);

    String selectLastSerialNumber();

    List<OaRejects> selectRejectsInfo(@Param("startTime") String startTime,
                                      @Param("endTime") String endTime,
                                      @Param("workCenter") String workCenter,
                                      @Param("userName") String userName,
                                      @Param("schedule") String schedule,
                                      @Param("headline") String headline);

    List<OaRejects> selectRejectsInfos(@Param("startTime") String startTime,
                                      @Param("endTime") String endTime,
                                      @Param("workCenter") String workCenter,
                                      @Param("userName") String userName,
                                       @Param("transactor") String transactor,
                                      @Param("schedule") String schedule,
                                      @Param("headline") String headline);
}