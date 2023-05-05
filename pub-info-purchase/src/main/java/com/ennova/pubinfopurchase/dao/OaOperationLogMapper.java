package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaOperationLog;
import com.ennova.pubinfopurchase.vo.OaOperationLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/5/5
 */
@Mapper
public interface OaOperationLogMapper {
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
    int insert(OaOperationLog record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaOperationLog record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    OaOperationLog selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaOperationLog record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaOperationLog record);

    //根据不良品处理单id查询不合格单日志明细
    List<OaOperationLogVO> selectByRejectsId(@Param("rejectsId") Integer rejectsId);
}