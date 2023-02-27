package com.ennova.pubinfoproduct.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpPrdNameVO {

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
     * 产品品名
     */
    private String prdName;
}
