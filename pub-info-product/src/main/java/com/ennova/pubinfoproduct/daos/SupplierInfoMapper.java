package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.SupplierInfo;
import java.util.List;
import com.ennova.pubinfoproduct.vo.CusAccSupplierVO;import com.ennova.pubinfoproduct.vo.SupplierInfoVO;import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplierInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierInfo record);

    int insertOrUpdate(SupplierInfo record);

    int insertOrUpdateSelective(SupplierInfo record);

    int insertSelective(SupplierInfo record);

    SupplierInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierInfo record);

    int updateByPrimaryKey(SupplierInfo record);

    int batchInsert(@Param("list") List<SupplierInfo> list);

    List<SupplierInfoVO> selectBySupplier(@Param("supplier") String supplier);

    List<CusAccSupplierVO> selectBySupplierParty(@Param("responsParty") String responsParty);

    SupplierInfo selectBySupplierNo(@Param("supplierNo") String supplierNo);

    List<SupplierInfo> selectAll();
}