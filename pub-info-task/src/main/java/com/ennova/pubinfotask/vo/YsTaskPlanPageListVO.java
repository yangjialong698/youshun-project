package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/7
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@Data
public class YsTaskPlanPageListVO {

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
    @ApiModelProperty(value = "主任务名")
    private String masterTaskName;

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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    /**
     * 实际完成日期
     */
    @ApiModelProperty(value = "实际完成日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime actualTime;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}

