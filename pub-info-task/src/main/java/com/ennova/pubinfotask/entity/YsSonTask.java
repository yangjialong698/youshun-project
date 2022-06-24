package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/13
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 子任务表
 */
@ApiModel(value = "子任务表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsSonTask {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String name;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID", example = "1")
    private Integer ysMasterTaskId;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急", example = "1")
    private Integer pressingLevel;

    /**
     * 任务状态： 0- 未开始  1- 进行中 2- 已完成 3- 已关闭
     */
    @ApiModelProperty(value = "任务状态： 0- 未开始  1- 进行中 2- 已完成 3- 已关闭", example = "1")
    private Integer status;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endTime;

    /**
     * 预计工时
     */
    @ApiModelProperty(value = "预计工时", example = "1")
    private Integer estimateWorkTime;

    /**
     * 团队成员ID
     */
    @ApiModelProperty(value = "团队成员ID", example = "1")
    private Integer ysTeamId;

    /**
     * 单位成本：  元／每小时
     */
    @ApiModelProperty(value = "单位成本：  元／每小时")
    private BigDecimal cost;

    /**
     * 认领人ID
     */
    @ApiModelProperty(value = "认领人ID", example = "1")
    private Integer receiveId;

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