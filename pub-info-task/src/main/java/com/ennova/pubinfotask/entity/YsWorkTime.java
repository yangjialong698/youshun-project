package com.ennova.pubinfotask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/5
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 报工表
 */
@ApiModel(value = "报工表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsWorkTime {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 子任务ID
     */
    @ApiModelProperty(value = "子任务ID")
    private Integer ysSonTaskId;

    /**
     * 日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private LocalDateTime takeTime;

    /**
     * 总耗时
     */
    @ApiModelProperty(value = "总耗时")
    private Integer totalConsume;

    /**
     * 预计
     */
    @ApiModelProperty(value = "预计")
    private Integer surplus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 管理者ID
     */
    @ApiModelProperty(value = "管理者ID")
    private Integer receiveId;

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
}