package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报废损失表
 */
@ApiModel(description = "报废损失表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpScrapLoss {
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 工作中心
     */
    @ApiModelProperty(value = "工作中心")
    private String workCenterNo;

    /**
     * 平均小时成本含社保
     */
    @ApiModelProperty(value = "平均小时成本含社保")
    private Double hourCost;

    /**
     * 单件材料费
     */
    @ApiModelProperty(value = "单件材料费")
    private Double prdPerCost;

    /**
     * 单件刀具油辅料
     */
    @ApiModelProperty(value = "单件刀具油辅料")
    private Double toolOil;

    /**
     * 产品品号
     */
    @ApiModelProperty(value = "产品品号")
    private String prdNo;

    /**
     * 品名
     */
    @ApiModelProperty(value = "品名")
    private String prdName;

    /**
     * 单据日期
     */
    @ApiModelProperty(value = "单据日期")
    private String orderDate;

    /**
     * 总工时
     */
    @ApiModelProperty(value = "总工时")
    private Double workHours;

    /**
     * 白班工时
     */
    @ApiModelProperty(value = "白班工时")
    private Double dayWorkHours;

    /**
     * 夜班工时
     */
    @ApiModelProperty(value = "夜班工时")
    private Double nightWorkHours;

    /**
     * 报废数量
     */
    @ApiModelProperty(value = "报废数量")
    private Integer scrapNum;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 报废金额
     */
    @ApiModelProperty(value = "报废金额")
    private Double scrapCost;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    /**
     * 是否删除 ：0 - 未删除  1 - 已删除
     */
    @ApiModelProperty(value = "是否删除 ：0 - 未删除  1 - 已删除")
    private Integer delFlag;
}