package com.ennova.pubinfofile.entity;

import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 日报反馈表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsFeedBack {
    /**
    * 日报反馈ID
    */
    @ApiModelProperty(value = "日报反馈ID")
    private Integer id;

    /**
    * 日报ID
    */
    @ApiModelProperty(value = "日报ID")
    private Integer dayRepId;

    /**
    * 反馈用户ID
    */
    @ApiModelProperty(value = "反馈用户ID")
    private Integer userId;

    /**
    * 反馈内容
    */
    @ApiModelProperty(value = "反馈内容")
    private String feedContent;

    /**
     * 反馈状态
     */
    @ApiModelProperty(value = "反馈状态")
    private Integer feedStatus;

    /**
    * 反馈日期
    */
    @ApiModelProperty(value = "反馈日期")
    private Date createTime;
}