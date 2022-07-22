package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息列表
 */
@ApiModel(value = "消息列表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsMessage {
    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    private Integer id;

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    private Integer ysBulletin;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value = "供应商ID")
    private Integer CgSupplier;

    /**
     * 接收人ID
     */
    @ApiModelProperty(value = "接收人ID")
    private Integer receiveId;

    /**
     * 消息来源：0 - 公告 , 1 - 供应商认证
     */
    @ApiModelProperty(value = "消息来源：0 - 公告 , 1 - 供应商认证")
    private Integer sourceType;

    /**
     * false - 未读  true- 已读
     */
    @ApiModelProperty(value = "false - 未读  true - 已读")
    private Boolean status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}