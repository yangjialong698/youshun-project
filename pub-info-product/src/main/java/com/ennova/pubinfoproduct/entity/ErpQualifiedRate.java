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
    * 日期
    */
    @ApiModelProperty(value="日期")
    private Date createTime;

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
    * 在制产品数量
    */
    @ApiModelProperty(value="在制产品数量")
    private Integer zzPrdCount;

    /**
    * 合格率
    */
    @ApiModelProperty(value="合格率")
    private String qualifiedRate;
}