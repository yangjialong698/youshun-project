package com.ennova.pubinfocommon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String face;

    /**
     * 角色id
     */
    @Value("role_id")
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色id")
    private String roleName;

    @ApiModelProperty(value = "roleCode")
    private String roleCode;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位id")
    private String company;

    @ApiModelProperty(value = "类别（0-平台；1-企业）")
    private Integer platform;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位名")
    private String companyName;

    /**
     * 单位
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 单位
     */
    @ApiModelProperty(value = "职位")
    private String position;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String areaId;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 状态(0-启用；1-禁用)
     */
    @ApiModelProperty(value = "状态(0-启用；1-禁用)")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    /**
     * 是否删除（0-未删除；1-已删除）
     */
    @ApiModelProperty(value = "是否删除（0-未删除；1-已删除）")
    private Integer isDelete;

    @ApiModelProperty(value = "token验证是否登录")
    private String token;

    @ApiModelProperty(value = "refreshToken过期刷新token")
    private String refreshToken;

    @ApiModelProperty(notes = "菜单")
    private List<MenuVO> menu;

    @ApiModelProperty(notes = "权限模块")
    private List<MenuVO> permissionsModule;
}

