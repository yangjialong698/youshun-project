package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
 */

/**
 * 采购信息表
 * */
@ApiOperation(value = "采购信息表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CgPurchaseInfo {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty(value = "采购编号", example = "1")
    private Integer serialNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String name;

    /**
     * 申请人
     */
    @ApiModelProperty(value = "请购人")
    private String applyName;

    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务编号")
    private String taskNumber;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 采购需求
     */
    @ApiModelProperty(value = "采购需求")
    private String purchaseRequirements;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 发布人id
     */
    @ApiModelProperty(value = "发布人id", example = "1")
    private Integer issuerId;

    /**
     * 到货日期
     */
    @ApiModelProperty(value = "到货日期")
    private Date deliveryTime;
}
