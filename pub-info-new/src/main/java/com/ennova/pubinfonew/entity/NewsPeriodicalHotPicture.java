package com.ennova.pubinfonew.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "期刊图片热区表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsPeriodicalHotPicture {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

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
}