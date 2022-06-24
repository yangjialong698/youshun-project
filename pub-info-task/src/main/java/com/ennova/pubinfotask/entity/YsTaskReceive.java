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
@ApiModel(value = "任务认领中间表")
@Data
@Builder
public class YsTaskReceive {
    @ApiModelProperty(value = "id", example = "1")
    private Integer id;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID", example = "1")
    private Integer ysMasterTaskId;

    /**
     * 认领人ID
     */
    @ApiModelProperty(value = "认领人ID", example = "1")
    private Integer receiveId;

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