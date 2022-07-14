package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsBulletin;
import com.ennova.pubinfotask.vo.ReviewerVO;
import com.ennova.pubinfotask.vo.YsBulletinVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YsBulletinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsBulletin record);

    int insertSelective(YsBulletin record);

    YsBulletin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsBulletin record);

    int updateByPrimaryKey(YsBulletin record);

    int updateBatch(List<YsBulletin> list);

    int batchInsert(@Param("list") List<YsBulletin> list);

    // 当前发布人的公告
    YsBulletin selectbyIdAndCreateId(@Param("id")Integer id,@Param("createId")Integer createId);

    // 审核人列表
    List<ReviewerVO> getReviewerList();

    // 公告列表
    List<YsBulletinVO> selectByStatusAndTitleLike(@Param("status") Integer status, @Param("likeTitle") String likeTitle, @Param("userId") Integer userId, @Param("orderBy") String orderBy);

    // 查询数据库中是否存在该公告
    List<YsBulletin> selectByTitleAndContent(@Param("title")String title,@Param("content")String content);

    // 查询数据库中是否存在该公告，排除当前id
    List<YsBulletin> selectByTitleAndContentAndIdNot(@Param("title")String title,@Param("content")String content,@Param("notId")Integer notId);

    //公告列表已发布不分页
    List<YsBulletinVO> selectAll(@Param("status") Integer status, @Param("orderBy") String orderBy);


}