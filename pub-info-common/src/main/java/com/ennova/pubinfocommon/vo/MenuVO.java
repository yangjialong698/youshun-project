package com.ennova.pubinfocommon.vo;

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
        @ApiModelProperty(value = "子菜单")
        private List<String> btnName = new ArrayList<>();
    }
}
