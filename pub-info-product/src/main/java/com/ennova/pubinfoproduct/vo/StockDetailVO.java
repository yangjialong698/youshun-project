package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockDetailVO {
//    @ApiModelProperty(value = "仓库编号")
//    private String WareNo;
    @ApiModelProperty(value = "仓库名称")
    private String WareHouse;
    @ApiModelProperty(value = "仓库数量")
    private Integer WareCount;
}
