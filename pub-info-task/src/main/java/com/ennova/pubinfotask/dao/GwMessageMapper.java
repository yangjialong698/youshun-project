package com.ennova.pubinfotask.dao;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.ennova.pubinfotask.entity.GwMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GwMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GwMessage record);

    int insertSelective(GwMessage record);

    GwMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GwMessage record);

    int updateByPrimaryKey(GwMessage record);

    List<GwMessage> selectByRemarkLike(@Param("likeRemark")String likeRemark);

}