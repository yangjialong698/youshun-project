package com.ennova.pubinfofile.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 经验建议评论表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsSugComment {
    /**
    * 经验建议评论ID
    */
    private Integer id;

    /**
    * 经验建议ID
    */
    private Integer expSugId;

    /**
    * 评论用户ID
    */
    private Integer userId;

    /**
    * 评论内容
    */
    private String sugContent;

    /**
    * 评论状态0未评论1已评论
    */
    private Integer sugStatus;

    /**
    * 评论日期
    */
    private Date createTime;
}