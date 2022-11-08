package com.ennova.pubinfoproduct.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class ScrapVO {
    @ApiModelProperty(value="单据日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String orderDate;
    @ApiModelProperty(value="报废量")
    private Integer scrapNum;
    @ApiModelProperty(value="日产量")
    private Integer dayPrdNum;
    @ApiModelProperty(value="不良数")
    private Integer badNum;
    @ApiModelProperty(value="不良及报废率")
    private String badScrapRate;
}
