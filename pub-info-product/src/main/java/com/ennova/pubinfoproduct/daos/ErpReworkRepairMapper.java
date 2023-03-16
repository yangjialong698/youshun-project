package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpReworkRepair;
import java.util.List;
import com.ennova.pubinfoproduct.vo.DefectRateVO;import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ErpReworkRepairMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpReworkRepair record);

    int insertOrUpdate(ErpReworkRepair record);

    int insertOrUpdateSelective(ErpReworkRepair record);

    int insertSelective(ErpReworkRepair record);

    ErpReworkRepair selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpReworkRepair record);

    int updateByPrimaryKey(ErpReworkRepair record);

    int batchInsert(@Param("list") List<ErpReworkRepair> list);

    List<ErpReworkRepair> selectAll(@Param("key") String key);

    List<DefectRateVO> selectByReworkTime(@Param("year") int year, @Param("month") int month);

    List<ErpReworkRepair> selectBySupplierNoAndTime(@Param("supplierNo") String supplierNo, @Param("year") String year, @Param("month") String month);
}