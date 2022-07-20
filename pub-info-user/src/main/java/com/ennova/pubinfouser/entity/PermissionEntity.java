package com.ennova.pubinfouser.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel(value = "权限", description = "权限")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity {
    @ApiModelProperty(example = "1", notes = "权限id")
    private Integer id;
    @ApiModelProperty(example = "权限添加", notes = "权限名称")
    private String permissionName;
    @ApiModelProperty(example = "权限添加", notes = "权限描述")
    private String description;
    @ApiModelProperty(example = "权限管理", notes = "权限约束名")
    private String val;
    @ApiModelProperty(example = "权限管理", notes = "上级权限约束名")
    private String parentVal;
    @ApiModelProperty(example = "1", notes = "上级权限id")
    private Integer parentId;
    @ApiModelProperty(example = "1", notes = "权限等级（1-一级；2-二级；3-三级）")
    private Integer level;
    @ApiModelProperty(example = "1", notes = "权限等级（1-平台；2-微信）")
    private Integer platform;
    @ApiModelProperty(example = "1", notes = "0菜单、1权限按钮")
    private Integer permissionType;
    @ApiModelProperty(example = "1", notes = "排序")
    public Integer sort;
    @ApiModelProperty(example = "index.html", notes = "路由")
    private String url;
    @ApiModelProperty(example = "/icon/img.png", notes = "图标")
    private String icon;
    @ApiModelProperty(example = "1", notes = "权限类型")
    private Integer typeModule;

    private Integer flag;

    private Date createTime;
    @ApiModelProperty(example = "1", notes = "跳转")
    private String redirect;
    @ApiModelProperty(example = "1", notes = "组件")
    private String component;
    @ApiModelProperty(example = "1", notes = "隐藏")
    private Integer hidden;
    @ApiModelProperty(example = "1", notes = "激活")
    private String active;
    @ApiModelProperty(example = "1", notes = "系统编号")
    private String systemNo;



}
