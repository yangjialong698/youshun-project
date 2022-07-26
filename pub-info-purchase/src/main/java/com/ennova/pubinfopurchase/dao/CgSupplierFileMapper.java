package com.ennova.pubinfopurchase.dao;


import com.ennova.pubinfopurchase.entity.CgSupplierFile;
import com.ennova.pubinfopurchase.vo.FileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CgSupplierFileMapper {


    int deleteByPrimaryKey(Integer id);

    int insert(CgSupplierFile record);

    List<CgSupplierFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    int insertSelective(CgSupplierFile record);

    CgSupplierFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CgSupplierFile record);

    int updateByPrimaryKey(CgSupplierFile record);

    List<FileVO> selectByCgSupplierId(Integer id);
}