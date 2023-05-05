package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaRejectsOpinion;
import com.ennova.pubinfopurchase.vo.OaRejectsOpinionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/3/20
 * @version 1.0
 */
@Mapper
public interface OaRejectsOpinionMapper {
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
    int insert(OaRejectsOpinion record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaRejectsOpinion record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    OaRejectsOpinion selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaRejectsOpinion record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaRejectsOpinion record);

    int updateBatch(List<OaRejectsOpinion> list);

    int batchInsert(@Param("list") List<OaRejectsOpinion> list);

    //根据不良品处理单id查询不合格单会签明细
    List<OaRejectsOpinionVO> selectByRejectsId(@Param("rejectsId") Integer rejectsId);

    //根据会签人id和会签人姓名查询会签明细
    List<OaRejectsOpinion> selectByOpinionUserIdAndOpinionUser(@Param("opinionUserId") Integer opinionUserId, @Param("opinionUser") String opinionUser);

    //根据不良品处理单id和查询不合格单会签明细
    List<OaRejectsOpinionVO> selectByRejectsIdAndSetpStaus(@Param("rejectsId") Integer rejectsId, @Param("setpStaus") Integer setpStaus);

    //根据不良品处理单id和查询不合格单会签明细
    OaRejectsOpinionVO selectByRejectsIdAndOpinionUserId(@Param("rejectsId") Integer rejectsId, @Param("opinionUserId") Integer opinionUserId, @Param("setpStaus") Integer setpStaus);
}