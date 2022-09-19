package com.ennova.pubinfotask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "SortMasterRateVO", description = "成本明细")
@Data
public class CostDetailVO {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "成本名称")
    private String name;
    @ApiModelProperty(value = "负责人")
    private String username;
    @ApiModelProperty(value = "科目")
    private String suject;
    @ApiModelProperty(value = "金额")
    private Double cost;
    @ApiModelProperty(value = "成本时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date costDate;
    @ApiModelProperty(value = "备注")
    private String remark;
}


