package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaOpinionDTO {

    /**
     * 会签意见人id
     */
    @ApiModelProperty(value="会签意见人id")
    private Integer opinionUserId;

    /**
     * 会签意见人
     */
    @ApiModelProperty(value="会签意见人")
    private String opinionUser;


}
