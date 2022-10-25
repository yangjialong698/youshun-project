package com.ennova.pubinfoproduct.dao;

import com.ennova.pubinfoproduct.vo.ProduceDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProduceMapper {

    List<ProduceDetailVO> selectProduceList(@Param("startTime")String startTime, @Param("endTime")String endTime,
                                            @Param("processNo")String processNo, @Param("workOrderNo")String workOrderNo);


    List<Integer> selOrderNumByOrderNo(String workOrderNo);
}
