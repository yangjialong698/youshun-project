package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 货品属性表
    */
@ApiModel(value="货品属性表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErpPrdInfo {
    /**
    * ID
    */
    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 品号
    */
    @ApiModelProperty(value="品号")
    private String prdNo;

    /**
    * 品名
    */
    @ApiModelProperty(value="品名")
    private String prdName;

    /**
    * 规格
    */
    @ApiModelProperty(value="规格")
    private String spec;

    /**
    * 单位
    */
    @ApiModelProperty(value="单位")
    private String prdUnit;
}