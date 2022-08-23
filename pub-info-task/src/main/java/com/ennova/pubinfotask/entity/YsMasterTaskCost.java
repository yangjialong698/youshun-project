package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "ys_master_task_cost")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsMasterTaskCost {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 成本录入日期
     */
    @ApiModelProperty(value = "成本录入日期")
    private Date costDate;

    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    private Double cost;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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