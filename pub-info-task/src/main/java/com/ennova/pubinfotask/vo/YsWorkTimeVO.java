package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
public class YsWorkTimeVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    ///**
    // * 子任务ID
    // */
    //@NotNull(message = "子任务ID不能为空")
    //@ApiModelProperty(value = "子任务ID")
    //private Integer ysSonTaskId;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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

    ///**
    // * 管理者ID
    // */
    //@NotNull(message = "管理者ID不能为空")
    //@ApiModelProperty(value = "管理者ID")
    //private Integer receiveId;
    //
    ///**
    // * 执行人ID
    // */
    //@NotNull(message = "执行人ID不能为空")
    //@ApiModelProperty(value = "执行人ID")
    //private Integer executorId;
    //
    //@NotNull(message = "子任务传递预计工时不能为空")
    //@ApiModelProperty(value = "子任务传递预计工时")
    //private Integer estimateWorkTime;

}