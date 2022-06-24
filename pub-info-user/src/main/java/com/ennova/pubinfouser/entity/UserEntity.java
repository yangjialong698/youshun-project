package com.ennova.pubinfouser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@ApiModel(value = "用户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号")
    private String wechatNo;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位id")
    private Integer company;

    /**
     * 单位
     */
    @ApiModelProperty(value = "部门")
    private Integer department;

    /**
     * 单位
     */
    @ApiModelProperty(value = "职位")
    private String position;


    /**
     * 状态(0-启用；1-禁用)
     */
    @ApiModelProperty(value = "状态(0-启用；1-禁用)")
    private String status;


    /**
     * 是否删除（0-未删除；1-已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除；1-已删除）")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}

