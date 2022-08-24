package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询一条主任务详情
 */
@Data
public class TaskDetailVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private String serialNumber;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String name;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 任务类型： 0- 实验类  1- 研发类
     */
    @ApiModelProperty(value = "任务类型： 0- 实验类  1- 研发类", example = "1")
    private Integer type;

    /**
     * 任务状态： 0- 未发布 1- 已发布，待认领  2- 已认领，未开始  3- 进行中 4- 已完成 5- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未发布 1- 已发布，待认领  2- 已认领，未开始  3- 进行中 4- 已完成 5- 已关闭")
    private Integer status;

    /**
     * 任务简介
     */
    @ApiModelProperty(value = "任务简介")
    private String summary;

    /**
     * 任务成本
     */
    @ApiModelProperty(value = "任务成本")
    private Double cost;

    /**
     * 考核标准
     */
    @ApiModelProperty(value = "考核标准")
    private String checkStandard;

    /**
     * 预计工时
     */
    @ApiModelProperty(value = "预计工时", example = "1")
    private Integer estimateHour;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime taskEndDate;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime;



}
