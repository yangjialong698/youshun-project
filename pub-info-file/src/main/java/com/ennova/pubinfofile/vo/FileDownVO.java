package com.ennova.pubinfofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-28
 */
@ApiModel(value = "文档下载")
@Data
public class FileDownVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称")
    private String fjName;

    /**
     * 附件地址
     */
    @ApiModelProperty(value = "附件地址")
    private String fileUrl;


    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 附件大小
     */
    @ApiModelProperty(value = "附件大小")
    private String fileSize;


}
