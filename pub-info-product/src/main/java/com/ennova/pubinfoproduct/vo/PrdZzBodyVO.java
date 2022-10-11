package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "货品自制表身")
@Data
public class PrdZzBodyVO {
    @ApiModelProperty(value = "序号")
    private String serialNumber;
    @ApiModelProperty(value = "工单类别")
    private String workOrderKind;
    @ApiModelProperty(value = "工单单号")
    private String workOrderNo;
    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;
    @ApiModelProperty(value = "加工顺序")
    private String processingSequence;
    @ApiModelProperty(value = "工艺")
    private String technology;
    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "投入数量")
    private Integer inputAmount;
    @ApiModelProperty(value = "完成数量")
    private Integer finishAmount;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapAmount;
    @ApiModelProperty(value = "待转数量")
    private Integer transferAmount;
    @ApiModelProperty(value = "在产品数量")
    private Integer leaveAmount;
}
