package com.ennova.pubinfotask.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author yangjialong
 * @CreateTime: 2022/5/20
 * @Description: com.ennova.pubinfotask.vo
 * @Version 1.0
 */
@ApiModel(value = "执行人剩余工时和结束日期")
@Builder
@Data
public class WorkTimeResidueVO {

    @ApiModelProperty(value = "总工时")
    private Integer total;

    @ApiModelProperty(value = "剩余工时")
    private Integer ResidueTime;

    @ApiModelProperty(value = "结束日期")
    private LocalDate taskEndDate;

}
