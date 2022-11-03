package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * erp合格率表
    */
@ApiModel(value="erp合格率表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErpQualifiedRate {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 工单号
    */
    @ApiModelProperty(value="工单号")
    private String workOrderNo;

    /**
    * 品号
    */
    @ApiModelProperty(value="品号")
    private String prdNo;

    /**
    * 品名
    */
    @ApiModelProperty(value="品名")
    private String prdName;

    /**
    * 工单状态
    */
    @ApiModelProperty(value="工单状态")
    private String workOrderStatus;

    /**
    * 模块编号
    */
    @ApiModelProperty(value="模块编号")
    private String moduleNo;

    /**
    * 在制产品数量
    */
    @ApiModelProperty(value="在制产品数量")
    private Integer zzPrdCount;

    /**
    * 报废量
    */
    @ApiModelProperty(value="报废量")
    private Integer scrapCount;

    /**
    * 报废率
    */
    @ApiModelProperty(value="报废率")
    private String scrapRate;

    /**
    * 日期
    */
    @ApiModelProperty(value="日期")
    private Date createTime;
}