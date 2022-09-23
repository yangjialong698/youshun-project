package com.ennova.pubinfotask.dao;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.ennova.pubinfotask.entity.Mail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mail record);

    int insertSelective(Mail record);

    Mail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Mail record);

    int updateByPrimaryKey(Mail record);

    List<Mail> selectAll();

}