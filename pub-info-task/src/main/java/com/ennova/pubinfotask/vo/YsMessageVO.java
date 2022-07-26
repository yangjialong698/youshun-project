package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className: YsMessageVO
 * @Description: 消息列表
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/22 14:05:53
 */
@ApiModel
@Data
@Builder
public class YsMessageVO {

    @ApiModelProperty(value = "ID")
    private int id;

    @ApiModelProperty(value = "消息名称")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息来源: 0-公告 1-供应商")
    private int sourceType;

    @ApiModelProperty(value = "发送人")
    private int createId;

    @ApiModelProperty(value = "发送人姓名")
    private String userName;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime checkTime;

    @ApiModelProperty(value = "状态: 0 - 未读(false)  1- 已读(true)")
    private Boolean status;

}