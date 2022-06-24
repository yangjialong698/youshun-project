package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/26
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 主任务文件表
 */
@ApiModel(value = "主任务文件表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YsMasterFile {
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
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    public String versionNo;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "1")
    private Integer userId;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;
}