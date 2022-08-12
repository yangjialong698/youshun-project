package com.ennova.pubinfodaily.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class FileVO {

    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "真实文件名")
    private String newfileName;

    @ApiModelProperty(value = "原文件名")
    private String fileName;

    private Integer  fileMasterId;

}
