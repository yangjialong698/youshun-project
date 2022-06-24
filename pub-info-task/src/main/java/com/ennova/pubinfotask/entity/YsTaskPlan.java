package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/29
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 任务计划
 */
@ApiModel(value = "任务计划")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsTaskPlan {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String name;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 紧急程度：0-  一般、1- 重要、2- 紧急
     */
    @ApiModelProperty(value = "紧急程度：0-  一般、1- 重要、2- 紧急")
    private Integer pressingLevel;

    /**
     * 计划状态：0- 未开始、1- 进行中、2- 已完成、3- 已关闭
     */
    @ApiModelProperty(value = "计划状态：0- 未开始、1- 已完成")
    private Integer planStatus;

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
     * 实际完成日期
     */
    @ApiModelProperty(value = "实际完成日期")
    private LocalDateTime actualTime;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;

    /**
     * 认领人ID
     */
    @ApiModelProperty(value = "认领人ID")
    private Integer receiveId;
}