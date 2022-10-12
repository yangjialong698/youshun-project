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
    @ApiModelProperty(value = "安全库存")
    private Integer safeStock;
    @ApiModelProperty(value = "总库存")
    private Integer totalStock;
    @ApiModelProperty(value = "外点仓名称")
    private String outPointStore;
    @ApiModelProperty(value = "采购订单数量")
    private Integer cgOrder;
    @ApiModelProperty(value = "在制订单数量")
    private Integer zzOrder;
}
