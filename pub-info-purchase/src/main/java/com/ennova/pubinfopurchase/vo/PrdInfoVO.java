package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrdInfoVO {

    private Integer id;

    @ApiModelProperty(value = "品号")
    private String prdNo;

    @ApiModelProperty(value = "品名")
    private String prdName;

    @ApiModelProperty(value = "规格")
    private String spec;

    @ApiModelProperty(value = "单位")
    private String prdUnit;
}
