package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description="scrap_per_outno")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapPerOutno {
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 单据日期
    */
    @ApiModelProperty(value="单据日期")
    private String orderDate;

    /**
    * 日产量
    */
    @ApiModelProperty(value="日产量")
    private Integer dayPrdNum;

    /**
    * 报废数量
    */
    @ApiModelProperty(value="报废数量")
    private Integer scrapNum;

    /**
    * 不良数量
    */
    @ApiModelProperty(value="不良数量")
    private Integer badNum;

    /**
    * 不良及报废率
    */
    @ApiModelProperty(value="不良及报废率")
    private String badScrapRate;

    /**
    * 工作中心
    */
    @ApiModelProperty(value="工作中心")
    private String moveOutNo;

    /**
    * 报废金额
    */
    @ApiModelProperty(value="报废金额")
    private Double scrapCost;
}