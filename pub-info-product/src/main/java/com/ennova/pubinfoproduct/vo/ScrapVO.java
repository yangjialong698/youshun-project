package com.ennova.pubinfoproduct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ScrapVO {
    @ApiModelProperty(value="单据日期")
    private Date orderDate;
    @ApiModelProperty(value="入库量")
    private Integer totalNum;
    @ApiModelProperty(value="日产量")
    private Integer dayPrdNum;
    @ApiModelProperty(value="报废率")
    private Integer scrapRate;
}
