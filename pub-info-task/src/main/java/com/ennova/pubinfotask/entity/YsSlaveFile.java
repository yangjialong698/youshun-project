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
 * @Date: 2022/5/19
 * @Description: com.ennova.pubinfotask.entity
 * @Version: 1.0
 */

/**
 * 子任务文件表
 */
@ApiModel(value = "子任务文件表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YsSlaveFile {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 文档编号
     */
    @ApiModelProperty(value = "文档编号")
    private Integer fileMasterId;

    /**
     * 原文件名
     */
    @ApiModelProperty(value = "原文件名")
    private String name;

    /**
     * MD5
     */
    @ApiModelProperty(value = "MD5")
    private String fileMd5;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    /**
     * 文档访问路径
     */
    @ApiModelProperty(value = "文档访问路径")
    private String fileUrl;

    /**
     * 是否删除： 0- 保留  1- 删除
     */
    @ApiModelProperty(value = "是否删除： 0- 保留  1- 删除")
    private Integer delFlag;

    /**
     * 是否对外开放 0- 不对外开放  1- 对外开放
     */
    @ApiModelProperty(value = "是否对外开放 0- 不对外开放  1- 对外开放")
    private Integer openFile;

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

    /**
     * 上传人ID
     */
    @ApiModelProperty(value = "上传人ID")
    private Integer userId;
}