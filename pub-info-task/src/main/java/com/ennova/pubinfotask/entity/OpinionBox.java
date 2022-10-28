package com.ennova.pubinfotask.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 意见箱
 */
@ApiModel(value = "意见箱")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpinionBox {
    @ApiModelProperty(value = "ID")
    private Integer id;

    /**
     * 发布人ID
     */
    @ApiModelProperty(value = "发布人ID")
    private Integer userId;

    /**
     * 匿名名称
     */
    @ApiModelProperty(value = "匿名名称")
    private String name;

    /**
     * 内容：2000汉字，每个汉字3个字节
     */
    @ApiModelProperty(value = "内容：2000汉字，每个汉字3个字节")
    private String content;

    /**
     * 意见箱附件ID
     */
    @ApiModelProperty(value = "意见箱附件ID")
    private String ysOpinionFileId;

    /**
     * 是否删除： 0- 保留  1- 删除
     */
    @ApiModelProperty(value = "是否删除： 0- 保留  1- 删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}