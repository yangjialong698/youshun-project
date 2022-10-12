package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 采购信息表
 */
@ApiModel(value = "采购信息表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgPurchaseInfo {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 采购编号
     */
    @ApiModelProperty(value = "采购编号")
    private Integer serialNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String name;

    /**
     * 请购人
     */
    @ApiModelProperty(value = "请购人")
    private String applyName;

    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务编号")
    private String taskNumber;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期")
    private Date createTime;

    /**
     * 采购需求
     */
    @ApiModelProperty(value = "采购需求")
    private String purchaseRequirements;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    /**
     * 发布人id
     */
    @ApiModelProperty(value = "发布人id")
    private Integer issuerId;

    /**
     * 到货日期
     */
    @ApiModelProperty(value = "到货日期")
    private Date deliveryTime;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long deptId;
}