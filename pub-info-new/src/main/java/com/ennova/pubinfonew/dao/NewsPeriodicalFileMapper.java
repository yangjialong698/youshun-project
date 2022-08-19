package com.ennova.pubinfonew.dao;

import com.ennova.pubinfonew.entity.NewsPeriodicalFile;
import com.ennova.pubinfonew.vo.NewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsPeriodicalFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsPeriodicalFile record);

    int insertSelective(NewsPeriodicalFile record);

    NewsPeriodicalFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsPeriodicalFile record);

    int updateByPrimaryKey(NewsPeriodicalFile record);

    int batchInsert(@Param("list") java.util.List<NewsPeriodicalFile> list);

    List<NewsPeriodicalFile> selectAllByFileMd5AndUserId(@Param("fileMd5")String fileMd5, @Param("userId")Integer userId);

    int selectByFileMd5(@Param("fileMd5") String fileMd5);

    List<NewsVO> selectPeriodicalFile(@Param("periodicalNum") Integer periodicalNum);

}