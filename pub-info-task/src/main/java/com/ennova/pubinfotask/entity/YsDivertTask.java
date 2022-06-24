package com.ennova.pubinfotask.entity;

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
 * 主任务移交表
 */
@ApiModel(value = "主任务移交表")
@Data
@Builder
public class YsDivertTask {
    @ApiModelProperty(value = "id", example = "1")
    private Integer id;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID", example = "1")
    private Integer ysMasterTaskId;

    /**
     * 移交后用户ID
     */
    @ApiModelProperty(value = "移交后用户ID", example = "1")
    private Integer divertUserId;

    /**
     * 移交前用户ID
     */
    @ApiModelProperty(value = "移交前用户ID", example = "1")
    private Integer receiveId;

    /**
     * 移交日期
     */
    @ApiModelProperty(value = "移交日期")
    private LocalDateTime divertTime;

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
}