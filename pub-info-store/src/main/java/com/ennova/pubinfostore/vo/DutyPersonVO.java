package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="责任人")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyPersonVO {
    @ApiModelProperty(value="责任人id")
    private Long id;

    @ApiModelProperty(value="责任人名称")
    private String username;
}
