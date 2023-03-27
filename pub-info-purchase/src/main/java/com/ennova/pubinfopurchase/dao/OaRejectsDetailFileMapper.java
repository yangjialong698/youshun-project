package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaRejectsDetailFile;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/3/17
 * @version 1.0
 */
@Mapper
public interface OaRejectsDetailFileMapper {
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
    int insert(OaRejectsDetailFile record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaRejectsDetailFile record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    OaRejectsDetailFile selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaRejectsDetailFile record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaRejectsDetailFile record);

    List<OaRejectsDetailFileVO> selectAllByRejectsDetailId(Integer id);
}