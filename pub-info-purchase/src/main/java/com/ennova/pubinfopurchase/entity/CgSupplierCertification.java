package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 供应商认证表
 */
@ApiModel(value = "供应商认证表")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CgSupplierCertification {
    /**
     *供应商认证id
     */
    @ApiModelProperty(value = "供应商认证id")
    private Integer id;

    /**
     *供应商编号
     */
    @ApiModelProperty(value = "供应商编号")
    private Integer supplierNumber;

    /**
     *供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     *地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     *联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String phone;

    /**
     *填报人ID
     */
    @ApiModelProperty(value = "填报人ID")
    private Integer createUserId;

    /**
     *认证状态  * 0:待审核 1:审核通过(已发布) 2:审核不通过 - 驳回（待修改）
     */
    @ApiModelProperty(value = "认证状态  * 0:待审核 1:审核通过(已发布) 2:审核不通过 - 驳回（待修改）")
    private Integer status;

    /**
     *审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private Integer checkUserId;

    /**
     *审核日期
     */
    @ApiModelProperty(value = "审核日期")
    private Date checkTime;

    /**
     *填报日期
     */
    @ApiModelProperty(value = "填报日期")
    private Date createTime;

    /**
     *更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}