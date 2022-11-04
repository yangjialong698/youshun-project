package com.ennova.pubinfoproduct.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class ScrapVO {
    @ApiModelProperty(value="单据日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String orderDate;
    @ApiModelProperty(value="入库量")
    private Integer totalNum;
    @ApiModelProperty(value="日产量")
    private Integer dayPrdNum;
    @ApiModelProperty(value="报废率")
    private Double scrapRate;
}
