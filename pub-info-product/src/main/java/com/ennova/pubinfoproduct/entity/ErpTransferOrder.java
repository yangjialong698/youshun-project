package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "erp_transfer_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpTransferOrder {
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据日期")
    private String orderDate;

    /**
     * 转移单号
     */
    @ApiModelProperty(value = "转移单号")
    private String transferOrderNo;

    /**
     * 移出编号
     */
    @ApiModelProperty(value = "移出编号")
    private String moveOutNo;

    /**
     * 移入编号
     */
    @ApiModelProperty(value = "移入编号")
    private String moveInNo;

    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private String workOrderNo;

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
     * 验收数量
     */
    @ApiModelProperty(value = "验收数量")
    private Integer acceptanceNum;

    /**
     * 报废数量
     */
    @ApiModelProperty(value = "报废数量")
    private Integer scrapNum;

    /**
     * 不良数量
     */
    @ApiModelProperty(value = "不良数量")
    private Integer badNum;

    /**
     * 总量：验收量+报废量
     */
    @ApiModelProperty(value = "总量：验收量+报废量")
    private Integer totalNum;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}