package com.ennova.pubinfostore.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 公共信息平台用户表
    */
@ApiModel(value="公共信息平台用户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TUserDing {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 姓名
    */
    @ApiModelProperty(value="姓名")
    private String username;

    /**
    * 密码
    */
    @ApiModelProperty(value="密码")
    private String password;

    /**
    * 微信号
    */
    @ApiModelProperty(value="微信号")
    private String wechatNo;

    /**
    * 手机号
    */
    @ApiModelProperty(value="手机号")
    private String mobile;

    /**
    * 工号
    */
    @ApiModelProperty(value="工号")
    private String jobNum;

    /**
    * 单位
    */
    @ApiModelProperty(value="单位")
    private String company;

    /**
    * 部门
    */
    @ApiModelProperty(value="部门")
    private String department;

    /**
    * 职位
    */
    @ApiModelProperty(value="职位")
    private String position;

    /**
    * 状态(0-已启用；1-已禁用)
    */
    @ApiModelProperty(value="状态(0-已启用；1-已禁用)")
    private String status;

    /**
    * 是否删除（0-未删除；1-已删除）
    */
    @ApiModelProperty(value="是否删除（0-未删除；1-已删除）")
    private Integer isDelete;

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
    * 首页是否展示（0-不展示）
    */
    @ApiModelProperty(value="首页是否展示（0-不展示）")
    private Integer isShow;

    /**
    * 用户ID
    */
    @ApiModelProperty(value="用户ID")
    private String userId;

    /**
    * 登录后默认密码是否修改（0-未修改；1-已修改）
    */
    @ApiModelProperty(value="登录后默认密码是否修改（0-未修改；1-已修改）")
    private Integer isUpdate;
}