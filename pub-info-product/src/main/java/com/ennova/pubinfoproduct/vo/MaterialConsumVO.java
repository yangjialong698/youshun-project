package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialConsumVO {

    @ApiModelProperty(value = "领用类型")
    private String consumKind;
    @ApiModelProperty(value = "领用日期")
    private Date consumTime;
    @ApiModelProperty(value = "领用单号")
    private String consumNo;
    @ApiModelProperty(value = "生产工单号")
    private String workOrderNo;
    @ApiModelProperty(value = "品名")
    private String prdName;
    @ApiModelProperty(value = "品号")
    private String prdNo;
    @ApiModelProperty(value = "数量")
    private Integer perAmount;
    @ApiModelProperty(value = "金额")
    private Integer amount;
//    @ApiModelProperty(value = "备注")
//    private String remarks;
}
