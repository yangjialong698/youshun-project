package com.ennova.pubinfonew.vo;

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
public class FileVO {

    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "展示文件名")
    private String fileName;

    @ApiModelProperty(value = "真实文件名")
    private String newfileName;

}
