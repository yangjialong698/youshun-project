package com.ennova.pubinfofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-27
 */
@ApiModel(value = "文件类型统计")
@Data
public class FileTypeCou {

    /**
     * 文档名称
     */
    @ApiModelProperty(value = "文档名称")
    private String fileName;

    /**
     * 文档数量
     */
    @ApiModelProperty(value = "文档数量")
    private Integer count;

    /**
     * 文档占比
     */
    @ApiModelProperty(value = "文档占比")
    private Integer filePercent;


}
