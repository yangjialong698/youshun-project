package com.ennova.pubinfoproduct.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 报废损失表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpScrLossDtlVO {
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
     * 总工时
     */
    private Double workHours;

    /**
     * 白班工时
     */
    private Double dayWorkHours;

    /**
     * 夜班工时
     */
    private Double nightWorkHours;

    /**
    * 报废数量
    */
    private Integer scrapNum;

    /**
     * 白班备注
     */
    private String remarks;

    /**
     * 晚班备注
     */
    private String nightRemarks;

    /**
     * 完整备注（早班+晚班）
     */
    private String totalRemarks;

    /**
    * 报废金额
    */
    private Double scrapCost;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 是否删除 ：0 - 未删除  1 - 已删除
     */
    private Integer delFlag;
}