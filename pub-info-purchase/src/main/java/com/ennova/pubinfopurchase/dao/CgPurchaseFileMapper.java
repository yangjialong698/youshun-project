package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.CgPurchaseFile;
import com.ennova.pubinfopurchase.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */
@Mapper
public interface CgPurchaseFileMapper {
    int insertFileSelective(CgPurchaseFile cgPurchaseFile);

    List<CgPurchaseFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    CgPurchaseFile selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CgPurchaseFile cgPurchaseFile);

    List<FileVO> selectByPurchaseInfoId(Integer id);
}
