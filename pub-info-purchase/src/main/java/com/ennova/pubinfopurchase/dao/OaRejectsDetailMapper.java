package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.dto.BadDisposalDTO;
import com.ennova.pubinfopurchase.dto.BadItemDTO;
import com.ennova.pubinfopurchase.dto.PrdInfoDTO;
import com.ennova.pubinfopurchase.entity.OaRejectsDetail;
import com.ennova.pubinfopurchase.vo.OaRejectsDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/3/17
 * @version 1.0
 */
@Mapper
public interface OaRejectsDetailMapper {
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
    int insert(OaRejectsDetail record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaRejectsDetail record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    OaRejectsDetail selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaRejectsDetail record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaRejectsDetail record);

    int updateBatch(List<OaRejectsDetail> list);

    int batchInsert(@Param("list") List<OaRejectsDetail> list);

    List<PrdInfoDTO> selectByWorkOrderNo(@Param("workOrderNo")String workOrderNo);

    List<BadItemDTO> selectBadItemInfo();

    List<BadDisposalDTO> selectBadDisposalInfo();

    List<OaRejectsDetailVO> selectRejectsDetail();

    //根据不良品处理单id查询不良品明细
    List<OaRejectsDetailVO> selectByRejectsId(@Param("rejectsId") Integer rejectsId);
}