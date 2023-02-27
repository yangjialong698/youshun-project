package com.ennova.pubinfoproduct.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报废损失表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpScrapLoss {
    private Integer id;

    /**
     * 工作中心
     */
    private String workCenterNo;

    /**
     * 平均小时成本含社保
     */
    private Double hourCost;

    /**
     * 单件材料费
     */
    private Double prdPerCost;

    /**
     * 单件刀具油辅料
     */
    private Double toolOil;

    /**
     * 产品品号
     */
    private String prdNo;

    /**
     * 品名
     */
    private String prdName;

    /**
     * 单据日期
     */
    private String orderDate;

    /**
     * 工时
     */
    private Double workHours;

    /**
     * 报废数量
     */
    private Integer scrapNum;

    /**
     * 报废金额
     */
    private Double scrapCost;

    /**
     * 是否删除 ：0 - 未删除  1 - 已删除
     */
    private Integer delFlag;
}