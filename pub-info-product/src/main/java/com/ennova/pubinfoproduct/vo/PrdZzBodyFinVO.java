package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrdZzBodyFinVO {
    @ApiModelProperty(value = "工单类别")
    private String workOrderKind;
    @ApiModelProperty(value = "工单单号")
    private String workOrderNo;
    @ApiModelProperty(value = "性质")
    private String  properties;
    @ApiModelProperty(value = "总投入数量")
    private Integer inputAmount;
    @ApiModelProperty(value = "机加-摇臂轴数量")
    private Integer jjybAmount;
    @ApiModelProperty(value = "机加后处理-摇臂轴数量")
    private Integer jjybHclAmount;
    @ApiModelProperty(value = "装配数量")
    private Integer assemblyAmount;
    @ApiModelProperty(value = "报废数量")
    private Integer scrappedAmount;
    @ApiModelProperty(value = "合计在产数量")
    private Integer onProduceAmount;
}
