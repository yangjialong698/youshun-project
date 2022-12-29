package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.CustomerAccountFile;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface CustomerAccountFileMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAccountFile record);

    int insertOrUpdate(CustomerAccountFile record);

    int insertOrUpdateSelective(CustomerAccountFile record);

    int insertSelective(CustomerAccountFile record);

    CustomerAccountFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAccountFile record);

    int updateByPrimaryKey(CustomerAccountFile record);

    int updateBatch(List<CustomerAccountFile> list);

    int updateBatchSelective(List<CustomerAccountFile> list);

    int batchInsert(@Param("list") List<CustomerAccountFile> list);

    List<CustomerAccountFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5,@Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5")String fileMd5);

    List<CustomerAccountFile> selectAllByCustomerAccountId(@Param("customerAccountId")Integer customerAccountId);




}