package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "货品自制表身")
@Data
public class PrdZzBodyVO {
    @ApiModelProperty(value = "品号")
    private String prdNo;
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
    @ApiModelProperty(value = "合计在产数量")
    private Integer onProduceAmount;
    @ApiModelProperty(value = "完成数量")
    private Integer finishAmount;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapAmount;
    @ApiModelProperty(value = "待转数量")
    private Integer transferAmount;
    @ApiModelProperty(value = "供应商代号")
    private String supplierNo;
    @ApiModelProperty(value = "在产品数量")
    private Integer leaveAmount;
    @ApiModelProperty(value = "加工顺序")
    private String processingSequence;
}
