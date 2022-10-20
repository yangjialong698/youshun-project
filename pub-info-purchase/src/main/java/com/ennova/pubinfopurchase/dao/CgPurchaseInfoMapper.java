package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.CgPurchaseInfo;
import com.ennova.pubinfopurchase.vo.CgPurchaseInfoVO;
import com.ennova.pubinfopurchase.vo.TaskNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CgPurchaseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CgPurchaseInfo record);

    int insertSelective(CgPurchaseInfo record);

    CgPurchaseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CgPurchaseInfo record);

    int updateByPrimaryKey(CgPurchaseInfo record);

    Integer selectLastSerialNumber();

    List<CgPurchaseInfoVO> selectPurchaseInfo(@Param("name") String name, @Param("type") Integer type);

    List<TaskNumber> selectTaskNumber(@Param("name") String name);

    List<TaskNumber> selectTaskNumberIsExist(@Param("serialNumber") Integer serialNumber);
}