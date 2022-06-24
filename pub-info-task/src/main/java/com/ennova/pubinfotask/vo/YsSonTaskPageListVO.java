package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/25
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */
/**
    * 子任务表
    */
@ApiModel(value="子任务分页列表")
@Data
@Builder
public class YsSonTaskPageListVO {

    @ApiModelProperty(value="ID", example = "1")
    private Integer id;

    @ApiModelProperty(value="子任务名称")
    private String name;

    @ApiModelProperty(value="执行人ID", example = "1")
    private Integer teamUserId;

    @ApiModelProperty(value="执行人名称")
    private String username;

    @ApiModelProperty(value="主任务名称")
    private String masterName;

    @ApiModelProperty(value="开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

    @ApiModelProperty(value="结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    @ApiModelProperty(value="预计工时", example = "1")
    private Integer estimateWorkTime;

    @ApiModelProperty(value="已使用", example = "1")
    private Integer totalConsume;

    @ApiModelProperty(value="认领人ID", example = "1")
    private Integer receiveId;

    @ApiModelProperty(value="任务状态： 0- 未开始  1- 进行中 2- 已完成 3- 已关闭", example = "1")
    private Integer status;

    @JsonIgnore
    @ApiModelProperty(value="紧急程度：0-  一般、1- 重要、2- 紧急", example = "1")
    private Integer pressingLevel;

    @ApiModelProperty(value = "按钮状态（是否存在封皮文件）： 0- 包含  1-不含", example = "1")
    private Integer buttonStatus;

    @ApiModelProperty(value = "文件类型id： 3- 经验建议 ", example = "1")
    private Integer ysFileTypeId;

}