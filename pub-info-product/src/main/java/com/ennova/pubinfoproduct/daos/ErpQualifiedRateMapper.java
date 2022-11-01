package com.ennova.pubinfoproduct.daos;
import java.util.List;
import com.ennova.pubinfoproduct.entity.ErpQualifiedRate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErpQualifiedRateMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ErpQualifiedRate record);

    int insertSelective(ErpQualifiedRate record);

    ErpQualifiedRate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpQualifiedRate record);

    int updateByPrimaryKey(ErpQualifiedRate record);

    List<ErpQualifiedRate> selectAllByToday();


}