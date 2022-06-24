package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/17
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@ApiModel(value = "执行人工时占比")
@Builder
@Data
public class WorkTimeChartVO {

    @ApiModelProperty(value = "执行人ID")
    private Integer executorId;

    @ApiModelProperty(value = "执行人名字")
    private String executorName;

    @ApiModelProperty(value = "总工时")
    private Integer total;

    @ApiModelProperty(value = "消耗")
    private Integer sumConsume;

    @ApiModelProperty(value = "占比")
    private Integer ratio;


}
