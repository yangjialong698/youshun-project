package com.ennova.pubinfonew.dao;

import com.ennova.pubinfonew.entity.NewsPeriodicalHotPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsPeriodicalHotPictureMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(NewsPeriodicalHotPicture record);

    int insertSelective(NewsPeriodicalHotPicture record);

    NewsPeriodicalHotPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsPeriodicalHotPicture record);

    int updateByPrimaryKey(NewsPeriodicalHotPicture record);

    List<NewsPeriodicalHotPicture> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);
}