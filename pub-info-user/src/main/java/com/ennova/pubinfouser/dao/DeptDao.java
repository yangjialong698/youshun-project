package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.DeptEntity;
import com.ennova.pubinfouser.vo.DeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptDao extends BaseDao<DeptEntity>{

    List<DeptEntity> getDeptByName(@Param("deptName")String deptName);

    int insertSelective(DeptEntity record);

    DeptEntity getDeptById(@Param("id")Integer id);

    int updateByPrimaryKeySelective(DeptEntity deptEntity);

    List<DeptVO> listDepts(@Param("company")Integer company, @Param("searchKey")String searchKey);

    List<DeptVO> listDeptList(@Param("company")Integer company);
}
