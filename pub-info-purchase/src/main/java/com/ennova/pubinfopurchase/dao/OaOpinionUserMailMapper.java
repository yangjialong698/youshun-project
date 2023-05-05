package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaOpinionUserMail;
import com.ennova.pubinfopurchase.vo.OaOpinionUserMailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/5/5
 */
@Mapper
public interface OaOpinionUserMailMapper {
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
    int insert(OaOpinionUserMail record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaOpinionUserMail record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    OaOpinionUserMail selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaOpinionUserMail record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaOpinionUserMail record);

    //根据步骤状态查询不合格单会签人明细
    List<OaOpinionUserMailVO> selectBySetpStaus(@Param("setpStaus") Integer setpStaus);

    OaOpinionUserMailVO selectByName(@Param("name") String name);
}