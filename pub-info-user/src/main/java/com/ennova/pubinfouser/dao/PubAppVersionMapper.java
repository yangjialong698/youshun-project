package com.ennova.pubinfouser.dao;

import com.ennova.pubinfouser.entity.PubAppVersion;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PubAppVersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PubAppVersion record);

    int insertOrUpdate(PubAppVersion record);

    int insertOrUpdateSelective(PubAppVersion record);

    int insertSelective(PubAppVersion record);

    PubAppVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PubAppVersion record);

    int updateByPrimaryKey(PubAppVersion record);

    int batchInsert(@Param("list") List<PubAppVersion> list);

    PubAppVersion selectByAppVersion(@Param("appVersion") String appVersion,@Param("versionType")Integer versionType);

    PubAppVersion selectByAppVersionType(@Param("versionType")Integer versionType);
}