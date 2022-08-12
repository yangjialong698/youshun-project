package com.ennova.pubinfodaily.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 非任务日报反馈表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsDailyFeedBack {
    /**
    * 日报反馈ID
    */
    private Integer id;

    /**
    * 日报ID
    */
    private Integer dailyRepId;

    /**
    * 反馈用户ID
    */
    private Integer userId;

    /**
    * 反馈内容
    */
    private String feedContent;

    /**
    * 反馈状态0未反馈1已反馈
    */
    private Integer feedStatus;

    /**
    * 反馈日期
    */
    private Date createTime;
}