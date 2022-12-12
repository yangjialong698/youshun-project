package com.ennova.pubinfostore.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 问题反馈文件表
 */
@ApiModel(value = "问题反馈文件表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScProblemFile {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 问题反馈id
     */
    @ApiModelProperty(value = "问题反馈id")
    private Integer problemFeedbackId;

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
     * 压缩文档访问路径
     */
    @ApiModelProperty(value = "压缩文档访问路径")
    private String ysFileUrl;

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
    private Date createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    /**
     * 上传人ID
     */
    @ApiModelProperty(value = "上传人ID")
    private Integer userId;

    /**
     * 文件类型（0-反馈问题类型 1-解决问题类型）
     */
    @ApiModelProperty(value = "文件类型（0-反馈问题类型 1-解决问题类型）")
    private Integer fileType;
}