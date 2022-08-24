package com.ennova.pubinfonew.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/8/11
 */
@Data
@Builder
public class FileDto {

    @ApiModelProperty(value = "附件ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "期数", example = "1")
    private Integer periodicalNum;

    @ApiModelProperty(value = "版数", example = "1")
    private Integer editionNum;
}
