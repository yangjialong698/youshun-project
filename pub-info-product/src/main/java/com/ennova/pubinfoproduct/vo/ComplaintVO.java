package com.ennova.pubinfoproduct.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintVO {
    private String supplierNo;
    private Double countNum;

    private Double defectRate;
    private String supplierName;
}
