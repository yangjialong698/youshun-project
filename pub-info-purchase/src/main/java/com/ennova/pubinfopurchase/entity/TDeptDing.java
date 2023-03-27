package com.ennova.pubinfopurchase.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
    * 公共信息平台部门表
    */
@ApiModel(value="公共信息平台部门表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TDeptDing {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 部门名称
    */
    @ApiModelProperty(value="部门名称")
    private String deptName;

    /**
    * 使用状态(0-已启用；1-已禁用)
    */
    @ApiModelProperty(value="使用状态(0-已启用；1-已禁用)")
    private String useStatus;

    /**
    * 是否可修改（0-可以；1-不可以）
    */
    @ApiModelProperty(value="是否可修改（0-可以；1-不可以）")
    private Integer isOperate;

    /**
    * 删除（0-未删除；1-已删除）
    */
    @ApiModelProperty(value="删除（0-未删除；1-已删除）")
    private Integer isDelete;

    /**
    * 企业id
    */
    @ApiModelProperty(value="企业id")
    private Integer company;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value="修改时间")
    private Date updateTime;

    /**
    * 上级部门ID
    */
    @ApiModelProperty(value="上级部门ID")
    private Long parentId;

    /**
    * 部门ID
    */
    @ApiModelProperty(value="部门ID")
    private Long deptId;

    /**
    * 主管用户ID
    */
    @ApiModelProperty(value="主管用户ID")
    private String manageId;
}