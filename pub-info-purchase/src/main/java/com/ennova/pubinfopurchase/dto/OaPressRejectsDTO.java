package com.ennova.pubinfopurchase.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/4/3
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaPressRejectsDTO {

    /**
     * 不良品处理单id
     */
    @ApiModelProperty(value="不良品处理单id")
    private Integer rejectsId;

    /**
     * 最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )
     */
    @ApiModelProperty(value="最新步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )")
    private Integer setpStaus;
}
