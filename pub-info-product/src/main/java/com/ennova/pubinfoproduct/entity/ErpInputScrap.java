package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * erp入库报废量
    */
@ApiModel(value="erp入库报废量")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErpInputScrap {
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
    * 入库数量
    */
    @ApiModelProperty(value="入库数量")
    private Integer inputPrdCount;

    /**
    * 报废数量
    */
    @ApiModelProperty(value="报废数量")
    private Integer scrapPrdCount;
}