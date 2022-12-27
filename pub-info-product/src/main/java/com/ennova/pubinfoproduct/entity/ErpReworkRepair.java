package com.ennova.pubinfoproduct.entity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返工返修表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpReworkRepair {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 反馈人id
     */
    @ApiModelProperty(value = "反馈人id")
    private Integer backUserId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplier;

    /**
     * 品号
     */
    @ApiModelProperty(value = "品号")
    private String productNo;

    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    private String productName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /**
     * 返工原因
     */
    @ApiModelProperty(value = "返工原因")
    private String reworkReason;

    /**
     * 返工工时
     */
    @ApiModelProperty(value = "返工工时")
    private Integer reworkHour;

    /**
     * 返工日期
     */
    @ApiModelProperty(value = "返工日期")
    private Date reworkTime;
}