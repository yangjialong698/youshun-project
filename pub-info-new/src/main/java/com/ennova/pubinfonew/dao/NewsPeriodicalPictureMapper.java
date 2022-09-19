package com.ennova.pubinfonew.dao;

import com.ennova.pubinfonew.entity.NewsPeriodicalPicture;
import com.ennova.pubinfonew.vo.NewsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsPeriodicalPictureMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(NewsPeriodicalPicture record);

    int insertSelective(NewsPeriodicalPicture record);

    NewsPeriodicalPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsPeriodicalPicture record);

    int updateByPrimaryKey(NewsPeriodicalPicture record);

    List<NewsVO> selectPeriodicalPicture(@Param("periodicalNum") Integer periodicalNum, @Param("editionNum") Integer editionNum);

    NewsVO selectPeriodicalAndedition(@Param("periodicalNum") Integer periodicalNum, @Param("editionNum") Integer editionNum);

    List<NewsPeriodicalPicture> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);
}