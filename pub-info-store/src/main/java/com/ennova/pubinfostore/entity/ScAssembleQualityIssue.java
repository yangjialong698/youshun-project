package com.ennova.pubinfostore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 装配质量异常表
    */
@ApiModel(value="装配质量异常表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScAssembleQualityIssue {

    @ApiModelProperty(value="ID")
    private Integer id;

    /**
    * 产品名称
    */
    @ApiModelProperty(value="产品名称")
    private String productName;

    /**
    * 品号
    */
    @ApiModelProperty(value="品号")
    private String productNumber;

    /**
    * 不良描述
    */
    @ApiModelProperty(value="不良描述")
    private String badDescription;

    /**
    * 不良数量
    */
    @ApiModelProperty(value="不良数量")
    private Integer badNumber;

    /**
    * 责任单位
    */
    @ApiModelProperty(value="责任单位")
    private String dutyUnit;

    /**
    * 责任人
    */
    @ApiModelProperty(value="责任人")
    private String dutyPerson;

    /**
    * 质量判定结果
    */
    @ApiModelProperty(value="质量判定结果")
    private String qualityStatus;

    /**
    * 装配发现人员
    */
    @ApiModelProperty(value="装配发现人员")
    private String assembleInspector;

    /**
    * 检查来源
    */
    @ApiModelProperty(value="检查来源")
    private String detectionSource;

    /**
    * 创建日期
    */
    @ApiModelProperty(value="创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
    * 修改日期
    */
    @ApiModelProperty(value="修改日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
    * 是否删除：0 - 保留 1- 删除
    */
    @ApiModelProperty(value="是否删除：0 - 保留 1- 删除")
    private Integer delFlag;
}