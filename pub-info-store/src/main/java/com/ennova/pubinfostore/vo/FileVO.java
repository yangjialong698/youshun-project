package com.ennova.pubinfostore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileVO {

    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "展示文件名")
    private String fileName;

    @ApiModelProperty(value = "真实文件名")
    private String newfileName;

    @ApiModelProperty(value = "压缩文档访问路径")
    private String ysFileUrl;

}
