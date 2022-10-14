package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrdInfoVO {
    @ApiModelProperty(value = "品号")
    private String prdNo;
    @ApiModelProperty(value = "品名")
    private String prdName;
    @ApiModelProperty(value = "属性")
    private String prdProperty;
    @ApiModelProperty(value = "总库存")
    private Integer totalStock;
}
