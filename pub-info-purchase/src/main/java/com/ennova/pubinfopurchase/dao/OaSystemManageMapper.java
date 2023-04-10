package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaSystemManage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/4/7
 * @version 1.0
 */
@Mapper
public interface OaSystemManageMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(OaSystemManage record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaSystemManage record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    OaSystemManage selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaSystemManage record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaSystemManage record);

    List<OaSystemManage> selectByAll();
}