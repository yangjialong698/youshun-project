package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaBathRejectsDeleteDTO {

    /**
     * 不良品处理单id
     */
    @ApiModelProperty(value="不良品处理单id")
    private Integer[] rejectsIds;

}
