package com.ennova.pubinfofile.entity;

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
@ApiModel(value = "意见建议表")
@Data
public class YsMasterFileEntity {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
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
     * 是否对外开放 0- 不对外开放  1- 对外开放
     */
    @ApiModelProperty(value = "是否对外开放 0- 不对外开放  1- 对外开放", example = "1")
    private Integer openFile;

    /**
     * 是否删除： 0- 保留  1- 删除
     */
    @ApiModelProperty(value = "是否删除： 0- 保留  1- 删除", example = "1")
    private Integer delFlag;

    /**
     * 文档类型ID
     */
    @ApiModelProperty(value = "文档类型ID", example = "1")
    private Integer ysFileTypeId;

    /**
     * 主任务ID
     */
    @ApiModelProperty(value = "主任务ID", example = "1")
    private Integer ysMasterTaskId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "子任务ID", example = "1")
    private Integer ysSonTaskId;

    /**
     * 主要内容
     */
    @ApiModelProperty(value = "主要内容", example = "1")
    private String fileContent;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "1")
    private Integer userId;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    public String versionNo;

    /**
     * 日报日期
     */
    @ApiModelProperty(value = "日报日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayrepTime;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
