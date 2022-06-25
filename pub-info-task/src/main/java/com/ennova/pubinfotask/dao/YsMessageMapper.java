package com.ennova.pubinfotask.dao;

import com.ennova.pubinfotask.entity.YsMessage;
import java.util.List;
import com.ennova.pubinfotask.vo.YsMessageVO;import org.apache.ibatis.annotations.Param;

public interface YsMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YsMessage record);

    int insertSelective(YsMessage record);

    YsMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YsMessage record);

    int updateByPrimaryKey(YsMessage record);

    int updateBatch(List<YsMessage> list);

    int batchInsert(@Param("list") List<YsMessage> list);

    List<YsMessageVO> selectByStatusAndYsBulletinLike(@Param("status") Boolean status, @Param("title") String title, @Param("receiveId") Integer receiveId);

    YsMessage selectBybulletinIdAndReceiveId(@Param("bulletinId")Integer bulletinId,@Param("receiveId")Integer receiveId);

    YsMessage selectByIdAndReceiveId(@Param("id")Integer id,@Param("receiveId")Integer receiveId);

    List<YsMessage> selectByReceiveIdAndStatus(@Param("receiveId")Integer receiveId,@Param("status")Boolean status);



}