package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/17
 */
@Data
public class BadDisposalDTO {

    @ApiModelProperty(value = "不良处置")
    private String badDisposal;

}
