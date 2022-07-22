package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.CgSupplierCertification;
import com.ennova.pubinfotask.vo.CgSupplierCertificationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CgSupplierCertificationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_supplier_certification
     *
     * @mbg.generated Thu Jul 21 13:23:47 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    int insert(CgSupplierCertification record);

    Integer selectLastSerialNumber();

    //当前创建人的供应商认证
    CgSupplierCertification selectbyIdAndCreateId(@Param("id")Integer id, @Param("createId")Integer createUserId);

    //供应商列表
    List<CgSupplierCertificationVO> selectByStatusAndSupplierName(@Param("status") Integer status, @Param("supplierName") String supplierName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_supplier_certification
     *
     * @mbg.generated Thu Jul 21 13:23:47 CST 2022
     */
    int insertSelective(CgSupplierCertification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_supplier_certification
     *
     * @mbg.generated Thu Jul 21 13:23:47 CST 2022
     */
    CgSupplierCertification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CgSupplierCertification record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_supplier_certification
     *
     * @mbg.generated Thu Jul 21 13:23:47 CST 2022
     */
    int updateByPrimaryKey(CgSupplierCertification record);
}