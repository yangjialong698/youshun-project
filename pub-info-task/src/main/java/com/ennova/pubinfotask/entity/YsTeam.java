package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 团队管理
 */
@ApiModel(value = "团队管理")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsTeam {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 任务组：1 非任务组：0
     */
    @ApiModelProperty(value = "任务组：1 非任务组：0")
    private Integer taskFlag;

    /**
     * 成本：  元/每小时
     */
    @ApiModelProperty(value = "成本：  元/每小时")
    private Double cost;

    /**
     * 团队管理者ID
     */
    @ApiModelProperty(value = "团队管理者ID")
    private Integer userId;

    /**
     * 执行人ID
     */
    @ApiModelProperty(value = "执行人ID")
    private Integer executorId;

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
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 是否删除（0-未删除；1-已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除；1-已删除）")
    private Integer isDelete;
}