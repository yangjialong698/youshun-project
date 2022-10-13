package com.ennova.pubinfoproduct.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "货品表头")
@Data
public class PrdDetailHeadVO {
    @ApiModelProperty(value = "品号")
    private String prdNo;
    @ApiModelProperty(value = "品名")
    private String prdName;
    @ApiModelProperty(value = "属性")
    private String prdProperty;
    @ApiModelProperty(value = "规格")
    private String specs;
    @ApiModelProperty(value = "总库存")
    private Integer totalStock;

    @ApiModelProperty(value = "采购未交数量")
    private Integer cgUnHandOrder;
    @ApiModelProperty(value = "采购待检数量")
    private Integer cgUnCheckCount;
    @ApiModelProperty(value = "在制产品数量")
    private Integer zzPrdCount;
}
