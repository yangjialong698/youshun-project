package com.ennova.pubinfostore.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 问题反馈表
    */
@ApiModel(value="问题反馈表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScProblemFeedback {

    @ApiModelProperty(value="")
    private Integer id;

    /**
    * 反馈人id
    */
    @ApiModelProperty(value="反馈人id")
    private Integer backUserId;

    /**
    * 问题来源
    */
    @ApiModelProperty(value="问题来源")
    private String backDepartment;

    /**
    * 反馈时间
    */
    @ApiModelProperty(value="反馈时间")
    private Date createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value="修改时间")
    private Date updateTime;

    /**
    * 责任部门
    */
    @ApiModelProperty(value="责任部门")
    private String dutyDepartment;

    /**
    * 责任人
    */
    @ApiModelProperty(value="责任人")
    private String dutyPerson;

    /**
    * 问题描述
    */
    @ApiModelProperty(value="问题描述")
    private String problemDescription;

    /**
     * 反馈状态（0-未解决 1-已解决）
     */
    @ApiModelProperty(value = "反馈状态（0-未解决 1-已解决）")
    private Integer backStatus;

    /**
    * 是否删除
    */
    @ApiModelProperty(value="是否删除")
    private Integer delFlag;
}