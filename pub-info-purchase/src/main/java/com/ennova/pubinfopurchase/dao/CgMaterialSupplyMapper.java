package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.CgMaterialSupply;
import com.ennova.pubinfopurchase.vo.CgMaterialSupplyVO;
import com.ennova.pubinfopurchase.vo.PrdInfoVO;
import com.ennova.pubinfopurchase.vo.SupplierInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  yangjialong
 * @date  2023/3/1
 * @version 1.0
 */
@Mapper
public interface CgMaterialSupplyMapper {
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
    int insert(CgMaterialSupply record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(CgMaterialSupply record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    CgMaterialSupply selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(CgMaterialSupply record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(CgMaterialSupply record);

    int updateBatch(List<CgMaterialSupply> list);

    int batchInsert(@Param("list") List<CgMaterialSupply> list);

    /*
    * 从货品属性表根据品号查询品名
    * */
    List<PrdInfoVO> selectByPrdNo(@Param("prdNo")String prdNo);

    /*
    * 从供应商字典表查询供应商编号和供应商名称
    * */
    List<SupplierInfoVO> selectBySupplierNo(@Param("supplierNo") String supplierNo);

    List<CgMaterialSupplyVO> selectMaterialSupplyInfo();
}