package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主任务表
 */
@ApiModel(value = "主任务表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsMasterTask {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", example = "1")
    private Integer serialNumber;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String name;

    /**
     * 任务类型： 0- 实验类  1- 研发类  2-制造类  3-其它
     */
    @ApiModelProperty(value = "任务类型： 0- 实验类  1- 研发类  2-制造类  3-其它", example = "1")
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
     * 任务成本，单位万元：1-9位整数
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
    private LocalDate taskEndDate;

    /**
     * 进度
     */
    @ApiModelProperty(value = "进度")
    private Integer rate;

    /**
     * 任务发布时间
     */
    @ApiModelProperty(value = "任务发布时间")
    private LocalDateTime publishDate;

    /**
     * 发布人ID
     */
    @ApiModelProperty(value = "发布人ID", example = "1")
    private Integer userId;

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

      /**
     * 主任务封皮ID
     */
    @ApiModelProperty(value = "主任务封皮ID", example = "1")
    private Integer ysMasterFileId;
}