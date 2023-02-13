package com.ennova.pubinfoproduct.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * erp_rework_repair不良返回信息对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefectRateVO {
    private String supplierNo;
    private Double defectRate;
}
