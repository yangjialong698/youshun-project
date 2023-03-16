package com.ennova.pubinfoproduct.daos;

import com.ennova.pubinfoproduct.entity.CustomerAccountInfo;
import java.util.List;
import com.ennova.pubinfoproduct.vo.ComplaintVO;import com.ennova.pubinfoproduct.vo.CustomerAccountInfoVO;import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerAccountInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAccountInfo record);

    int insertOrUpdate(CustomerAccountInfo record);

    int insertOrUpdateSelective(CustomerAccountInfo record);

    int insertSelective(CustomerAccountInfo record);

    CustomerAccountInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAccountInfo record);

    int updateByPrimaryKey(CustomerAccountInfo record);

    int batchInsert(@Param("list") List<CustomerAccountInfo> list);

    List<CustomerAccountInfoVO> selectByMonthNumAndKey(@Param("monthNum") Integer monthNum, @Param("key") String key);

    List<ComplaintVO> selectByComplainTime(@Param("year") int year, @Param("month") int month);

    List<CustomerAccountInfoVO> selectBySupplierNoAndTimeList(@Param("supplierNo") String supplierNo, @Param("year") String year, @Param("month") String month);
}