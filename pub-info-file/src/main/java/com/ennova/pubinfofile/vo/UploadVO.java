package com.ennova.pubinfofile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@ApiModel(value = "文件上传封装参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadVO {

     /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    /**
     * 文档名称
     */
    @ApiModelProperty(value = "主任务名称")
    private String fileName;


    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "文档类型ID", example = "1")
    private Integer ysFileTypeId;

    /**
     * 主任务ID
     */
    private Integer ysMasterTaskId;

    /**
     * 子任务ID
     */
    private Integer ysSonTaskId;

    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "前缀", example = "1")
    private String filePrefix;

    /**
     * 主要内容
     */
    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    public String versionNo;




}
