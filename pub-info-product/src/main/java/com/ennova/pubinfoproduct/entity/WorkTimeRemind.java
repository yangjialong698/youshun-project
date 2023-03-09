package com.ennova.pubinfoproduct.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 工时缺失提醒表
    */
@ApiModel(description="工时缺失提醒表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeRemind {
    @ApiModelProperty(value="")
    private Integer id;

    /**
    * 工作中心
    */
    @ApiModelProperty(value="工作中心")
    private String workCenterNo;

    /**
    * 产品品号
    */
    @ApiModelProperty(value="产品品号")
    private String prdNo;

    /**
    * 单据日期
    */
    @ApiModelProperty(value="单据日期")
    private String orderDate;

    /**
    * 创建日期
    */
    @ApiModelProperty(value="创建日期")
    private Date createTime;
}