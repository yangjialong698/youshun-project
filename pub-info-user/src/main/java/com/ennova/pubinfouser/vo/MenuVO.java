package com.ennova.pubinfouser.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVO {

    @JsonIgnore
    private int id;
    @ApiModelProperty(value = "路由")
    private String path;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "跳转")
    private String redirect;
    @ApiModelProperty(value = "组件")
    private String component;
    @ApiModelProperty(value = "隐藏")
    private Integer hidden;
    @ApiModelProperty(value = "权限约束名")
    private String val;
    @ApiModelProperty(value = "激活")
    private String active;
    @ApiModelProperty(value = "子菜单")
    private List<MenuBan> children = new ArrayList<>();

    @Data
    public static class MenuBan {
        @JsonIgnore
        private int id;
        @ApiModelProperty(value = "路由")
        private String path;
        @ApiModelProperty(value = "名称")
        private String name;
        @ApiModelProperty(value = "图标")
        private String icon;
        @ApiModelProperty(value = "跳转")
        private String redirect;
        @ApiModelProperty(value = "组件")
        private String component;
        @ApiModelProperty(value ="隐藏")
        private Integer hidden;
        @ApiModelProperty(value = "权限约束名")
        private String val;
        @ApiModelProperty(value = "激活")
        private String active;
        @ApiModelProperty(value = "子菜单")
        private List<String> btnName = new ArrayList<>();
    }
}
