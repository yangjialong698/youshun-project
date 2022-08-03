package com.ennova.pubinfofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/6/30
 */
@ApiModel(value = "近十天日报统计")
@Data
public class DataReportVO {

    /**
     * 日报数量
     */
    @ApiModelProperty(value = "id")
    private Long dreportNum;

    /**
     * 日报日期
     */
    @ApiModelProperty(value = "日报日期")
    private String dayrepTime;
}
