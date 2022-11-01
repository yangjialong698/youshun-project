package com.ennova.pubinfoproduct.daos;
import java.util.List;
import com.ennova.pubinfoproduct.entity.ErpInputScrap;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErpInputScrapMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ErpInputScrap record);

    int insertSelective(ErpInputScrap record);

    ErpInputScrap selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpInputScrap record);

    int updateByPrimaryKey(ErpInputScrap record);

    List<ErpInputScrap> selectAll();
}