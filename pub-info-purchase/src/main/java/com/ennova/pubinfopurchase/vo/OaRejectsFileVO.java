package com.ennova.pubinfopurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author  yangjialong
 * @date  2023/4/11
 * @version 1.0
 */
@ApiModel(description="不合格处理单文件表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaRejectsFileVO {

    /**
    * ID
    */
    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 不良品处理单id
    */
    @ApiModelProperty(value="不良品处理单id")
    private Integer rejectsId;

    /**
    * 原文件名
    */
    @ApiModelProperty(value="原文件名")
    private String name;

    /**
    * MD5
    */
    @ApiModelProperty(value="MD5")
    private String fileMd5;

    /**
    * 文件大小
    */
    @ApiModelProperty(value="文件大小")
    private String fileSize;

    /**
    * 文档访问路径
    */
    @ApiModelProperty(value="文档访问路径")
    private String fileUrl;

    /**
    * 是否删除： 0- 保留  1- 删除
    */
    @ApiModelProperty(value="是否删除： 0- 保留  1- 删除")
    private Integer delFlag;

    /**
    * 是否对外开放 0- 不对外开放  1- 对外开放
    */
    @ApiModelProperty(value="是否对外开放 0- 不对外开放  1- 对外开放")
    private Integer openFile;

    /**
    * 创建日期
    */
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;

    /**
    * 更新日期
    */
    @ApiModelProperty(value="更新日期")
    private LocalDateTime updateTime;

    /**
    * 上传人ID
    */
    @ApiModelProperty(value="上传人ID")
    private Integer userId;
}