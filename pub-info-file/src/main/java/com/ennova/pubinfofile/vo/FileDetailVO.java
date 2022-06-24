package com.ennova.pubinfofile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-25
 */
@ApiModel(value = "文档详情")
@Data
public class FileDetailVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 文档编号
     */
    @ApiModelProperty(value = "文档编号")
    private String serialNumber;

    /**
     * 文档名称
     */
    @ApiModelProperty(value = "文档名称")
    private String fileName;

    /**
     * 主任务名称
     */
    @ApiModelProperty(value = "主任务名称", example = "1")
    private String masterTaskName;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID")
    private Integer ysMasterTaskId;

    /**
     * 子任务名称
     */
    @ApiModelProperty(value = "子任务名称", example = "1")
    private String sonTaskName;

    /**
     * 子任务ID
     */
    @ApiModelProperty(value = "子任务ID")
    private Integer ysSonTaskId;

    /**
     * 文档类型
     */
    @ApiModelProperty(value = "文档类型")
    private String fileType;

    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private String username;

    /**
     * 日报日期
     */
    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayrepTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    private Integer fileId;

    @ApiModelProperty(value = "类型前缀")
    private String filePrefix;

}
