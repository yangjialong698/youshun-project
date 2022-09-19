package com.ennova.pubinfonew.dao;

import com.ennova.pubinfonew.entity.NewsComment;
import com.ennova.pubinfonew.vo.CurrentUserVO;
import com.ennova.pubinfonew.vo.NewsCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsComment record);

    int insertSelective(NewsComment record);

    NewsComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsComment record);

    int updateByPrimaryKey(NewsComment record);

    int batchInsert(@Param("list") java.util.List<NewsComment> list);

    List<CurrentUserVO> selectCheckPerson();

    int updateSortIdById(@Param("id") Integer id);

    int updateSortIdsById(@Param("id") Integer id);

    List<NewsCommentVO> selectCommentByEditionTitle();

    int deleteComment(@Param("id") Integer id);

    List<NewsComment> selectCommentByNewsId(@Param("newsId") Integer newsId);
}