package com.ennova.pubinfonew.dao;

import com.ennova.pubinfonew.entity.NewsPeriodical;
import com.ennova.pubinfonew.vo.NewsPeriodicalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsPeriodicalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsPeriodical record);

    int insertSelective(NewsPeriodical record);

    NewsPeriodical selectByPrimaryKey(Integer id);

    List<NewsPeriodicalVO> selectAllNewPeriodical(@Param("periodicalNum")Integer periodicalNum, @Param("editionNum")Integer editionNum, @Param("editionTitle") String editionTitle);

    int updateByPrimaryKeySelective(NewsPeriodical record);

    int updateByPrimaryKey(NewsPeriodical record);

    int batchInsert(@Param("list") java.util.List<NewsPeriodical> list);

    List<Integer> selectPeriodicalNum();
}