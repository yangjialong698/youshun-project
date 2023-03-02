package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/3/1
 * @version 1.0
 */
@ApiModel(description="采购物料供需表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgMaterialSupply {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
    * 供应商名称
    */
    @ApiModelProperty(value="供应商名称")
    private String supplierName;

    /**
    * 供应商品号
    */
    @ApiModelProperty(value="供应商品号")
    private String supplierNo;

    /**
    * 品名
    */
    @ApiModelProperty(value="品名")
    private String prdName;

    /**
    * 品号
    */
    @ApiModelProperty(value="品号")
    private String prdNo;

    /**
    * 规格
    */
    @ApiModelProperty(value="规格")
    private String spec;

    /**
    * 到货数量
    */
    @ApiModelProperty(value="到货数量")
    private Integer deliveryNum;

    /**
    * 到货日期
    */
    @ApiModelProperty(value="到货日期")
    private LocalDateTime deliveryDate;

    /**
    * 请购未到数量
    */
    @ApiModelProperty(value="请购未到数量")
    private Integer requestUndueNum;

    /**
    * 是否删除（0-未删除 1- 已删除）
    */
    @ApiModelProperty(value="是否删除（0-未删除 1- 已删除）")
    private Integer delFlag;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
}