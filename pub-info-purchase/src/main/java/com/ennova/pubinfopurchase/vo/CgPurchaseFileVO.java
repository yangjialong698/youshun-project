package com.ennova.pubinfopurchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/14
 */
@Data
@Builder
public class CgPurchaseFileVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 采购信息编号
     */
    @ApiModelProperty(value = "采购信息ID")
    private Integer purchaseInfoId;

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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 上传人ID
     */
    @ApiModelProperty(value = "上传人ID")
    private Integer userId;
}
