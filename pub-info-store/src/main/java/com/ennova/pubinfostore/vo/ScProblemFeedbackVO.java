package com.ennova.pubinfostore.vo;

import com.ennova.pubinfostore.entity.ScProblemFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 问题反馈表
 */
@ApiModel(value = "问题反馈表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScProblemFeedbackVO {

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
    private String backUserName;

    /**
     * 问题来源
     */
    @ApiModelProperty(value="问题来源")
    private String backDepartment;

    /**
     * 反馈时间
     */
    @ApiModelProperty(value = "反馈时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 责任部门id
     */
    @ApiModelProperty(value = "责任部门id")
    private String dutyDepartmentId;

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
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    /**
     * 反馈状态（0-未解决 1-已解决）
     */
    @ApiModelProperty(value = "反馈状态（0-未解决 1-已解决）")
    private Integer backStatus;

    @ApiModelProperty(value = "附件ID")
    private List<Integer> scProblemFileId;

    /**
     * 问题反馈附件
     */
    @ApiModelProperty(value = "上传的附件：仅用于列表展示")
    private List<ScProblemFile> fileVOList;
}