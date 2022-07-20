package com.ennova.pubinfouser.vo;

import com.ennova.pubinfouser.entity.PermissionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewMenuVO {
    @ApiModelProperty(value = "一级系统菜单")
    private String sysNum;

    @ApiModelProperty(value = "一级系统菜单名称")
    private String sysName;

    @ApiModelProperty(value = "任务平台2，3,4级菜单")
    private List<MenuVO> menu;

}
