package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.OaRejectsFile;
import com.ennova.pubinfopurchase.vo.OaRejectsFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/4/11
 * @version 1.0
 */
@Mapper
public interface OaRejectsFileMapper {
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
    int insert(OaRejectsFile record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(OaRejectsFile record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    OaRejectsFile selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(OaRejectsFile record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(OaRejectsFile record);

    List<OaRejectsFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    List<OaRejectsFileVO> selectByRejectsId(@Param("rejectsId") Integer rejectsId);
}