package com.ennova.pubinfouser.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@ApiModel(value = "用户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

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
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String jobNum;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位id")
    private Integer company;

    /**
     * 部门
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

    private String deptName;

    private String roleName;

    private String roleCode;

    private Integer roleId;

    @ApiModelProperty(value = "token验证是否登录")
    private String token;

    @ApiModelProperty(value = "refreshToken过期刷新token")
    private String refreshToken;

//    @ApiModelProperty(value = "一级菜单")
//    private List<TUserSystem> tUserSystems;

    @ApiModelProperty(notes = "菜单")
    private List<MenuVO> menu;

    @ApiModelProperty(notes = "新菜单")
    private List<NewMenuVO> newMenu;

    @ApiModelProperty(notes = "权限模块")
    private List<MenuVO> permissionsModule;

    /**
     * 是否修改默认密码（0-未修改；1-已修改）
     */
    @ApiModelProperty(value = "是否修改（0-未修改；1-已修改）")
    private Integer isUpdate;
}

