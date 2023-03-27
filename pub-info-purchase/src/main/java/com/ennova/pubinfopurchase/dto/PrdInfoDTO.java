package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/17
 */
@Data
public class PrdInfoDTO {

    @ApiModelProperty(value = "工单号")
    private String workOrderNo;

    @ApiModelProperty(value = "品号")
    private String prdNo;

    @ApiModelProperty(value = "品名")
    private String prdName;
}
