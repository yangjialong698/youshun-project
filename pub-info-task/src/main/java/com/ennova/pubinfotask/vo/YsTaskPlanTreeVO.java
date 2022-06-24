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
public class YsTaskPlanTreeVO {

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String name;


    /**
     * 计划名称
     */
    @ApiModelProperty(value = "状态")
    private Integer planStatus;


    /**
     * 实际完成日期
     */
    @ApiModelProperty(value = "计划完成或实际完成日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime actualTime;
}
