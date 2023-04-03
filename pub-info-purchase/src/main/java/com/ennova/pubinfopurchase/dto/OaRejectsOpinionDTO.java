package com.ennova.pubinfopurchase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/3/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaRejectsOpinionDTO {

    /**
     * 不良品处理单id
     */
    @ApiModelProperty(value="不良品处理单id")
    private Integer rejectsId;

    /**
     * 会签意见超时时间
     */
    @ApiModelProperty(value="会签意见超时时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime overtime;

    /**
     * 会签步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )
     */
    @ApiModelProperty(value="会签步骤(1-质量发起人， 2-生产部， 3-多部门，4-质量经理，5-最终处置意见 )")
    private Integer setpStaus;

    List<OaOpinionDTO> opinionDTOS;

}
