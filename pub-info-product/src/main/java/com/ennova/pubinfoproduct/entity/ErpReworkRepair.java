package com.ennova.pubinfoproduct.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返工返修表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpReworkRepair {
    /**
     * ID
     */
    private Integer id;

    /**
     * 反馈人id
     */
    private Integer backUserId;

    /**
     * 供应商编号
     */
    private String supplierNo;

    /**
     * 供应商名称
     */
    private String supplier;

    /**
     * 品号
     */
    private String productNo;

    /**
     * 品名
     */
    private String productName;

    /**
     * 返修数量
     */
    private Integer num;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 返工原因
     */
    private String reworkReason;

    /**
     * 返工工时
     */
    private Integer reworkHour;

    /**
     * 返工日期
     */
    private Date reworkTime;
}