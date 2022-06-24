package com.ennova.pubinfofile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-05-05
 */

@ApiModel(value = "文档修改详情")
@Data
public class ModifyFileVO {

    @ApiModelProperty(value = "封皮ID")
    private Integer id;

    @ApiModelProperty(value = "文档名称/名称")
    private String fileName;

    @ApiModelProperty(value = "主任务名称")
    private String masterTaskName;

    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    @ApiModelProperty(value = "文档类型ID")
    private Integer ysFileTypeId;

    @ApiModelProperty(value = "文档类型名称")
    private String ysFileTypeName;

    @ApiModelProperty(value = "子任务名称")
    private String sonTaskName;

    @ApiModelProperty(value = "子任务ID")
    private Integer ysSonTaskId;

    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

    @ApiModelProperty(value = "版本号")
    private String versionNo;

    @ApiModelProperty(value = "文档类型前缀")
    private String filePrefix;

    @ApiModelProperty(value = "删除类型")
    private Integer type;

    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayrepTime;

    private List<FileVO> fileVOList;


}
