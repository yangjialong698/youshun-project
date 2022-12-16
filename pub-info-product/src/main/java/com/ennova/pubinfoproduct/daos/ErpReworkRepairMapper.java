package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.ErpReworkRepair;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ErpReworkRepairMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErpReworkRepair record);

    int insertSelective(ErpReworkRepair record);

    ErpReworkRepair selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpReworkRepair record);

    int updateByPrimaryKey(ErpReworkRepair record);

    int updateBatch(List<ErpReworkRepair> list);

    int batchInsert(@Param("list") List<ErpReworkRepair> list);
}