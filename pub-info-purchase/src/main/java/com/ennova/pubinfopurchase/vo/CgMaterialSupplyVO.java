package com.ennova.pubinfopurchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/3/1
 * @version 1.0
 */
@ApiModel(value = "采购物料供需表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgMaterialSupplyVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
    * 供应商名称
    */
    @ApiModelProperty(value = "供应商名称")
    @Size(max = 255,message = "供应商名称最大长度要小于 255")
    private String supplierName;

    /**
    * 供应商品号
    */
    @ApiModelProperty(value = "供应商品号")
    @Size(max = 50,message = "供应商品号最大长度要小于 50")
    @NotBlank(message = "供应商品号(supplierNo): 不能为空!")
    private String supplierNo;

    /**
    * 品名
    */
    @ApiModelProperty(value = "品名")
    @Size(max = 255,message = "品名最大长度要小于 255")
    private String prdName;

    /**
    * 品号
    */
    @ApiModelProperty(value = "品号")
    @NotBlank(message = "品号(prdNo): 不能为空!")
    @Size(max = 50,message = "品号最大长度要小于 50")
    private String prdNo;

    /**
    * 规格
     *
    */
    @ApiModelProperty(value = "规格")
    @Size(max = 50,message = "规格最大长度要小于 50")
    private String spec;

    /**
    * 到货数量
    */
    @ApiModelProperty(value = "到货数量")
    private Integer deliveryNum;

    /**
    * 到货日期
    */
    @ApiModelProperty(value = "到货日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;

    /**
    * 请购未到数量
    */
    @ApiModelProperty(value = "请购未到数量")
    private Integer requestUndueNum;

    /**
     * 是否删除（0-未删除 1- 已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除 1- 已删除）")
    private Integer delFlag;
}