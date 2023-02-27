package com.ennova.pubinfoproduct.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 工作中心产品费用表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpPrdCost {
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
}