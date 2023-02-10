package com.ennova.pubinfoproduct.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商评价表
 */
@ApiModel(description = "供应商评价表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEvaluation {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private Integer supplierNo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 来料不良率
     */
    @ApiModelProperty(value = "来料不良率")
    private Integer incomingDefectiveRate;

    /**
     * 到货及时性
     */
    @ApiModelProperty(value = "到货及时性")
    private Integer timelyDelivery;

    /**
     * 返工返修率
     */
    @ApiModelProperty(value = "返工返修率")
    private Double reworkRate;

    /**
     * 配合度
     */
    @ApiModelProperty(value = "配合度")
    private Integer cooperation;

    /**
     * 来料短缺量
     */
    @ApiModelProperty(value = "来料短缺量")
    private Integer incomingShortage;

    /**
     * 客诉
     */
    @ApiModelProperty(value = "客诉")
    private Integer customerComplaints;

    /**
     * 汇总
     */
    @ApiModelProperty(value = "汇总")
    private Double summary;

    /**
     * 评价时间
     */
    @ApiModelProperty(value = "评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date evaluationTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}