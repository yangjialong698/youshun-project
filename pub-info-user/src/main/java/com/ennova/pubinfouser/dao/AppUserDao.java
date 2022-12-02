package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.AppUserEntity;
import com.ennova.pubinfouser.vo.AppUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppUserDao{


    AppUserVO getUserInfoByMobile(@Param("mobile") String mobile);

    AppUserVO getUserInfoByJobNum(@Param("jobNum")String jobNum);

    AppUserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppUserEntity record);

    List<AppUserVO> listAppUsers();

    List<AppUserVO> selectNotAppUser();

    int batchInsert(@Param("list") List<AppUserEntity> list);

    int insertSelective(AppUserEntity record);
}
