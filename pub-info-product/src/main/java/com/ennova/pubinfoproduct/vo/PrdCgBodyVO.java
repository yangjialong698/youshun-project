package com.ennova.pubinfoproduct.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "货品采购表身")
@Data
public class PrdCgBodyVO {

    @ApiModelProperty(value = "采购单别")
    private String purchaseKind;
    @ApiModelProperty(value = "采购单号")
    private String purchaseNumber;
    @ApiModelProperty(value = "进货单号")
    private String stockNumber;
    @ApiModelProperty(value = "采购日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseTime;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "采购数量")
    private Integer purchaseAmount;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "已交数量")
    private Integer deliveredAmount;
    @ApiModelProperty(value = "待检数量")
    private Integer unCheckAmount;
    @ApiModelProperty(value = "未交数量")
    private Integer undeliveredAmount ;
}
