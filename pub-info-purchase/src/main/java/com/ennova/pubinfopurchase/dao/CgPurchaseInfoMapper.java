package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.CgPurchaseInfo;
import com.ennova.pubinfopurchase.vo.CgPurchaseInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */
@Mapper
public interface CgPurchaseInfoMapper {
    int insertInfoSelective(CgPurchaseInfo cgPurchaseInfo);

    Integer selectLastSerialNumber();

    CgPurchaseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(CgPurchaseInfo cgPurchaseInfo);

    List<CgPurchaseInfoVO> selectPurchaseInfo(@Param("name") String name);

    int deleteByPrimaryKey(Integer id);
}
