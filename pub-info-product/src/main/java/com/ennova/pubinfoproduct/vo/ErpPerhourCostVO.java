package com.ennova.pubinfoproduct.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpPerhourCostVO {

    /**
     * 工作中心
     */
    private String workCenterNo;

    /**
     * 平均小时成本含社保
     */
    private Double hourCost;
}
