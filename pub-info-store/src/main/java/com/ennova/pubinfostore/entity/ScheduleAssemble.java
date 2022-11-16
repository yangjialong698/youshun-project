package com.ennova.pubinfostore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 排产装配表
    */
@ApiModel(value="排产装配表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleAssemble {
    /**
    * 主键
    */
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
    * 排产日期
    */
    @ApiModelProperty(value="排产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryDate;

    /**
    * 产品品号（非外键）
    */
    @ApiModelProperty(value="产品品号（非外键）")
    private String productId;

    /**
    * 产品名称
    */
    @ApiModelProperty(value="产品名称")
    private String productName;

    /**
    * 工单号
    */
    @ApiModelProperty(value="工单号")
    private String workOrder;

    /**
    * 开单总数
    */
    @ApiModelProperty(value="开单总数")
    private Integer totalOrder;

    /**
    * 生产总数
    */
    @ApiModelProperty(value="生产总数")
    private Integer totalProduction;

    /**
    * 备注
    */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
    * 需生产日期
    */
    @ApiModelProperty(value="需生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date needDate;

    /**
    * 实际完成状态
    */
    @ApiModelProperty(value="实际完成状态: 0-未完成,1-已完成")
    private Integer status;

    /**
    * 装配人员ID
    */
    @ApiModelProperty(value="装配人员ID")
    private String assembleId;

    /**
    * 是否删除：（冗余字段，实际无使用）
    */
    @ApiModelProperty(value="是否删除：（冗余字段，实际无使用）")
    @JsonIgnore
    private Integer flag;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}