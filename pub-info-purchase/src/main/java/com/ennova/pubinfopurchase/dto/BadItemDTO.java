package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/17
 */
@Data
public class BadItemDTO {

    @ApiModelProperty(value = "不良品项目")
    private String badItem;

}
