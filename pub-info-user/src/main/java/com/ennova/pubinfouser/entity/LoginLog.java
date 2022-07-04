package com.ennova.pubinfouser.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录日志
 */
@ApiModel(value = "登录日志")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 操作IP
     */
    @ApiModelProperty(value = "操作IP")
    private String requestIp;

    /**
     * 登录人ID
     */
    @ApiModelProperty(value = "登录人ID")
    private Integer userId;

    /**
     * 登录人姓名
     */
    @ApiModelProperty(value = "登录人姓名")
    private String userName;

    /**
     * 登录人账号
     */
    @ApiModelProperty(value = "登录人账号")
    private String account;

    /**
     * 登录描述
     */
    @ApiModelProperty(value = "登录描述")
    private String description;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private Date loginDate;

    /**
     * 浏览器请求头
     */
    @ApiModelProperty(value = "浏览器请求头")
    private String ua;

    /**
     * 登录地点
     */
    @ApiModelProperty(value = "登录地点")
    private String location;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}