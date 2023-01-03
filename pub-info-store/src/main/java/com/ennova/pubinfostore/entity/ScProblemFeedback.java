package com.ennova.pubinfostore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "问题反馈表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScProblemFeedback {
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 反馈人id
     */
    @ApiModelProperty(value = "反馈人id")
    private Integer backUserId;

    /**
     * 反馈人
     */
    @ApiModelProperty(value = "反馈人")
    private String backPerson;

    /**
     * 问题来源
     */
    @ApiModelProperty(value = "问题来源")
    private String backDepartment;

    /**
     * 反馈时间
     */
    @ApiModelProperty(value = "反馈时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 责任部门
     */
    @ApiModelProperty(value = "责任部门")
    private String dutyDepartment;

    /**
     * 责任人id
     */
    @ApiModelProperty(value = "责任人id")
    private String dutyPersonId;

    /**
     * 责任人
     */
    @ApiModelProperty(value = "责任人")
    private String dutyPerson;

    /**
     * 问题描述（内容：2000汉字，每个汉字2个字节）
     */
    @ApiModelProperty(value = "问题描述（内容：2000汉字，每个汉字2个字节）")
    private String problemDescription;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    /**
     * 反馈状态（0-未解决 1-已解决 2-待确认 3-待解决）
     */
    @ApiModelProperty(value = "反馈状态(0-未解决 1-已解决 2-待确认 3-待解决)")
    private String backStatus;

    /**
     * 问题原因
     */
    @ApiModelProperty(value = "问题原因")
    private String problemReason;

    /**
     * 解决措施
     */
    @ApiModelProperty(value = "解决措施")
    private String problemSolution;

    /**
     * 解决时间
     */
    @ApiModelProperty(value = "解决时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date solveTime;
}